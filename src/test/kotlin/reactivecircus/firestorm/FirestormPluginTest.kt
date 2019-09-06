package reactivecircus.firestorm

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.google.common.truth.Truth.assertThat
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertThrows
import org.junit.Test

class FirestormPluginTest {

    private val rootProject = ProjectBuilder.builder().withName("root").build()
    private val appProject = ProjectBuilder.builder().withParent(rootProject).withName("app").build()
    private val libraryProject = ProjectBuilder.builder().withParent(rootProject).withName("library").build()

    @Test
    fun `plugin registers tasks for Android Application subproject without product flavor`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = false)

        (appProject as DefaultProject).evaluate()

        assertCheckIncrementalSourceChangeTaskRegistered(appProject)
        assertGenerateDummyApkTaskRegisteredOnRootProject()

        val assembleDebugApkPair = appProject.tasks.getByName("assembleDebugApkPair")
        assertThat(assembleDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleDebugApkPair.description).isEqualTo("Assembles app and test APKs for debug.")
        val assembleReleaseApkPair = appProject.tasks.findByName("assembleReleaseApkPair")
        assertThat(assembleReleaseApkPair).isNull()

        val runDebugTestsOnFirebaseTestLab = appProject.tasks.getByName("runDebugTestsOnFirebaseTestLab")
        assertThat(runDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for debug on Firebase Test Lab.")
        val runReleaseTestsOnFirebaseTestLab = appProject.tasks.findByName("runReleaseTestsOnFirebaseTestLab")
        assertThat(runReleaseTestsOnFirebaseTestLab).isNull()
    }

    @Test
    fun `plugin registers tasks for Android Application subproject with product flavor`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = true)

        (appProject as DefaultProject).evaluate()

        assertCheckIncrementalSourceChangeTaskRegistered(appProject)
        assertGenerateDummyApkTaskRegisteredOnRootProject()

        val assembleMockDebugApkPair = appProject.tasks.getByName("assembleMockDebugApkPair")
        assertThat(assembleMockDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleMockDebugApkPair.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        val assembleProdDebugApkPair = appProject.tasks.getByName("assembleProdDebugApkPair")
        assertThat(assembleProdDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleProdDebugApkPair.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        val assembleMockReleaseApkPair = appProject.tasks.findByName("assembleMockReleaseApkPair")
        assertThat(assembleMockReleaseApkPair).isNull()
        val assembleProdReleaseApkPair = appProject.tasks.findByName("assembleProdReleaseApkPair")
        assertThat(assembleProdReleaseApkPair).isNull()

        val runMockDebugTestsOnFirebaseTestLab = appProject.tasks.getByName("runMockDebugTestsOnFirebaseTestLab")
        assertThat(runMockDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runMockDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for mockDebug on Firebase Test Lab.")
        val runProdDebugTestsOnFirebaseTestLab = appProject.tasks.getByName("runProdDebugTestsOnFirebaseTestLab")
        assertThat(runProdDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runProdDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for prodDebug on Firebase Test Lab.")
        val runMockReleaseTestsOnFirebaseTestLab = appProject.tasks.findByName("runMockReleaseTestsOnFirebaseTestLab")
        assertThat(runMockReleaseTestsOnFirebaseTestLab).isNull()
        val runProdReleaseTestsOnFirebaseTestLab = appProject.tasks.findByName("runProdReleaseTestsOnFirebaseTestLab")
        assertThat(runProdReleaseTestsOnFirebaseTestLab).isNull()
    }

    @Test
    fun `plugin registers tasks for Android Library subproject without product flavor`() {
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormPlugin::class.java)

        libraryProject.createAndroidLibraryProject(hasProductFlavor = false)

        (libraryProject as DefaultProject).evaluate()

        val assembleDebugApkPair = libraryProject.tasks.getByName("assembleDebugApkPair")
        assertThat(assembleDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleDebugApkPair.description).isEqualTo("Assembles app and test APKs for debug.")

        val assembleReleaseApkPair = libraryProject.tasks.findByName("assembleReleaseApkPair")
        assertThat(assembleReleaseApkPair).isNull()
    }

    @Test
    fun `plugin registers tasks for Android Library subproject with product flavor`() {
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormPlugin::class.java)

        libraryProject.createAndroidLibraryProject(hasProductFlavor = true)

        (libraryProject as DefaultProject).evaluate()

        assertCheckIncrementalSourceChangeTaskRegistered(libraryProject)
        assertGenerateDummyApkTaskRegisteredOnRootProject()

        val assembleMockDebugApkPair = libraryProject.tasks.getByName("assembleMockDebugApkPair")
        assertThat(assembleMockDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleMockDebugApkPair.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        val assembleProdDebugApkPair = libraryProject.tasks.getByName("assembleProdDebugApkPair")
        assertThat(assembleProdDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleProdDebugApkPair.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        val assembleMockReleaseApkPair = libraryProject.tasks.findByName("assembleMockReleaseApkPair")
        assertThat(assembleMockReleaseApkPair).isNull()
        val assembleProdReleaseApkPair = libraryProject.tasks.findByName("assembleProdReleaseApkPair")
        assertThat(assembleProdReleaseApkPair).isNull()

        val runMockDebugTestsOnFirebaseTestLab = libraryProject.tasks.getByName("runMockDebugTestsOnFirebaseTestLab")
        assertThat(runMockDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runMockDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for mockDebug on Firebase Test Lab.")
        val runProdDebugTestsOnFirebaseTestLab = libraryProject.tasks.getByName("runProdDebugTestsOnFirebaseTestLab")
        assertThat(runProdDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runProdDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for prodDebug on Firebase Test Lab.")
        val runMockReleaseTestsOnFirebaseTestLab =
            libraryProject.tasks.findByName("runMockReleaseTestsOnFirebaseTestLab")
        assertThat(runMockReleaseTestsOnFirebaseTestLab).isNull()
        val runProdReleaseTestsOnFirebaseTestLab =
            libraryProject.tasks.findByName("runProdReleaseTestsOnFirebaseTestLab")
        assertThat(runProdReleaseTestsOnFirebaseTestLab).isNull()
    }

    @Test
    fun `plugin registers tasks for multi-project project`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormPlugin::class.java)
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = true)
        libraryProject.createAndroidLibraryProject(hasProductFlavor = false)

        (rootProject as DefaultProject).evaluate()
        (appProject as DefaultProject).evaluate()
        (libraryProject as DefaultProject).evaluate()

        assertCheckIncrementalSourceChangeTaskRegistered(appProject)

        val assembleMockDebugApkPair = appProject.tasks.getByName("assembleMockDebugApkPair")
        assertThat(assembleMockDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleMockDebugApkPair.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        val assembleProdDebugApkPair = appProject.tasks.getByName("assembleProdDebugApkPair")
        assertThat(assembleProdDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleProdDebugApkPair.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        val assembleMockReleaseApkPair = appProject.tasks.findByName("assembleMockReleaseApkPair")
        assertThat(assembleMockReleaseApkPair).isNull()
        val assembleProdReleaseApkPair = appProject.tasks.findByName("assembleProdReleaseApkPair")
        assertThat(assembleProdReleaseApkPair).isNull()

        val runMockDebugTestsOnFirebaseTestLab = appProject.tasks.getByName("runMockDebugTestsOnFirebaseTestLab")
        assertThat(runMockDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runMockDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for mockDebug on Firebase Test Lab.")
        val runProdDebugTestsOnFirebaseTestLab = appProject.tasks.getByName("runProdDebugTestsOnFirebaseTestLab")
        assertThat(runProdDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runProdDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for prodDebug on Firebase Test Lab.")
        val runMockReleaseTestsOnFirebaseTestLab = appProject.tasks.findByName("runMockReleaseTestsOnFirebaseTestLab")
        assertThat(runMockReleaseTestsOnFirebaseTestLab).isNull()
        val runProdReleaseTestsOnFirebaseTestLab = appProject.tasks.findByName("runProdReleaseTestsOnFirebaseTestLab")
        assertThat(runProdReleaseTestsOnFirebaseTestLab).isNull()

        val assembleDebugApkPair = libraryProject.tasks.getByName("assembleDebugApkPair")
        assertThat(assembleDebugApkPair.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(assembleDebugApkPair.description).isEqualTo("Assembles app and test APKs for debug.")
        val assembleReleaseApkPair = libraryProject.tasks.findByName("assembleReleaseApkPair")
        assertThat(assembleReleaseApkPair).isNull()

        val runDebugTestsOnFirebaseTestLab = libraryProject.tasks.getByName("runDebugTestsOnFirebaseTestLab")
        assertThat(runDebugTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(runDebugTestsOnFirebaseTestLab.description).isEqualTo("Runs tests for debug on Firebase Test Lab.")
        val runReleaseTestsOnFirebaseTestLab = libraryProject.tasks.findByName("runReleaseTestsOnFirebaseTestLab")
        assertThat(runReleaseTestsOnFirebaseTestLab).isNull()

        assertGenerateDummyApkTaskRegisteredOnRootProject()

        assertThat(rootProject.getTasksByName("checkIncrementalSourceChange", true)).hasSize(2)
        assertThat(rootProject.getTasksByName("generateDummyApk", true)).hasSize(1)
        assertThat(rootProject.getTasksByName("assembleDebugApkPair", true)).hasSize(1)
        assertThat(rootProject.getTasksByName("assembleMockDebugApkPair", true)).hasSize(1)
        assertThat(rootProject.getTasksByName("assembleProdDebugApkPair", true)).hasSize(1)
    }

    @Test
    fun `plugin cannot be applied to root project`() {
        rootProject.pluginManager.apply(FirestormPlugin::class.java)

        assertThrows(ProjectConfigurationException::class.java) {
            (rootProject as DefaultProject).evaluate()
        }
    }

    @Test
    fun `plugin cannot be applied to subproject without Android App or Android Library plugin`() {
        val kotlinJvmProject = ProjectBuilder.builder().withParent(rootProject).withName("kotlin").build()
        kotlinJvmProject.pluginManager.apply("kotlin")
        kotlinJvmProject.pluginManager.apply(FirestormPlugin::class.java)

        assertThrows(ProjectConfigurationException::class.java) {
            (kotlinJvmProject as DefaultProject).evaluate()
        }
    }

    private fun assertCheckIncrementalSourceChangeTaskRegistered(project: Project) {
        val checkIncrementalSourceChange = project.tasks.getByName("checkIncrementalSourceChange")
        assertThat(checkIncrementalSourceChange.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(checkIncrementalSourceChange.description).isEqualTo("Checks if project source has changed based on difference from the previous git commit.")
    }

    private fun assertGenerateDummyApkTaskRegisteredOnRootProject() {
        val generateDummyApk = rootProject.tasks.getByName("generateDummyApk")
        assertThat(generateDummyApk.group).isEqualTo(FIRESTORM_TASK_GROUP)
        assertThat(generateDummyApk.description).isEqualTo("Generates a dummy APK for running Android Library tests on Firebase Test Lab.")
    }
}
