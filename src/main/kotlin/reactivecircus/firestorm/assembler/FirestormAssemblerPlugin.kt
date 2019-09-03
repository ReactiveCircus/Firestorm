package reactivecircus.firestorm.assembler

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.TestVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import reactivecircus.firestorm.appExtension
import reactivecircus.firestorm.hasAndroidAppPlugin
import reactivecircus.firestorm.hasAndroidLibraryPlugin
import reactivecircus.firestorm.isRoot
import reactivecircus.firestorm.libraryExtension

/**
 * A plugin for assembling and aggregating app and test APKs to be consumed by the [FirestormAssemblerPlugin]
 * for running multi-module instrumented tests on Firebase Test Lab.
 * The [AssembleTestApk] task is generated for each debug build variant in an Android project.
 * This plugin should be applied directly to Android Application or Android Library subprojects
 * with instrumented tests to be run on Firebase Test Lab.
 */
class FirestormAssemblerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("firestormAssembler", FirestormAssemblerExtension::class.java)
        project.afterEvaluate {
            require(!project.isRoot) {
                "Please apply Firestorm Assembler plugin directly to subproject(s)."
            }

            val isAndroidAppProject = project.hasAndroidAppPlugin
            val isAndroidLibraryProject = project.hasAndroidLibraryPlugin

            require(isAndroidAppProject || isAndroidLibraryProject) {
                "Please make sure either the 'com.android.library' or 'com.android.application' plugin is applied before the Firestorm Assembler plugin."
            }

            when {
                isAndroidAppProject -> project.appExtension.testVariants.all {
                    registerTaskForVariant(project, extension, it)
                }
                isAndroidLibraryProject -> project.libraryExtension.testVariants.all {
                    registerTaskForVariant(project, extension, it)
                }
            }
        }
    }

    private fun registerTaskForVariant(
        project: Project,
        extension: FirestormAssemblerExtension,
        testVariant: TestVariant
    ) {
        project.tasks.register(
            "assemble${testVariant.testedVariant.name.toCamelCase()}TestApk",
            AssembleTestApk::class.java
        ) { task ->
            task.group = "firestorm"
            task.description = "Assembles app and test APKs for ${testVariant.testedVariant.name}."

            // Depends on assembleBuildVariantAndroidTest.
            // Also depends on assembleBuildVariant if project is an Android App project.
            val taskDependencies: List<TaskProvider<Task>> = mutableListOf(testVariant.assembleProvider).apply {
                if (testVariant.testedVariant is ApplicationVariant) {
                    add(0, testVariant.testedVariant.assembleProvider)
                }
            }
            task.setDependsOn(taskDependencies)

            task.testVariant.set(project.provider { testVariant })
            task.incremental.set(project.provider { extension.incremental })
            task.configureIncrementalAssembler {
                includes = extension.incrementalAssemblerConfigs.includes
                excludes = extension.incrementalAssemblerConfigs.excludes
                checkDependencies = extension.incrementalAssemblerConfigs.checkDependencies
            }
        }
    }
}
