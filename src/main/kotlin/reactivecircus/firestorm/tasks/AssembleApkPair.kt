package reactivecircus.firestorm.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Copies and renames the app APK and test APK for a given project / build variant
 * into a shared output directory for Firestorm.
 *
 * rootProject/build/outputs/firestorm/subproject-name/buildVariant/app.apk
 * rootProject/build/outputs/firestorm/subproject-name/buildVariant/test.apk
 */
@CacheableTask
abstract class AssembleApkPair : DefaultTask() {

    @get:Input
    @get:Optional
    abstract val shouldRun: Property<Boolean>

    @get:InputFile
    @get:Classpath
    abstract val inputAppApk: RegularFileProperty

    @get:InputFile
    @get:Classpath
    abstract val inputTestApk: RegularFileProperty

    @get:OutputFile
    abstract val outputAppApk: RegularFileProperty

    @get:OutputFile
    abstract val outputTestApk: RegularFileProperty

    init {
        onlyIf {
            shouldRun.getOrElse(true)
        }
    }

    @TaskAction
    fun assemble() {
        val outputAppApkFile = outputAppApk.get().asFile
        inputAppApk.get().asFile.copyTo(outputAppApkFile, overwrite = true)
        logger.lifecycle("Generated app apk at ${outputAppApkFile.path}.")

        val outputTestApkFile = outputTestApk.get().asFile
        inputTestApk.get().asFile.copyTo(outputTestApkFile, overwrite = true)
        logger.lifecycle("Generated test apk at ${outputTestApkFile.path}.")
    }
}
