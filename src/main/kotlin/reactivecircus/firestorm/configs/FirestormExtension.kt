package reactivecircus.firestorm.configs

import groovy.lang.Closure
import org.gradle.util.ConfigureUtil
import reactivecircus.firestorm.FirestormPlugin

/**
 * Extension for [FirestormPlugin].
 */
open class FirestormExtension {

    /**
     * Whether to skip assembling test APKs if the project source is unchanged
     * based on difference from the previous git commit.
     */
    var incrementalAssembler: Boolean = DEFAULT_INCREMENTAL_ASSEMBLER

    /**
     * Configurations for incremental assembler.
     * Only relevant when [incrementalAssembler] is true.
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
        const val DEFAULT_INCREMENTAL_ASSEMBLER = true
    }
}
