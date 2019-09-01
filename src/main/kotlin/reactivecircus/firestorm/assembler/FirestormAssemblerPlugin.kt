package reactivecircus.firestorm.assembler

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.TestedVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import reactivecircus.firestorm.appExtension
import reactivecircus.firestorm.hasAndroidAppPlugin
import reactivecircus.firestorm.hasAndroidLibraryPlugin
import reactivecircus.firestorm.isReleaseBuild
import reactivecircus.firestorm.isRoot
import reactivecircus.firestorm.libraryExtension

// TODO look at AndroidXPlugin
// TODO document
class FirestormAssemblerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            require(!project.isRoot) {
                "Please apply Firestorm Assembler plugin directly to subproject(s)."
            }

            val isAndroidAppProject = project.hasAndroidAppPlugin
            val isAndroidLibraryProject = project.hasAndroidLibraryPlugin

            require(isAndroidAppProject || isAndroidLibraryProject) {
                "Please make sure either the 'com.android.library' or 'com.android.application' plugin is applied before the Firestorm Assembler plugin."
            }

            val registerTaskForBuildVariants: (BaseVariant) -> Unit = { variant ->
                // only support non-release builds
                if (!variant.isReleaseBuild) {
                    project.tasks.register(
                        "assemble${variant.name.toCamelCase()}TestApk", AssembleTestApkTask::class.java
                    ) { task ->
                        // TODO configure
                        task.group = "firestorm"
                        task.description = "Assembles app and test APKs for ${variant.name}."
                        task.dependsOn((variant as TestedVariant).testVariant.assembleProvider)
                    }
                }
            }

            when {
                isAndroidAppProject -> project.appExtension.applicationVariants.all { variant ->
                    registerTaskForBuildVariants(variant)
                }
                isAndroidLibraryProject -> project.libraryExtension.libraryVariants.all { variant ->
                    registerTaskForBuildVariants(variant)
                }
            }
        }
    }
}
