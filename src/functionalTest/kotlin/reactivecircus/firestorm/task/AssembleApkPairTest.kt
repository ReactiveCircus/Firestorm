package reactivecircus.firestorm.task

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import reactivecircus.firestorm.assertFileExists
import reactivecircus.firestorm.withFixtureRunner
import java.io.File

class AssembleApkPairTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `generates 1 app APK and 1 test APK for a project with only app subproject (no product flavor)`() {
        val appProjectWithoutFlavor = File("src/functionalTest/fixtures/application-no-flavor")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(appProjectWithoutFlavor)
        ).runAndCheckResult(
            "assembleDebugApkPair"
        ) {
            assertThat(task(":application-no-flavor:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-no-flavor:packageDebug")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-no-flavor:packageDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-no-flavor:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":generateDummyApk")?.outcome)
                .isNull()

            assertThat(output)
                .contains("/build/outputs/firestorm/application-no-flavor/debug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/application-no-flavor/debug/test.apk.")

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-no-flavor/debug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-no-flavor/debug/test.apk")
            )
        }
    }

    @Test
    fun `generates 1 app APK and 1 test APK for a project with only app subproject (with product flavors)`() {
        val appProjectWithFlavors = File("src/functionalTest/fixtures/application-with-flavors")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(appProjectWithFlavors)
        ).runAndCheckResult(
            "assembleMockDebugApkPair"
        ) {
            assertThat(task(":generateDummyApk")?.outcome)
                .isNull()
            assertThat(task(":application-with-flavors:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:packageMockDebug")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:packageMockDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:assembleMockDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(output)
                .contains("/build/outputs/firestorm/application-with-flavors/mockDebug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/application-with-flavors/mockDebug/test.apk.")

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/test.apk")
            )

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/test.apk")
            )
        }
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with 1 app subproject (with project flavors) and multiple Android Library subprojects`() {
        val appProjectWithFlavors = File("src/functionalTest/fixtures/application-with-flavors")
        val libraryProjectWithoutFlavors1 = File("src/functionalTest/fixtures/library-no-flavor-1")
        val libraryProjectWithoutFlavors2 = File("src/functionalTest/fixtures/library-no-flavor-2")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(appProjectWithFlavors, libraryProjectWithoutFlavors1, libraryProjectWithoutFlavors2)
        ).runAndCheckResult(
            "assembleMockDebugApkPair", "assembleDebugApkPair"
        ) {
            assertThat(task(":application-with-flavors:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:packageMockDebug")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:packageMockDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":application-with-flavors:assembleMockDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":library-no-flavor-1:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-1:packageDebug")?.outcome)
                .isNull()
            assertThat(task(":library-no-flavor-1:packageDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-1:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":library-no-flavor-2:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-2:packageDebug")?.outcome)
                .isNull()
            assertThat(task(":library-no-flavor-2:packageDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-2:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(output)
                .contains("/build/outputs/firestorm/application-with-flavors/mockDebug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/application-with-flavors/mockDebug/test.apk.")

            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-1/debug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-1/debug/test.apk.")

            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-2/debug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-2/debug/test.apk.")

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/application-with-flavors/mockDebug/test.apk")
            )

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-1/debug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-1/debug/test.apk")
            )

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-2/debug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-2/debug/test.apk")
            )
        }
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with only Android Library subprojects (no product flavor)`() {
        val libraryProjectWithoutFlavors1 = File("src/functionalTest/fixtures/library-no-flavor-1")
        val libraryProjectWithoutFlavors2 = File("src/functionalTest/fixtures/library-no-flavor-2")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithoutFlavors1, libraryProjectWithoutFlavors2)
        ).runAndCheckResult(
            "assembleDebugApkPair"
        ) {
            assertThat(task(":library-no-flavor-1:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-1:packageDebug")?.outcome)
                .isNull()
            assertThat(task(":library-no-flavor-1:packageDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-1:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":library-no-flavor-2:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-2:packageDebug")?.outcome)
                .isNull()
            assertThat(task(":library-no-flavor-2:packageDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-2:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":generateDummyApk")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-1/debug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-1/debug/test.apk.")

            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-2/debug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-no-flavor-2/debug/test.apk.")

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-1/debug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-1/debug/test.apk")
            )

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-2/debug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-no-flavor-2/debug/test.apk")
            )
        }
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with only Android Library subprojects (with product flavors)`() {
        val libraryProjectWithFlavors1 = File("src/functionalTest/fixtures/library-with-flavors-1")
        val libraryProjectWithFlavors2 = File("src/functionalTest/fixtures/library-with-flavors-2")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithFlavors1, libraryProjectWithFlavors2)
        ).runAndCheckResult(
            "assembleMockDebugApkPair"
        ) {
            assertThat(task(":library-with-flavors-1:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-with-flavors-1:packageMockDebug")?.outcome)
                .isNull()
            assertThat(task(":library-with-flavors-1:packageMockDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-with-flavors-1:assembleMockDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(task(":library-with-flavors-2:checkIncrementalSourceChange")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-with-flavors-2:packageMockDebug")?.outcome)
                .isNull()
            assertThat(task(":library-with-flavors-2:packageMockDebugAndroidTest")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-with-flavors-2:assembleMockDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)

            assertThat(output)
                .contains("/build/outputs/firestorm/library-with-flavors-1/mockDebug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-with-flavors-1/mockDebug/test.apk.")

            assertThat(output)
                .contains("/build/outputs/firestorm/library-with-flavors-2/mockDebug/app.apk.")
            assertThat(output)
                .contains("/build/outputs/firestorm/library-with-flavors-2/mockDebug/test.apk.")

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-with-flavors-1/mockDebug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-with-flavors-1/mockDebug/test.apk")
            )

            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-with-flavors-2/mockDebug/app.apk")
            )
            assertFileExists(
                File(testProjectRoot.root, "build/outputs/firestorm/library-with-flavors-2/mockDebug/test.apk")
            )
        }
    }

    @Test
    fun `skips checkIncrementalSourceChange task when incrementalAssembler is disabled`() {
        val appProjectWithoutIncrementalAssembler = File("src/functionalTest/fixtures/application-without-incremental-assembler")
        withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(appProjectWithoutIncrementalAssembler)
        ).runAndCheckResult(
            "assembleDebugApkPair"
        ) {
            assertThat(task(":application-without-incremental-assembler:checkIncrementalSourceChange")?.outcome)
                .isNull()
            assertThat(task(":application-without-incremental-assembler:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
        }
    }

    @Test
    fun `AssembleApkPair supports incremental build`() {
        val appProjectWithoutFlavor = File("src/functionalTest/fixtures/application-no-flavor")
        val runner = withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(appProjectWithoutFlavor)
        )

        runner.runAndCheckResult(
            "assembleDebugApkPair"
        ) {
            assertThat(task(":application-no-flavor:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
        }

        runner.runAndCheckResult(
            "assembleDebugApkPair"
        ) {
            assertThat(task(":application-no-flavor:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.UP_TO_DATE)
        }
    }

    @Test
    fun `AssembleApkPair is cacheable`() {
        val libraryProjectWithoutFlavors = File("src/functionalTest/fixtures/library-no-flavor-1")
        val runner = withFixtureRunner(
            rootProject = testProjectRoot,
            subprojects = listOf(libraryProjectWithoutFlavors)
        )

        runner.runAndCheckResult(
            "assembleDebugApkPair", "--build-cache"
        ) {
            assertThat(task(":library-no-flavor-1:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
        }

        runner.runAndCheckResult(
            "clean", "assembleDebugApkPair", "--build-cache"
        ) {
            assertThat(task(":clean")?.outcome)
                .isEqualTo(TaskOutcome.SUCCESS)
            assertThat(task(":library-no-flavor-1:assembleDebugApkPair")?.outcome)
                .isEqualTo(TaskOutcome.FROM_CACHE)
        }
    }
}
