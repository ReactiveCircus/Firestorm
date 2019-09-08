package reactivecircus.firestorm

import org.gradle.api.Action
import reactivecircus.firestorm.configs.SmartTriggerConfigs

/**
 * Extension for [FirestormPlugin].
 */
open class FirestormExtension {

    /**
     * Enable or disable the Firestorm plugin.
     */
    var enabled: Boolean = DEFAULT_ENABLED

    /**
     * Whether to only trigger assembling APKs and running tests if the project source has meaningful git changes.
     */
    var smartTrigger: Boolean = DEFAULT_SMART_TRIGGER

    /**
     * Configurations for smart trigger.
     * Only relevant when [smartTrigger] is true.
     */
    val smartTriggerConfigs = SmartTriggerConfigs()

    @Suppress("unused")
    fun smartTriggerConfigs(action: Action<SmartTriggerConfigs>) {
        action.execute(smartTriggerConfigs)
    }

    companion object {
        const val DEFAULT_ENABLED = true
        const val DEFAULT_SMART_TRIGGER = true
    }
}
