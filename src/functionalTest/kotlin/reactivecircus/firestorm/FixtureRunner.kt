package reactivecircus.firestorm

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.Properties

val defaultTopLevelBuildScript =
    """
        allprojects {
            repositories {
                google()
            }
        }
    """.trimIndent()

val gradleRunner: GradleRunner by lazy {
    GradleRunner.create().withPluginClasspath()
}

fun GradleRunner.runWithFixtures(
    rootProject: TemporaryFolder,
    topLevelBuildScript: String,
    subprojects: List<File>,
    vararg commands: String,
    action: GradleRunner.() -> BuildResult
): BuildResult {
    // copy all subprojects into the root project
    subprojects.forEach {
        it.copyRecursively(File(rootProject.root, it.name), true)
    }

    // add build.gradle to root project
    rootProject.newFile("build.gradle").writeText(topLevelBuildScript)

    // generate local.properties with sdk.dir
    val androidHome = androidHome()
    rootProject.newFile("local.properties").writeText("sdk.dir=$androidHome\n")

    // generate settings.gradle
    rootProject.newFile("settings.gradle").run {
        subprojects.forEach {
            appendText("include ':${it.name}'\n")
        }
    }

    return withProjectDir(rootProject.root)
        .withArguments(commands.asList() + "--stacktrace")
        .action()
}

private fun androidHome(): String {
    val env = System.getenv("ANDROID_HOME")
    if (env != null) {
        return env.replace("\\", "/")
    }
    val localProp = File(File(System.getProperty("user.dir")), "local.properties")
    val result = if (localProp.exists()) {
        val prop = Properties()
        localProp.inputStream().use {
            prop.load(it)
        }
        prop.getProperty("sdk.dir")?.replace("\\", "/")
    } else null
    return checkNotNull(result) {
        "Missing 'ANDROID_HOME' environment variable or local.properties with 'sdk.dir'"
    }
}
