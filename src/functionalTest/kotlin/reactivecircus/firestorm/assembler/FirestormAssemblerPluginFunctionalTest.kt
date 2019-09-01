package reactivecircus.firestorm.assembler

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import reactivecircus.firestorm.defaultTopLevelBuildScript
import reactivecircus.firestorm.gradleRunner
import reactivecircus.firestorm.runWithFixtures
import java.io.File

class FirestormAssemblerPluginFunctionalTest {

    @get:Rule
    val testProjectRoot = TemporaryFolder()

    @Test
    fun `generates 1 app APK and 1 test APK for a project with only app subproject (no product flavor)`() {
        val appProjectWithoutFlavor = File("src/functionalTest/fixtures/application-no-flavor")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(appProjectWithoutFlavor),
            "assembleDebugTestApk"
        ) { build() }

        assertThat(result.output).contains("BUILD SUCCESSFUL")

        /** TODO assert task output contains "generated xxx.apk"
        assertThat(result.output).contains(
        "..."
        )
         **/

        // TODO assert expected APK generated at location...
    }

    @Test
    fun `generates 1 app APK and 1 test APK for a project with only app subproject (with product flavors)`() {
        val appProjectWithFlavors = File("src/functionalTest/fixtures/application-with-flavors")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(appProjectWithFlavors),
            "assembleMockDebugTestApk"
        ) { build() }

        assertThat(result.output).contains("BUILD SUCCESSFUL")

        /** TODO assert task output contains "generated xxx.apk"
        assertThat(result.output).contains(
        "..."
        )
         **/

        // TODO assert expected APK generated at location...
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with 1 app subproject (with project flavors) and multiple Android library subprojects`() {
        val appProjectWithFlavors = File("src/functionalTest/fixtures/application-with-flavors")
        val libraryProjectWithoutFlavors1 = File("src/functionalTest/fixtures/library-no-flavor-1")
        val libraryProjectWithoutFlavors2 = File("src/functionalTest/fixtures/library-no-flavor-2")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(appProjectWithFlavors, libraryProjectWithoutFlavors1, libraryProjectWithoutFlavors2),
            "assembleMockDebugTestApk", "assembleDebugTestApk"
        ) { build() }

        assertThat(result.output).contains("BUILD SUCCESSFUL")

        /** TODO assert task output contains "generated xxx.apk"
        assertThat(result.output).contains(
        "..."
        )
         **/

        // TODO assert expected APK generated at location...
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with only Android library subprojects (no product flavor)`() {
        val libraryProjectWithoutFlavors1 = File("src/functionalTest/fixtures/library-no-flavor-1")
        val libraryProjectWithoutFlavors2 = File("src/functionalTest/fixtures/library-no-flavor-2")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(libraryProjectWithoutFlavors1, libraryProjectWithoutFlavors2),
            "assembleDebugTestApk"
        ) { build() }

        assertThat(result.output).contains("BUILD SUCCESSFUL")

        /** TODO assert task output contains "generated xxx.apk"
        assertThat(result.output).contains(
        "..."
        )
         **/

        // TODO assert expected APK generated at location...
    }

    @Test
    fun `generates 1 app APK and 1 test APK per subproject for a project with only Android library subprojects (with product flavors)`() {
        val libraryProjectWithFlavors1 = File("src/functionalTest/fixtures/library-with-flavors-1")
        val libraryProjectWithFlavors2 = File("src/functionalTest/fixtures/library-with-flavors-2")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(libraryProjectWithFlavors1, libraryProjectWithFlavors2),
            "assembleMockDebugTestApk"
        ) { build() }

        assertThat(result.output).contains("BUILD SUCCESSFUL")

        /** TODO assert task output contains "generated xxx.apk"
        assertThat(result.output).contains(
        "..."
        )
         **/

        // TODO assert expected APK generated at location...
    }

    @Test
    fun `fails build when applying assembler plugin to subproject without Android Application or Android Library plugin`() {
        val kotlinJvmProject = File("src/functionalTest/fixtures/kotlin")
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            defaultTopLevelBuildScript,
            listOf(kotlinJvmProject),
            "clean"
        ) { buildAndFail() }

        assertThat(result.output).contains(
            "Please make sure either the 'com.android.library' or 'com.android.application' plugin is applied before the Firestorm Assembler plugin."
        )
    }

    @Test
    fun `fails build when applying assembler plugin to root project`() {
        val result = gradleRunner.runWithFixtures(
            testProjectRoot,
            """
                plugins {
                    id 'firestorm-assembler'
                }
            """.trimIndent(),
            emptyList(),
            "clean"
        ) { buildAndFail() }

        assertThat(result.output).contains(
            "Please apply Firestorm Assembler plugin directly to subproject(s)."
        )
    }
}
