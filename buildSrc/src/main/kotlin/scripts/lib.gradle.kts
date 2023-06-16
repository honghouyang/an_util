import plugins.Apps
import plugins.Artifacts
import plugins.LIB_VERSION_NAME
import plugins.Lint
import plugins.androidTestDependencies
import plugins.kotlinDependencies
import plugins.unitTestDependencies
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
}

sonarqube {
    properties {
        // 源码目录，工程中必须存在该目录
        property("sonar.sources", "src/main/kotlin")
        // 测试目录，工程中必须存在该目录
        property("sonar.tests", "src/test/kotlin, src/androidTest/kotlin")
    }
}

android {
    compileSdk = Apps.COMPILE_SDK

    defaultConfig {
        minSdk = Apps.MIN_SDK
        targetSdk = Apps.TARGET_SDK
        consumerProguardFiles("consumer-rules.pro")
        multiDexEnabled = true // 解决 API 21 以下系统 android test 方法数超出 64K 限制问题

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf(
            "clearPackageData" to "true",
            "disableAnalytics" to "true",
        )
    }

    lint {
        checkDependencies = true
        abortOnError = false
        htmlOutput = file("${rootProject.buildDir}/${Lint.REPORT_HTML_PATH}")
    }

    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }

    sourceSets {
        getByName("main") {
            // 主要代码目录
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            // 单元测试目录
            java.srcDir("src/test/kotlin")
        }
        getByName("androidTest") {
            // 仪表测试目录
            java.srcDir("src/androidTest/kotlin")
        }
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true // 开启单元测试覆盖率统计
            enableAndroidTestCoverage = true // 开启仪表测试覆盖率统计
            multiDexKeepProguard =
                file("proguard-multidex-rules.pro") // 解决 API 21 以下系统 android test 方法数超出 64K 限制问题
        }
        create("snapshot") {
            enableUnitTestCoverage = false // 关闭单元测试覆盖率统计
            enableAndroidTestCoverage = false // 关闭仪表测试覆盖率统计
        }
        getByName("release") {
            enableUnitTestCoverage = false // 关闭单元测试覆盖率统计
            enableAndroidTestCoverage = false // 关闭仪表测试覆盖率统计
        }
    }

    compileOptions {
        encoding = "UTF-8"
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    libraryVariants.all {
        outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName =
                    "${Artifacts.ARTIFACT_ID}-${buildType.name}-${LIB_VERSION_NAME}.aar"
            }
    }

    testBuildType = "debug"
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // test
    unitTestDependencies()

    // android test
    androidTestDependencies()

    // main
    // kotlin
    kotlinDependencies()
}

val runTasks: MutableList<String> = gradle.startParameter.taskNames

tasks.register<Jar>("androidSourcesJar") {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("releaseAar") {
                // Applies the component for the release build variant.
                groupId = Artifacts.GROUP_ID
                artifactId = Artifacts.ARTIFACT_ID
                version = LIB_VERSION_NAME

                artifact(tasks["bundleReleaseAar"])
                artifact(tasks["androidSourcesJar"])
            }
            // Creates a Maven publication called “snapshot”.
            create<MavenPublication>("snapshotAar") {
                // Applies the component for the snapshot build variant.
                groupId = Artifacts.GROUP_ID
                artifactId = Artifacts.ARTIFACT_ID
                version = "$LIB_VERSION_NAME-SNAPSHOT"

                artifact(tasks["bundleReleaseAar"])
                artifact(tasks["androidSourcesJar"])
            }
        }
    }
}