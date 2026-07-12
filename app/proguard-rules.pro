# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# YandxTV models
-keep class com.alxdmk.yandxtv.domain.model.** { *; }
-keep class com.alxdmk.yandxtv.util.CatalogExporter$** { *; }

# WebView
-keepclassmembers class * extends android.webkit.WebViewClient {
    public *;
}

# Security Crypto
-keep class androidx.security.crypto.** { *; }
