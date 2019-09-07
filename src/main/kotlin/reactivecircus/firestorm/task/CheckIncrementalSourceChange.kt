package reactivecircus.firestorm.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Checks if the project source has changed by comparing with the previous git commit.
 */
@CacheableTask
abstract class CheckIncrementalSourceChange : DefaultTask() {

    // TODO add git commit hash as input?

    @get:Input
    abstract val includes: SetProperty<String>

    @get:Input
    abstract val excludes: SetProperty<String>

    @get:Input
    abstract val checkDependencies: Property<Boolean>

    @get:OutputFile
    abstract val result: RegularFileProperty

    /**
     * Returns a Provider<Boolean> indicating whether the source has changed.
     */
    fun sourceChanged(): Provider<Boolean> {
        return result.map { regularFile ->
            with(regularFile.asFile) {
                if (exists()) {
                    readText().trim().equals(false.toString(), ignoreCase = true)
                } else {
                    false
                }
            }
        }
    }

    // TODO figure out how to make task cacheable and avoid IO / computation in configuration phase

    @TaskAction
    fun check() {
        result.get().asFile.writeText(true.toString())
    }
}
