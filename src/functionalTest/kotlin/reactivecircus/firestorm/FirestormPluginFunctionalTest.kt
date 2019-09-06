package reactivecircus.firestorm

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FirestormPluginFunctionalTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `fails build when applying plugin to subproject without Android Application or Android Library plugin`() {
        val kotlinJvmProject = File("src/functionalTest/fixtures/kotlin")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(kotlinJvmProject)
        ).runAndExpectFailure(
            "clean"
        ) {
            assertThat(output).contains(
                "Please make sure either the 'com.android.library' or 'com.android.application' plugin is applied to the project."
            )
        }
    }

    @Test
    fun `fails build when applying plugin to root project`() {
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = emptyList(),
            topLevelBuildScript = """
                plugins {
                    id 'io.github.reactivecircus.firestorm'
                }
            """.trimIndent()
        ).runAndExpectFailure(
            "clean"
        ) {
            assertThat(output).contains(
                "Please apply Firestorm plugin directly to subproject(s)."
            )
        }
    }

    // TODO test missing required configs e.g. FTL credentials
}
