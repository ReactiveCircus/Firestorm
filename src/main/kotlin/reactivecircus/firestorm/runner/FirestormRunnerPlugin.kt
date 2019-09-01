package reactivecircus.firestorm.runner

import org.gradle.api.Plugin
import org.gradle.api.Project
import reactivecircus.firestorm.hasConfiguredTask
import reactivecircus.firestorm.isRoot

// TODO look at AndroidXPlugin
// TODO document
class FirestormRunnerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            require(project.isRoot) {
                "Please apply Firestorm Runner plugin to the rootProject."
            }

            if (!project.rootProject.hasConfiguredTask<RunAvailableTestsOnFirebaseTestLabTask>()) {
                project.rootProject.tasks.register(
                    "runAvailableTestsOnFirebaseTestLab",
                    RunAvailableTestsOnFirebaseTestLabTask::class.java
                ) { task ->
                    // TODO configure
                }
            }
        }
    }
}
