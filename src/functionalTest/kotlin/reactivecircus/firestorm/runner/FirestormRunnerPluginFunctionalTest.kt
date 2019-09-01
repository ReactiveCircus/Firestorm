package reactivecircus.firestorm.runner

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import reactivecircus.firestorm.defaultTopLevelBuildScript
import reactivecircus.firestorm.gradleRunner
import reactivecircus.firestorm.runWithFixtures
import java.io.File

class FirestormRunnerPluginFunctionalTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `fails build when applying runner plugin to subproject`() {
        val appProjectWithoutFlavor = File("src/functionalTest/fixtures/application-with-runner-plugin")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(appProjectWithoutFlavor),
            "clean"
        ) { buildAndFail() }

        assertThat(result.output).contains(
            "Please apply Firestorm Runner plugin to the rootProject."
        )
    }
}
