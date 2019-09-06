package reactivecircus.firestorm.task

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import reactivecircus.firestorm.assertFileExists
import reactivecircus.firestorm.withFixtureRunner
import java.io.File

class GenerateDummyApkTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `generates dummy APK`() {
        val libraryProjectWithoutFlavors = File("src/functionalTest/fixtures/library-no-flavor-1")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithoutFlavors)
        ).runAndCheckResult(
            "generateDummyApk"
        ) {
            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/dummy.apk")
            )
        }
    }

    @Test
    fun `GenerateDummyApk supports incremental build`() {
        val libraryProjectWithoutFlavors = File("src/functionalTest/fixtures/library-no-flavor-1")
        val runner = withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithoutFlavors)
        )

        runner.runAndCheckResult(
            "generateDummyApk"
        ) {
            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
        }

        runner.runAndCheckResult(
            "generateDummyApk"
        ) {
            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.UP_TO_DATE)
        }
    }

    @Test
    fun `GenerateDummyApk is cacheable`() {
        val libraryProjectWithoutFlavors = File("src/functionalTest/fixtures/library-no-flavor-1")
        val runner = withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithoutFlavors)
        )

        runner.runAndCheckResult(
            "generateDummyApk", "--build-cache"
        ) {
            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
        }

        runner.runAndCheckResult(
            "clean", "generateDummyApk", "--build-cache"
        ) {
            assertThat(task(":clean")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.FROM_CACHE)
        }
    }
}
