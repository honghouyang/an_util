import plugins.Libraries

plugins {
    id("sample")
}

android {
    namespace = "com.hhy.util.sample"
}

dependencies {
    // main
    implementation(project(":library"))
    implementation(Libraries.Androidx.STARTUP)
    implementation(Libraries.THREE_TEN_ABP)
    implementation(Libraries.EASY_PERMISSIONS)
}
