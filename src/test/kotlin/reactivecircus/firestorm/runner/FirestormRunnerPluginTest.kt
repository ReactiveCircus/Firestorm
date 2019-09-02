package reactivecircus.firestorm.runner

import com.google.common.truth.Truth.assertThat
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test
import reactivecircus.firestorm.FIRESTORM_GROUP
import reactivecircus.firestorm.assembler.FirestormAssemblerPlugin

class FirestormRunnerPluginTest {

    private val rootProject = ProjectBuilder.builder().withName("root").build()
    private val appProject = ProjectBuilder.builder().withParent(rootProject).withName("app").build()

    @Test
    fun `runner plugin registers tasks for for root project`() {
        rootProject.pluginManager.apply(FirestormRunnerPlugin::class.java)

        (rootProject as DefaultProject).evaluate()

        val runAndroidTestsOnFirebaseTestLab = rootProject.tasks.getByName("runAvailableTestsOnFirebaseTestLab")
        assertThat(runAndroidTestsOnFirebaseTestLab.description).isEqualTo("Runs all tests from all available test APKs on Firebase Test Lab.")
        assertThat(runAndroidTestsOnFirebaseTestLab.group).isEqualTo(FIRESTORM_GROUP)
        // TODO assert properties / configurations
    }

    @Test
    fun `runner plugin cannot be applied to subproject`() {
        appProject.pluginManager.apply(FirestormAssemblerPlugin::class.java)

        Assert.assertThrows(ProjectConfigurationException::class.java) {
            (appProject as DefaultProject).evaluate()
        }
    }
}
