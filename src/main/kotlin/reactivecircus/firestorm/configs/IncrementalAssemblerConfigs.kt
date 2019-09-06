package reactivecircus.firestorm.configs

import org.gradle.api.tasks.Input

/**
 * Configurations for incremental assembler.
 */
open class IncrementalAssemblerConfigs {
    /**
     * Set of include patterns to be considered when performing project source change detection.
     */
    @get:Input
    var includes: Set<String> = DEFAULT_INCLUDES

    /**
     * Set of exclude patterns to be ignored when performing project source change detection.
     */
    @get:Input
    var excludes: Set<String> = DEFAULT_EXCLUDES

    /**
     * Whether to recursively check the project's transitive dependencies when performing project source change detection.
     */
    @get:Input
    var checkDependencies: Boolean = DEFAULT_CHECK_DEPENDENCIES

    companion object {
        val DEFAULT_INCLUDES = setOf(
            "src/**/*.kt",
            "src/**/*.java",
            "src/**/*.xml",
            "src/*/assets",
            "build.gradle",
            "build.gradle.kts",
            "*.properties"
        )
        val DEFAULT_EXCLUDES = setOf(
            "src/test*"
        )
        const val DEFAULT_CHECK_DEPENDENCIES = true
    }
}
