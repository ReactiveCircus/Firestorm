package reactivecircus.firestorm

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.TestVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import reactivecircus.firestorm.tasks.AssembleApkPair
import reactivecircus.firestorm.tasks.AnalyzeGitChanges
import reactivecircus.firestorm.tasks.GenerateDummyApk
import reactivecircus.firestorm.tasks.RunTestsOnFirebaseTestLab
import java.io.File

/**
 * A plugin for assembling app and test APKs for an Android Application or Android Library subproject,
 * and running instrumented tests on Firebase Test Lab.
 * This plugin should be applied directly to Android Application or Android Library subprojects
 * with instrumented tests to be run on Firebase Test Lab.
 */
class FirestormPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("firestorm", FirestormExtension::class.java)
        project.afterEvaluate {
            require(!project.isRoot) {
                "Please apply Firestorm plugin directly to Android Application or Library subproject(s)."
            }

            val isAndroidAppProject = project.hasAndroidAppPlugin
            val isAndroidLibraryProject = project.hasAndroidLibraryPlugin

            require(isAndroidAppProject || isAndroidLibraryProject) {
                "Firestorm plugin is designed to work with Android projects but ${project.displayName} doesn't have either 'com.android.library' or 'com.android.application' plugin applied."
            }

            if (extension.enabled) {
                when {
                    isAndroidAppProject -> registerTasks(project, extension, project.appExtension.testVariants)
                    isAndroidLibraryProject -> registerTasks(project, extension, project.libraryExtension.testVariants)
                }
            } else {
                project.logger.info("Firestorm plugin is disabled.")
            }
        }
    }

    private fun registerTasks(
        project: Project,
        extension: FirestormExtension,
        testVariants: DomainObjectSet<TestVariant>
    ) {
        // register AnalyzeGitChanges task if smartTrigger is enabled
        val analyzeGitChanges = takeIf { extension.smartTrigger }
            ?.registerAnalyzeGitChangesTask(project, extension)

        // register GenerateDummyApk task if project is an Android Library project
        val generateDummyApk = takeIf { project.hasAndroidLibraryPlugin }
            ?.registerGenerateDummyApkTask(project)

        testVariants.all { testVariant ->
            val testedVariantName = testVariant.testedVariant.name

            // Use APK generated by packageBuildVariant task for Android Application project,
            // or use dummy apk for Android Library project.
            val appApkProvider = (testVariant.testedVariant as? ApplicationVariant)?.packageApplicationProvider
                ?.flatMap {
                    it.outputDirectory.file(it.apkNames.first())
                } ?: generateDummyApk!!.flatMap { it.dummyApk }

            val testApkProvider = testVariant.packageApplicationProvider
                .flatMap {
                    it.outputDirectory.file(it.apkNames.first())
                }

            val assembleApkPair = project.tasks.register(
                "assemble${testedVariantName.capitalize()}ApkPair", AssembleApkPair::class.java
            ) { task ->
                task.group = FIRESTORM_TASK_GROUP
                task.description = "Assembles app and test APKs for $testedVariantName."
                task.inputAppApk.set(appApkProvider)
                task.inputTestApk.set(testApkProvider)
                if (extension.smartTrigger) {
                    task.shouldSkip.set(analyzeGitChanges!!.flatMap { it.sourceChanged() })
                }
                task.outputAppApk.set(
                    File(
                        project.rootProject.buildDir,
                        "$FIRESTORM_TASK_OUTPUT_DIR/${project.name}/$testedVariantName/$APP_APK_FILE"
                    )
                )
                task.outputTestApk.set(
                    File(
                        project.rootProject.buildDir,
                        "$FIRESTORM_TASK_OUTPUT_DIR/${project.name}/$testedVariantName/$TEST_APK_FILE"
                    )
                )
            }

            project.tasks.register(
                "run${testedVariantName.capitalize()}TestsOnFirebaseTestLab", RunTestsOnFirebaseTestLab::class.java
            ) { task ->
                task.group = FIRESTORM_TASK_GROUP
                task.description = "Runs tests for $testedVariantName on Firebase Test Lab."
                task.appApk.set(assembleApkPair.flatMap { it.outputAppApk })
                task.testApk.set(assembleApkPair.flatMap { it.outputTestApk })
            }
        }
    }

    private fun registerAnalyzeGitChangesTask(
        project: Project,
        extension: FirestormExtension
    ): TaskProvider<AnalyzeGitChanges> {
        return project.tasks.register(
            "analyzeGitChanges", AnalyzeGitChanges::class.java
        ) {
            it.group = FIRESTORM_TASK_GROUP
            it.description = "Checks if project source has meaningful git changes."
            it.includes.set(extension.smartTriggerConfigs.includes)
            it.excludes.set(extension.smartTriggerConfigs.excludes)
            it.checkDependencies.set(extension.smartTriggerConfigs.checkDependencies)
            it.result.set(File(project.buildDir, "$FIRESTORM_TASK_OUTPUT_DIR/$GIT_CHANGES_ANALYSIS_RESULT_FILE"))
        }
    }

    private fun registerGenerateDummyApkTask(project: Project): TaskProvider<GenerateDummyApk> {
        val taskName = "generateDummyApk"
        // task is registered on the root project as the output is always identical.
        return if (project.rootProject.hasConfiguredTask<GenerateDummyApk>()) {
            project.rootProject.tasks.named(taskName, GenerateDummyApk::class.java)
        } else {
            project.rootProject.tasks.register(taskName, GenerateDummyApk::class.java) {
                it.group = FIRESTORM_TASK_GROUP
                it.description = "Generates a dummy APK for running Android Library tests on Firebase Test Lab."
                it.dummyApk.set(File(project.rootProject.buildDir, "$FIRESTORM_TASK_OUTPUT_DIR/$DUMMY_APK_FILE"))
            }
        }
    }
}
