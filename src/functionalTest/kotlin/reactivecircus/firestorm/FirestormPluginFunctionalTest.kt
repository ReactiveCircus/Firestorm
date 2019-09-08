package reactivecircus.firestorm

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FirestormPluginFunctionalTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `fails build when applying plugin to subproject without Android Application or Android Library plugin`() {
        val kotlinJvmProject = Fixture("src/functionalTest/fixtures/kotlin")
        withFixtureRunner(
            rootProject = testProjectRoot,
            fixtures = listOf(kotlinJvmProject)
        ).runAndExpectFailure(
            "clean"
        ) {
            assertThat(output).contains(
                "Firestorm plugin is designed to work with Android projects but project ':kotlin' doesn't have either 'com.android.library' or 'com.android.application' plugin applied."
            )
        }
    }

    @Test
    fun `fails build when applying plugin to root project`() {
        withFixtureRunner(
            rootProject = testProjectRoot,
            fixtures = emptyList(),
            topLevelBuildScript =
            """
            plugins {
                id 'io.github.reactivecircus.firestorm'
            }
            """.trimIndent()
        ).runAndExpectFailure(
            "clean"
        ) {
            assertThat(output).contains(
                "Please apply Firestorm plugin directly to Android Application or Library subproject(s)."
            )
        }
    }

    @Test
    fun `firestorm tasks are registered when plugin is enabled`() {
        val appProjectWithoutFlavor = Fixture(
            path = "src/functionalTest/fixtures/application-no-flavor",
            pluginConfigs = """
            firestorm {
                enabled = true
            }
            """.trimIndent()
        )
        withFixtureRunner(
            rootProject = testProjectRoot,
            fixtures = listOf(appProjectWithoutFlavor)
        ).runAndCheckResult(
            "tasks", "--group=firestorm"
        ) {
            assertThat(output).contains("Firestorm tasks")
        }
    }

    @Test
    fun `firestorm tasks are not registered when plugin is disabled`() {
        val appProjectWithoutFlavor = Fixture(
            path = "src/functionalTest/fixtures/application-no-flavor",
            pluginConfigs = """
            firestorm {
                enabled = false
            }
            """.trimIndent()
        )
        withFixtureRunner(
            rootProject = testProjectRoot,
            fixtures = listOf(appProjectWithoutFlavor)
        ).runAndCheckResult(
            "tasks", "--group=firestorm"
        ) {
            assertThat(output).doesNotContain("Firestorm firestorm")
            assertThat(output).contains("No tasks")
            assertThat(output).contains("Firestorm plugin is disabled.")
        }
    }

    // TODO test missing required configs e.g. FTL credentials
}
