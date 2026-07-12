# Keep data classes used by Room & Kotlin Serialization
-keep class com.alxdmk.yandxtv.data.db.entity.** { *; }
-keep class com.alxdmk.yandxtv.domain.model.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Room
-keep class androidx.room.** { *; }

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.alxdmk.yandxtv.**$$serializer { *; }
-keepclassmembers class com.alxdmk.yandxtv.** {
    *** Companion;
}
-keepclasseswithmembers class com.alxdmk.yandxtv.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# WebView JavaScript interface (if added in future)
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Suppress warnings for missing classes in release
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
