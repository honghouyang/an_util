package plugins

object Versions {
    object Kotlin {
        const val STDLIB = "1.6.21"
        const val COROUTINE = "1.6.4"
        const val SERIALIZATION = "1.3.3"
    }

    object Androidx {
        const val APPCOMPAT = "1.5.1"
        const val MULTIDEX = "2.0.1"
        const val NAVIGATION = "2.5.2"
        const val CONSTRAINT_LAYOUT = "2.1.4"
        const val STARTUP = "1.1.1"

        object Test {
            const val BASE = "1.4.0"
            const val ORCHESTRATOR = "1.4.1"
            const val JUNIT = "1.1.3"
            const val CORE_TESTING = "2.1.0"
            const val UI_AUTOMATOR = "2.2.0"
        }
    }

    object Test {
        const val MOCKK = "1.12.8"
        const val ROBOLECTRIC = "4.8.2"
    }

    object Google {
        const val MATERIAL = "1.6.1"
        const val ZXING = "3.3.3" // 3.4 以后版本只支持 API 24 以上
    }

    object Square {
        const val LEAK_CANARY = "2.9.1"
    }

    const val THREE_TEN_ABP = "1.3.1"
    const val EASY_PERMISSIONS = "3.0.0"
}

object Libraries {
    object Kotlin {
        const val STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.STDLIB}"
        const val COROUTINE =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.COROUTINE}"
        const val SERIALIZATION =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlin.SERIALIZATION}"
        const val JUNIT = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin.STDLIB}"
        const val COROUTINE_TEST =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Kotlin.COROUTINE}"
    }

    object Androidx {
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.Androidx.APPCOMPAT}"
        const val MULTIDEX = "androidx.multidex:multidex:${Versions.Androidx.MULTIDEX}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.Androidx.CONSTRAINT_LAYOUT}"
        const val NAVIGATION_UI =
            "androidx.navigation:navigation-ui-ktx:${Versions.Androidx.NAVIGATION}"
        const val NAVIGATION_FRAGMENT =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Androidx.NAVIGATION}"
        const val STARTUP =
            "androidx.startup:startup-runtime:${Versions.Androidx.STARTUP}"

        object Test {
            const val CORE = "androidx.test:core-ktx:${Versions.Androidx.Test.BASE}"
            const val RUNNER = "androidx.test:runner:${Versions.Androidx.Test.BASE}"
            const val RULES = "androidx.test:rules:${Versions.Androidx.Test.BASE}"
            const val ORCHESTRATOR =
                "androidx.test:orchestrator:${Versions.Androidx.Test.ORCHESTRATOR}"
            const val JUNIT = "androidx.test.ext:junit-ktx:${Versions.Androidx.Test.JUNIT}"
            const val CORE_TESTING =
                "androidx.arch.core:core-testing:${Versions.Androidx.Test.CORE_TESTING}"
            const val UI_AUTOMATOR =
                "androidx.test.uiautomator:uiautomator:${Versions.Androidx.Test.UI_AUTOMATOR}"
        }
    }

    object Test {
        const val MOCKK = "io.mockk:mockk:${Versions.Test.MOCKK}"
        const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.Test.ROBOLECTRIC}"
    }

    object Google {
        const val MATERIAL = "com.google.android.material:material:${Versions.Google.MATERIAL}"
        const val ZXING = "com.google.zxing:core:${Versions.Google.ZXING}"
    }

    object Square {
        const val LEAK_CANARY =
            "com.squareup.leakcanary:leakcanary-android:${Versions.Square.LEAK_CANARY}"
    }

    const val THREE_TEN_ABP = "com.jakewharton.threetenabp:threetenabp:${Versions.THREE_TEN_ABP}"
    const val EASY_PERMISSIONS = "pub.devrel:easypermissions:${Versions.EASY_PERMISSIONS}"
}
