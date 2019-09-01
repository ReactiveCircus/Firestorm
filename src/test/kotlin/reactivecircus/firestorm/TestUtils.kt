package reactivecircus.firestorm

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import java.io.File

fun Project.createAndroidAppProject(hasProductFlavor: Boolean) {
    appExtension.apply {
        compileSdkVersion(29)
        if (hasProductFlavor) {
            flavorDimensions("environment")
            productFlavors {
                it.create("mock")
                it.create("prod")
            }
        }
    }

    File(projectDir, "src/main/AndroidManifest.xml").apply {
        parentFile.mkdirs()
        writeText("""<manifest package="com.foo.bar"/>""")
    }
}

fun Project.createAndroidLibraryProject(hasProductFlavor: Boolean) {
    libraryExtension.apply {
        compileSdkVersion(29)
        if (hasProductFlavor) {
            flavorDimensions("environment")
            productFlavors {
                it.create("mock")
                it.create("prod")
            }
        }
    }

    File(projectDir, "src/main/AndroidManifest.xml").apply {
        parentFile.mkdirs()
        writeText("""<manifest package="com.foo.bar"/>""")
    }
}

fun Task.dependsOnTask(taskName: String) = dependsOn.find {
    (it as TaskProvider<*>).name == taskName
} != null
