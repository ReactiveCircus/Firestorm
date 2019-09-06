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
        tasks.register("clean", Delete) {
            delete rootProject.buildDir
        }
    """.trimIndent()

private val defaultGradleRunner: GradleRunner by lazy {
    GradleRunner.create().withPluginClasspath()
}

fun withFixtureRunner(
    rootProject: TemporaryFolder,
    subprojects: List<File>,
    topLevelBuildScript: String = defaultTopLevelBuildScript,
    dryRun: Boolean = false
) = FixtureRunner(
    gradleRunner = defaultGradleRunner,
    rootProject = rootProject,
    subprojects = subprojects,
    topLevelBuildScript = topLevelBuildScript,
    dryRun = dryRun
)

class FixtureRunner(
    private val gradleRunner: GradleRunner,
    private val rootProject: TemporaryFolder,
    topLevelBuildScript: String,
    private val subprojects: List<File>,
    private val dryRun: Boolean
) {
    init {
        // copy all subprojects into the root project
        subprojects.forEach {
            it.copyRecursively(File(rootProject.root, it.name), overwrite = true)
        }

        // add build.gradle to root project
        rootProject.newFile("build.gradle").writeText(topLevelBuildScript)

        // generate local.properties with sdk.dir
        val androidHome = androidHome()
        rootProject.newFile("local.properties").writeText("sdk.dir=$androidHome\n")

        // generate settings.gradle
        rootProject.newFile("settings.gradle").run {
            val localBuildCacheDirectory = rootProject.newFolder("local-cache")
            appendText(
                """
                buildCache {
                    local {
                        directory '${localBuildCacheDirectory.toURI()}'
                    }
                }
                """.trimIndent()
            )
            appendText("\n")
            subprojects.forEach {
                appendText("include ':${it.name}'\n")
            }
        }
    }

    fun runAndCheckResult(vararg commands: String, action: BuildResult.() -> Unit) {
        val buildResult = gradleRunner.withProjectDir(rootProject.root)
            .withArguments(buildArguments(commands.toList()))
            .build()
        action(buildResult)
    }

    fun runAndExpectFailure(vararg commands: String, action: BuildResult.() -> Unit) {
        val buildResult = gradleRunner.withProjectDir(rootProject.root)
            .withArguments(buildArguments(commands.toList()))
            .buildAndFail()
        action(buildResult)
    }

    private fun buildArguments(commands: List<String>): List<String> {
        val args = mutableListOf("--stacktrace", "--info")
        if (dryRun) {
            args.add("--dry-run")
        }
        args.addAll(commands)
        return args
    }
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
