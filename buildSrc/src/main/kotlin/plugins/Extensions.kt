package plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/** lib 版本名采用 GNU 风格，主版本号.子版本号.修正版本号，例如 1.2.10. */
const val LIB_VERSION_NAME = "${Apps.MAJOR}.${Apps.MINOR}.${Apps.PATCH}"

/** 版本号由版本名映射，主版本号 * 10000 + 子版本号 * 100 + 修正版本号，例如 1.2.10 -> 10210. */
const val VERSION_CODE = Apps.MAJOR * 10000 + Apps.MINOR * 100 + Apps.PATCH

/** 获取当前 git 节点提交数. */
fun Project.gitCommitCount() = "git rev-list --count HEAD".runCommand(rootDir)

/** 获取当前 git 节点的哈希值. */
fun Project.gitCommitHash() = "git rev-parse --short=8 HEAD".runCommand(rootDir)

/** 获取当前 git 分支的名称. */
fun Project.gitBranchName() = "git symbolic-ref --short -q HEAD".runCommand(rootDir)

/** app 版本名采用 GNU 风格，主版本号.子版本号.修正版本号.变更版本数_版本节点hash，例如 1.2.10.1907_b7cb9608 */
fun Project.appVersionName() =
    "${Apps.MAJOR}.${Apps.MINOR}.${Apps.PATCH}.${gitCommitCount()}_${gitCommitHash()}"

/** 配置单元测试依赖. */
fun DependencyHandlerScope.unitTestDependencies() {
    "testImplementation"(Libraries.Kotlin.JUNIT)
    "testImplementation"(Libraries.Kotlin.COROUTINE_TEST)
    "testImplementation"(Libraries.Test.MOCKK)
    "testImplementation"(Libraries.Test.ROBOLECTRIC)
    "testImplementation"(Libraries.Androidx.Test.CORE)
    "testImplementation"(Libraries.Androidx.Test.RUNNER)
    "testImplementation"(Libraries.Androidx.Test.RULES)
    "testImplementation"(Libraries.Androidx.Test.JUNIT)
    "testImplementation"(Libraries.Androidx.Test.CORE_TESTING)
}

/** 配置仪表测试依赖. */
fun DependencyHandlerScope.androidTestDependencies() {
    "androidTestImplementation"(Libraries.Kotlin.JUNIT)
    "androidTestImplementation"(Libraries.Kotlin.COROUTINE_TEST)
    "androidTestImplementation"(Libraries.Androidx.Test.JUNIT)
    "androidTestImplementation"(Libraries.Androidx.Test.CORE)
    "androidTestImplementation"(Libraries.Androidx.Test.RUNNER)
    "androidTestImplementation"(Libraries.Androidx.Test.RULES)
    "androidTestImplementation"(Libraries.Androidx.Test.UI_AUTOMATOR)
    "androidTestUtil"(Libraries.Androidx.Test.ORCHESTRATOR)
}

/** 配置 kotlin 依赖. */
fun DependencyHandlerScope.kotlinDependencies() {
    "implementation"(Libraries.Kotlin.STDLIB)
    "implementation"(Libraries.Kotlin.COROUTINE)
}

/** 配置 androidx 依赖. */
fun DependencyHandlerScope.androidXDependencies() {
    "implementation"(Libraries.Androidx.APPCOMPAT)
    "implementation"(Libraries.Androidx.CONSTRAINT_LAYOUT)
    "implementation"(Libraries.Androidx.MULTIDEX)
    "implementation"(Libraries.Androidx.NAVIGATION_UI)
    "implementation"(Libraries.Androidx.NAVIGATION_FRAGMENT)
}

/** 执行 shell 脚本. */
fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
    .directory(workingDir)
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
    .start()
    .apply { waitFor(timeoutAmount, timeoutUnit) }
    .run {
        val error = errorStream.bufferedReader().readText().trim()
        if (error.isNotEmpty()) {
            throw IOException(error)
        }
        inputStream.bufferedReader().readText().trim()
    }

val Project.android: BaseExtension
    get() = extensions.findByName("android") as? BaseExtension
        ?: error("Project '$name' is not an Android module")
