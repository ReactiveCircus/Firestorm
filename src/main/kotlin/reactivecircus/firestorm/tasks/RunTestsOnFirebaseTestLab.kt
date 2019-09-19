@file:Suppress("UnstableApiUsage")

package reactivecircus.firestorm.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

/**
 * TODO document.
 */
abstract class RunTestsOnFirebaseTestLab : DefaultTask() {

    @get:SkipWhenEmpty
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val appApk: RegularFileProperty

    @get:SkipWhenEmpty
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val testApk: RegularFileProperty

    init {
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun runTests() {
        // TODO
    }
}
