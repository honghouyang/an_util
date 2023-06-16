// Top-level build file where you can add configuration options common to all sub-projects/modules.
import plugins.Kover
import plugins.LIB_VERSION_NAME
import plugins.MavenUrls
import plugins.Sonar
import plugins.UnitTest
import plugins.Versions
import plugins.gitBranchName

plugins {
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
}

buildscript {
    // Don't declare classpath dependencies here.
    // Instead, declare them as implementation dependency in `/buildSrc/build.gradle.kts`.
    // It has the same outcome on the configuration of the project with the benefit that
    // we can use type-safe kotlin scripts defined in `buildSrc` sources.
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(MavenUrls.GOOGLE)
        maven(MavenUrls.ALIBABA)
        maven(MavenUrls.GRADLE)
    }
}

koverMerged {
    enable()
    filters { // common filters for all default Kover merged tasks
        classes { // common class filter for all default Kover merged tasks
            excludes += Kover.CLASSES_EXCLUDES // class exclusion rules
        }
        projects { // common projects filter for all default Kover merged tasks
            excludes += Kover.PROJECTS_EXCLUDES // Specifies the projects excluded from the merged tasks
        }
    }

    xmlReport {
        reportFile.set(layout.buildDirectory.file("$buildDir/${Kover.REPORT_XML_PATH}")) // change report file name
    }

    htmlReport {
        reportDir.set(layout.buildDirectory.dir("$buildDir/${Kover.REPORT_HTML_PATH}")) // change report directory
    }
}

sonarqube {
    androidVariant = "debug"

    properties {
        property("sonar.host.url", Sonar.URL) // 服务器地址
        property("sonar.login", Sonar.TOKEN) // 登录秘钥

        property("sonar.language", "kotlin") // 语言
        property("sonar.sourceEncoding", "UTF-8") // 编码类型

        property("sonar.projectKey", Sonar.PROJECT_KEY) // 项目 ID
        property("sonar.projectName", rootProject.name) // 项目名
        property("sonar.projectVersion", LIB_VERSION_NAME) // 项目版本号
        property("sonar.branch.name", rootProject.gitBranchName())

        property("sonar.java.coveragePlugin", "kover") // 代码覆盖率插件
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "$buildDir/${Kover.REPORT_XML_PATH}"
        ) // 代码覆盖率报告地址
    }
}

val unitTestReport = tasks.register<TestReport>("testReport") {
    destinationDir = file("$buildDir/${UnitTest.REPORT_HTML_PATH}")
    // Include the results from the `test` task in all subprojects
    reportOn(
        subprojects.mapNotNull {
            it.tasks.findByPath("testDebugUnitTest")
        }
    )
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            // Force all Kotlin stdlib artifacts to use the same version.
            if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                useVersion(Versions.Kotlin.STDLIB)
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        }
    }

    tasks.withType<Test> {
        finalizedBy(unitTestReport)
        reports {
            ignoreFailures = true
        }
    }

    sonarqube {
        properties {
            property(
                "sonar.junit.reportPaths",
                "$buildDir/${UnitTest.TEST_RESULTS_PATH}"
            ) // 单元测试结果地址
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
