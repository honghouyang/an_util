import plugins.Apps
import plugins.LIB_VERSION_NAME
import plugins.Libraries
import plugins.Lint
import plugins.VERSION_CODE
import plugins.androidTestDependencies
import plugins.androidXDependencies
import plugins.kotlinDependencies
import plugins.unitTestDependencies
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlinx.kover")
    id("org.sonarqube")
}

sonarqube {
    properties {
        isSkipProject = true // 不进行代码质量扫描
    }
}

android {
    compileSdk = Apps.COMPILE_SDK

    defaultConfig {
        applicationId = Apps.APPLICATION_ID
        minSdk = Apps.MIN_SDK
        targetSdk = Apps.TARGET_SDK
        versionCode = VERSION_CODE
        versionName = LIB_VERSION_NAME
        multiDexEnabled = true

        testApplicationId = Apps.TEST_APPLICATION_ID
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

    signingConfigs {
        val keyProps = Properties()
        getByName("debug") {
            keyProps.load(FileInputStream(rootProject.file("signature/debug-keystore.properties")))
            storeFile = file(keyProps.getProperty("storeFile"))
            storePassword = keyProps.getProperty("storePass")
            keyAlias = keyProps.getProperty("keyAlias")
            keyPassword = keyProps.getProperty("keyPass")
        }

        create("release") {
            keyProps.load(FileInputStream(rootProject.file("signature/release-keystore.properties")))
            storeFile = file(keyProps.getProperty("storeFile"))
            storePassword = keyProps.getProperty("storePass")
            keyAlias = keyProps.getProperty("keyAlias")
            keyPassword = keyProps.getProperty("keyPass")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug" // applicationId 后缀 ".debug"，用于区分测试包
            versionNameSuffix = "-debug" // 版本名后缀 "-debug"，用于区分测试包
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false // 关闭代码缩减、混淆、优化功能
            isShrinkResources = false // 关闭资源缩减功能
            enableUnitTestCoverage = true // 开启测试覆盖率统计
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        create("snapshot") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false // 关闭代码缩减、混淆、优化功能
            isShrinkResources = false // 关闭资源缩减功能
            enableUnitTestCoverage = false // 开启测试覆盖率统计
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true // 开启代码缩减、混淆、优化功能
            isShrinkResources = true // 开启资源缩减功能
            enableUnitTestCoverage = false // 关闭测试覆盖率统计
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        resources.excludes += listOf(
            "**/*.kotlin_module",
            "**/*.version",
            "**/kotlin/**",
            "**/*.txt",
            "**/*.xml",
            "**/*.properties",
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/NOTICE",
            "META-INF/LGPL2.1",
            "META-INF/AL2.0",
            "DebugProbesKt.bin",
        )
    }

    compileOptions {
        encoding = "UTF-8"
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    applicationVariants.all {
        outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName =
                    "${rootProject.name}-${buildType.name}-${LIB_VERSION_NAME}.apk"
            }
    }

    testBuildType = "debug"
    testOptions {
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

    // androidx
    androidXDependencies()

    // google
    implementation(Libraries.Google.MATERIAL)

    // square
    debugImplementation(Libraries.Square.LEAK_CANARY)
}
