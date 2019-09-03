package reactivecircus.firestorm.assembler

import com.android.build.gradle.api.TestVariant
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import reactivecircus.firestorm.assembler.incremental.IncrementalAssemblerConfigs
import reactivecircus.firestorm.hasAndroidAppPlugin

@CacheableTask
open class AssembleTestApk : DefaultTask() {

    @Internal
    val isAppProject = project.hasAndroidAppPlugin

    @Internal
    val testVariant: Property<TestVariant> = project.objects.property(TestVariant::class.java)

    @Input
    val incremental: Property<Boolean> = project.objects.property(Boolean::class.java)

    @Nested
    val incrementalAssemblerConfigs: IncrementalAssemblerConfigs = IncrementalAssemblerConfigs()

    fun configureIncrementalAssembler(configure: IncrementalAssemblerConfigs.() -> Unit) =
        configure(incrementalAssemblerConfigs)

    @TaskAction
    fun assemble() {
        // TODO inject dummyApk from resource for library project
        // TODO inject testVariant to get standard app and test apk output paths
    }
}
