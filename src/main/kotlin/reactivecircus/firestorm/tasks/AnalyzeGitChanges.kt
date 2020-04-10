package reactivecircus.firestorm.tasks

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
 * Analyze and detect meaningful git changes in a subproject (module).
 */
@CacheableTask
abstract class AnalyzeGitChanges : DefaultTask() {

    // TODO add project-commit-hash.diff file as input? (from GenerateGitDiffs task)

    @get:Input
    abstract val includes: SetProperty<String>

    @get:Input
    abstract val excludes: SetProperty<String>

    @get:Input
    abstract val checkDependencies: Property<Boolean>

    @get:OutputFile
    abstract val result: RegularFileProperty

    /**
     * Returns a Provider<Boolean> indicating whether the project has meaningful git changes.
     */
    fun changeDetected(): Provider<Boolean> = result.map {
        it.asFile.readText().trim().equals(true.toString(), ignoreCase = true)
    }

    // TODO figure out how to make task cacheable and avoid IO / computation in configuration phase

    @TaskAction
    fun analyze() {
        result.get().asFile.writeText(true.toString())
    }
}
