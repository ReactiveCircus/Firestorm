package reactivecircus.firestorm

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Project
import org.gradle.api.Task

val Project.isRoot get() = this == this.rootProject

val Project.hasAndroidAppPlugin get() = plugins.hasPlugin(AppPlugin::class.java)
val Project.hasAndroidLibraryPlugin get() = plugins.hasPlugin(LibraryPlugin::class.java)

val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)

val BaseVariant.isReleaseBuild get() = buildType.name == "release"

inline fun <reified T : Task> Project.hasConfiguredTask() = tasks.withType(T::class.java).isNotEmpty()
