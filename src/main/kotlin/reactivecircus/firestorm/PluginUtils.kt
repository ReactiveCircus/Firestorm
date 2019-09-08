package reactivecircus.firestorm

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import org.gradle.api.Task

internal val Project.isRoot get() = this == this.rootProject

internal val Project.hasAndroidAppPlugin get() = plugins.hasPlugin(AppPlugin::class.java)
internal val Project.hasAndroidLibraryPlugin get() = plugins.hasPlugin(LibraryPlugin::class.java)

internal val Project.appExtension: AppExtension get() = extensions.getByType(AppExtension::class.java)
internal val Project.libraryExtension: LibraryExtension get() = extensions.getByType(LibraryExtension::class.java)

internal inline fun <reified T : Task> Project.hasConfiguredTask() = tasks.withType(T::class.java).isNotEmpty()
