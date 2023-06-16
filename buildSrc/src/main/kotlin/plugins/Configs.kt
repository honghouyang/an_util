package plugins

object Apps {
    /** 主版本号. */
    const val MAJOR = 1

    /** 子版本号. */
    const val MINOR = 7

    /** 修正版本号. */
    const val PATCH = 0

    const val APPLICATION_ID = "com.hhy.util.sample"
    const val TEST_APPLICATION_ID = "com.hhy.util.sample.test"

    /** SDK 编译版本 */
    const val COMPILE_SDK = 32

    /** 最小 SDK 版本. */
    const val MIN_SDK = 19

    /** 目标 SDK 版本 */
    const val TARGET_SDK = 32
}

object MavenUrls {
    const val ALIBABA = "https://maven.aliyun.com/repository/public/"
    const val GOOGLE = "https://maven.aliyun.com/repository/google/"
    const val GRADLE = "https://maven.aliyun.com/repository/gradle-plugin/"
    const val JITPACK = "https://jitpack.io"
}

object Sonar {
    const val URL = "http://sonar.ops.hhy.com/"
    const val TOKEN = "ae78f18155d78bc6a2567c02551c851dc167606e"

    /** 项目 ID，需要去 http://sonar.ops.hhy.com 查阅. */
    const val PROJECT_KEY = "L-2"
}

object Kover {
    const val REPORT_XML_PATH = "reports/kover/xml/report.xml"
    const val REPORT_HTML_PATH = "reports/kover/html"
    val CLASSES_EXCLUDES = listOf("*.BuildConfig")
    val PROJECTS_EXCLUDES = listOf(":sample")
}

object UnitTest {
    const val REPORT_HTML_PATH = "reports/tests"
    const val TEST_RESULTS_PATH = "test-results/testDebugUnitTest"
}

object Lint {
    const val REPORT_HTML_PATH = "reports/lint-results.html"
}
