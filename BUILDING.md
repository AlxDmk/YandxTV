# Сборка YandxTV APK

Полное руководство по сборке release APK и установке на Android TV / Яндекс ТВ.

## Требования

| Инструмент | Версия |
|---|---|
| Android Studio | Ladybug (2024.2+) |
| JDK | 17+ |
| Android SDK | API 35 (compileSdk) |
| Gradle | 8.9 (через wrapper) |

## 1. Клонирование репозитория

```bash
git clone https://github.com/AlxDmk/YandxTV.git
cd YandxTV
```

## 2. Настройка local.properties

```bash
cp local.properties.example local.properties
# Отредактируйте local.properties — укажите путь к Android SDK
```

## 3. Сборка debug APK (для тестирования)

```bash
./gradlew assembleDebug
```

APK будет в: `app/build/outputs/apk/debug/app-debug.apk`

## 4. Сборка release APK

### 4.1 Создание keystore (один раз)

```bash
keytool -genkeypair -v \
  -keystore yandxtv-release.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias yandxtv
```

Сохраните `yandxtv-release.jks` в безопасном месте. **Не коммитьте в git!**

### 4.2 Конфигурация подписи в local.properties

Добавьте в `local.properties`:

```properties
KEYSTORE_PATH=/absolute/path/to/yandxtv-release.jks
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=yandxtv
KEY_PASSWORD=your_key_password
```

### 4.3 Обновите app/build.gradle.kts — добавьте signingConfigs

```kotlin
import java.util.Properties
val localProps = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(localProps["KEYSTORE_PATH"] as String)
            storePassword = localProps["KEYSTORE_PASSWORD"] as String
            keyAlias = localProps["KEY_ALIAS"] as String
            keyPassword = localProps["KEY_PASSWORD"] as String
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 4.4 Сборка release

```bash
./gradlew assembleRelease
```

APK будет в: `app/build/outputs/apk/release/app-release.apk`

## 5. Установка через флешку

1. Скопируйте `app-release.apk` на USB-флешку (FAT32)
2. Вставьте флешку в ТВ
3. Установите файловый менеджер (например, [ES File Explorer](https://play.google.com/store/apps/details?id=com.estrongs.android.pop) или X-plore)
4. Откройте APK через файловый менеджер
5. Разрешите установку из неизвестных источников в настройках ТВ:
   - **Яндекс ТВ**: Настройки → Безопасность → Неизвестные источники
   - **Android TV**: Настройки → Безопасность и ограничения
6. Нажмите «Установить»
7. Приложение появится в лаунчере ТВ

## 6. Установка через ADB (для разработчиков)

```bash
# Включите ADB debugging на ТВ: Настройки → О системе → Нажмите 7 раз на сборку
adb connect <TV_IP_ADDRESS>:5555
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Минимальные требования к устройству

- Android 5.1 (API 22) и выше
- Поддерживается: Android TV, Яндекс ТВ, Google TV, Amazon Fire TV
- Рекомендуется: пульт с D-pad и кнопкой Back

## Размер APK

Ожидаемый размер release APK: **~8–12 МБ** (зависит от Compose runtime)
