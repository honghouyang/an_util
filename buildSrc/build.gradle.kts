plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/google/")
    maven("https://maven.aliyun.com/repository/gradle-plugin/")
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation("com.android.tools.build:gradle:7.3.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.6.21")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1")
    implementation("org.jetbrains.kotlinx:kover:0.6.0")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.4.0.2513")
}
