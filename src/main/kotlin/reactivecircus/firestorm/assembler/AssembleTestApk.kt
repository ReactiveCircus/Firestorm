package reactivecircus.firestorm.assembler

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction

@CacheableTask
open class AssembleTestApk : DefaultTask() {

    @TaskAction
    fun run() {
        // TODO
    }
}
