import plugins.Libraries

plugins {
    id("lib")
    id("kotlin-parcelize")
}

android {
    namespace = "com.hhy.util"

    defaultConfig {
        resourcePrefix = "util_"
    }
}

dependencies {
    // main
    implementation(Libraries.Androidx.STARTUP)
    implementation(Libraries.Androidx.APPCOMPAT)
    implementation(Libraries.THREE_TEN_ABP)
    implementation(Libraries.Google.ZXING)
    implementation(Libraries.EASY_PERMISSIONS)
}
