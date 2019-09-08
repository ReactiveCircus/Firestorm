package reactivecircus.firestorm.configs

/**
 * Configurations for smart trigger.
 */
open class SmartTriggerConfigs {
    /**
     * Set of include patterns to be considered when performing project source git changes detection.
     */
    var includes: Set<String> = DEFAULT_INCLUDES

    /**
     * Set of exclude patterns to be ignored when performing project source git changes detection.
     */
    var excludes: Set<String> = DEFAULT_EXCLUDES

    /**
     * Whether to recursively check the project's transitive dependencies when performing project source git changes detection.
     */
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
