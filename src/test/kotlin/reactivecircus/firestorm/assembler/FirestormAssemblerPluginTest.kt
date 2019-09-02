package reactivecircus.firestorm.assembler

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.google.common.truth.Truth.assertThat
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertThrows
import org.junit.Test
import reactivecircus.firestorm.FIRESTORM_GROUP
import reactivecircus.firestorm.createAndroidAppProject
import reactivecircus.firestorm.createAndroidLibraryProject
import reactivecircus.firestorm.dependsOnTask

class FirestormAssemblerPluginTest {

    private val rootProject = ProjectBuilder.builder().withName("root").build()
    private val appProject = ProjectBuilder.builder().withParent(rootProject).withName("app").build()
    private val libraryProject = ProjectBuilder.builder().withParent(rootProject).withName("library").build()

    @Test
    fun `assembler plugin registers tasks for Android Application subproject without product flavor`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = false)

        (appProject as DefaultProject).evaluate()

        val assembleDebugTestApk = appProject.tasks.getByName("assembleDebugTestApk")
        assertThat(assembleDebugTestApk.description).isEqualTo("Assembles app and test APKs for debug.")
        assertThat(assembleDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleDebugTestApk.dependsOnTask("assembleDebugAndroidTest")).isTrue()

        val assembleReleaseTestApk = appProject.tasks.findByName("assembleReleaseTestApk")
        assertThat(assembleReleaseTestApk).isNull()
        // TODO assert properties / configurations
    }

    @Test
    fun `assembler plugin registers tasks for Android Application subproject with product flavor`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = true)

        (appProject as DefaultProject).evaluate()

        val assembleMockDebugTestApk = appProject.tasks.getByName("assembleMockDebugTestApk")
        assertThat(assembleMockDebugTestApk.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        assertThat(assembleMockDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleMockDebugTestApk.dependsOnTask("assembleMockDebugAndroidTest")).isTrue()

        val assembleProdDebugTestApk = appProject.tasks.getByName("assembleProdDebugTestApk")
        assertThat(assembleProdDebugTestApk.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        assertThat(assembleProdDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleProdDebugTestApk.dependsOnTask("assembleProdDebugAndroidTest")).isTrue()

        val assembleMockReleaseTestApk = appProject.tasks.findByName("assembleMockReleaseTestApk")
        assertThat(assembleMockReleaseTestApk).isNull()

        val assembleProdReleaseTestApk = appProject.tasks.findByName("assembleProdReleaseTestApk")
        assertThat(assembleProdReleaseTestApk).isNull()
        // TODO assert properties / configurations
    }

    @Test
    fun `assembler plugin registers tasks for Android Library subproject without product flavor`() {
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        libraryProject.createAndroidLibraryProject(hasProductFlavor = false)

        (libraryProject as DefaultProject).evaluate()

        val assembleDebugTestApk = libraryProject.tasks.getByName("assembleDebugTestApk")
        assertThat(assembleDebugTestApk.description).isEqualTo("Assembles app and test APKs for debug.")
        assertThat(assembleDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleDebugTestApk.dependsOnTask("assembleDebugAndroidTest")).isTrue()

        val assembleReleaseTestApk = libraryProject.tasks.findByName("assembleReleaseTestApk")
        assertThat(assembleReleaseTestApk).isNull()
        // TODO assert properties / configurations
    }

    @Test
    fun `assembler plugin registers tasks for Android Library subproject with product flavor`() {
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        libraryProject.createAndroidLibraryProject(hasProductFlavor = true)

        (libraryProject as DefaultProject).evaluate()

        val assembleMockDebugTestApk = libraryProject.tasks.getByName("assembleMockDebugTestApk")
        assertThat(assembleMockDebugTestApk.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        assertThat(assembleMockDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleMockDebugTestApk.dependsOnTask("assembleMockDebugAndroidTest")).isTrue()

        val assembleProdDebugTestApk = libraryProject.tasks.getByName("assembleProdDebugTestApk")
        assertThat(assembleProdDebugTestApk.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        assertThat(assembleProdDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleProdDebugTestApk.dependsOnTask("assembleProdDebugAndroidTest")).isTrue()

        val assembleMockReleaseTestApk = libraryProject.tasks.findByName("assembleMockReleaseTestApk")
        assertThat(assembleMockReleaseTestApk).isNull()

        val assembleProdReleaseTestApk = libraryProject.tasks.findByName("assembleProdReleaseTestApk")
        assertThat(assembleProdReleaseTestApk).isNull()
        // TODO assert properties / configurations
    }

    @Test
    fun `assembler plugin registers tasks for multi-project project`() {
        appProject.pluginManager.apply(AppPlugin::class.java)
        appProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)
        libraryProject.pluginManager.apply(LibraryPlugin::class.java)
        libraryProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        appProject.createAndroidAppProject(hasProductFlavor = true)
        libraryProject.createAndroidLibraryProject(hasProductFlavor = false)

        (rootProject as DefaultProject).evaluate()
        (appProject as DefaultProject).evaluate()
        (libraryProject as DefaultProject).evaluate()

        val assembleMockDebugTestApk = appProject.tasks.getByName("assembleMockDebugTestApk")
        assertThat(assembleMockDebugTestApk.description).isEqualTo("Assembles app and test APKs for mockDebug.")
        assertThat(assembleMockDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleMockDebugTestApk.dependsOnTask("assembleMockDebugAndroidTest")).isTrue()

        val assembleProdDebugTestApk = appProject.tasks.getByName("assembleProdDebugTestApk")
        assertThat(assembleProdDebugTestApk.description).isEqualTo("Assembles app and test APKs for prodDebug.")
        assertThat(assembleProdDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleProdDebugTestApk.dependsOnTask("assembleProdDebugAndroidTest")).isTrue()

        val assembleMockReleaseTestApk = appProject.tasks.findByName("assembleMockReleaseTestApk")
        assertThat(assembleMockReleaseTestApk).isNull()

        val assembleProdReleaseTestApk = appProject.tasks.findByName("assembleProdReleaseTestApk")
        assertThat(assembleProdReleaseTestApk).isNull()

        val assembleDebugTestApk = libraryProject.tasks.getByName("assembleDebugTestApk")
        assertThat(assembleDebugTestApk.description).isEqualTo("Assembles app and test APKs for debug.")
        assertThat(assembleDebugTestApk.group).isEqualTo(FIRESTORM_GROUP)
        assertThat(assembleDebugTestApk.dependsOnTask("assembleDebugAndroidTest")).isTrue()

        val assembleReleaseTestApk = libraryProject.tasks.findByName("assembleReleaseTestApk")
        assertThat(assembleReleaseTestApk).isNull()

        assertThat(rootProject.getTasksByName("assembleDebugTestApk", true)).hasSize(1)
        assertThat(rootProject.getTasksByName("assembleMockDebugTestApk", true)).hasSize(1)
        assertThat(rootProject.getTasksByName("assembleProdDebugTestApk", true)).hasSize(1)
        // TODO assert properties / configurations
    }

    @Test
    fun `assembler plugin cannot be applied to root project`() {
        rootProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        assertThrows(ProjectConfigurationException::class.java) {
            (rootProject as DefaultProject).evaluate()
        }
    }

    @Test
    fun `assembler plugin cannot be applied to subproject without Android App or Android Library plugin`() {
        val kotlinJvmProject = ProjectBuilder.builder().withParent(rootProject).withName("kotlin").build()
        kotlinJvmProject.pluginManager.apply("kotlin")
        kotlinJvmProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        assertThrows(ProjectConfigurationException::class.java) {
            (kotlinJvmProject as DefaultProject).evaluate()
        }
    }
}
