#noinspection ShrinkerUnresolvedReference

# 这里的规则将会被包含进 AAR 包中
# 1. 可以存放该模块的混淆规则，防止使用方无法找到对应的方法
# 2. 可以存放影响该模块功能的第三方库的混淆规则，例如反射、序列化等

# kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class **$$serializer {
    *** INSTANCE;
}

# util
-keep class com.hhy.util.** { *; }
-keep class androidx.startup.AppInitializer
-keep class * extends androidx.startup.Initializer
