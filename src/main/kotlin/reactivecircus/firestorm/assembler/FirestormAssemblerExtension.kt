package reactivecircus.firestorm.assembler

import groovy.lang.Closure
import org.gradle.util.ConfigureUtil
import reactivecircus.firestorm.assembler.incremental.IncrementalAssemblerConfigs

/**
 * Extension for [FirestormAssemblerPlugin].
 */
open class FirestormAssemblerExtension {

    /**
     * Whether to skip assembling test APKs if the project source is unchanged
     * based on difference from the previous git commit.
     */
    var incremental = DEFAULT_INCREMENTAL

    /**
     * Configurations for incremental model.
     * Only relevant when [incremental] is true.
     */
    val incrementalAssemblerConfigs = IncrementalAssemblerConfigs()

    @Suppress("unused")
    fun incrementalAssembler(configure: IncrementalAssemblerConfigs.() -> Unit) {
        configure(incrementalAssemblerConfigs)
    }

    @Suppress("unused")
    fun incrementalAssembler(closure: Closure<IncrementalAssemblerConfigs>): IncrementalAssemblerConfigs =
        ConfigureUtil.configure(closure, incrementalAssemblerConfigs)

    companion object {
        const val DEFAULT_INCREMENTAL = true
    }
}
