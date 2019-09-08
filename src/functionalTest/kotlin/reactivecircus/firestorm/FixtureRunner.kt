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
    fixtures: List<Fixture>,
    topLevelBuildScript: String = defaultTopLevelBuildScript,
    dryRun: Boolean = false
) = FixtureRunner(
    gradleRunner = defaultGradleRunner,
    rootProject = rootProject,
    fixtures = fixtures,
    topLevelBuildScript = topLevelBuildScript,
    dryRun = dryRun
)

data class Fixture(
    val path: String,
    val pluginConfigs: String? = null
) {
    val subproject: File = File(path)
}

class FixtureRunner(
    private val gradleRunner: GradleRunner,
    private val rootProject: TemporaryFolder,
    topLevelBuildScript: String,
    private val fixtures: List<Fixture>,
    private val dryRun: Boolean
) {
    init {
        fixtures.forEach {
            // copy all subprojects into the root project
            it.subproject.copyRecursively(File(rootProject.root, it.subproject.name), overwrite = true)

            // apply plugin configs if available
            it.pluginConfigs?.run {
                File(rootProject.root, "${it.subproject.name}/build.gradle").appendText("\n$this\n")
            }
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
            fixtures.forEach {
                appendText("include ':${it.subproject.name}'\n")
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
