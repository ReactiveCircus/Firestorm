package reactivecircus.firestorm.runner

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction

@CacheableTask
open class RunAvailableTestsOnFirebaseTestLab : DefaultTask() {

    init {
        group = "firestorm"
        description = "Runs all tests from all available test APKs on Firebase Test Lab."
    }

    @TaskAction
    fun run() {
        // TODO
    }
}
