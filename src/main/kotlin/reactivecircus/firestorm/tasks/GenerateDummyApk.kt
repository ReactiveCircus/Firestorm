@file:Suppress("UnstableApiUsage")

package reactivecircus.firestorm.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import reactivecircus.firestorm.DUMMY_APK_FILE

/**
 * Firebase Test Lab requires an "app" APK which doesn't make sense when running tests for an Android Library project.
 * This task generates a small dummy APK to be used as a substitute.
 */
@CacheableTask
abstract class GenerateDummyApk : DefaultTask() {

    @get:OutputFile
    abstract val dummyApk: RegularFileProperty

    @TaskAction
    fun generate() {
        dummyApk.get().asFile.writeBytes(
            javaClass.classLoader.getResource(DUMMY_APK_FILE)!!.readBytes()
        )
    }
}
