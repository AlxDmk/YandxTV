# YandxTV ProGuard rules

# Keep Hilt
-keepattributes *Annotation*
-dontwarn dagger.hilt.**

# Keep Room entities
-keep class com.alxdmk.yandxtv.data.db.** { *; }

# Keep domain models (used with Gson)
-keep class com.alxdmk.yandxtv.domain.model.** { *; }
-keep class com.alxdmk.yandxtv.util.CatalogExporter.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
