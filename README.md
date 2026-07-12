# YandxTV — TV Browser & Site Catalog

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21%20(Lollipop)-green.svg)]()
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-blue.svg)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)]()

**YandxTV** — персональный TV-браузер и лаунчер для Android TV / Яндекс ТВ.  
Создавайте каталог сайтов, открывайте их прямо на телевизоре и безопасно храните учётные данные — всё локально, без облака.

---

## ✨ Возможности

| Функция | Описание |
|---|---|
| 📋 Каталог сайтов | Сетка карточек с favicon, названием и доменом |
| 🌐 Встроенный браузер | WebView с поддержкой Desktop UA |
| 🔐 Безопасное хранение | Android Keystore + EncryptedSharedPreferences |
| 📺 TV-навигация | Полная поддержка D-Pad, Enter, Back |
| 💾 Локальная база | Room DB, всё хранится на устройстве |
| 🎨 Тёмная/светлая тема | Material 3 с TV-адаптацией |
| 📤 Экспорт каталога | JSON-экспорт (без паролей) |

---

## 📱 Минимальные требования

- Android TV / Яндекс ТВ с Android **5.0+ (API 21)**
- Рекомендуется Android 9+ (API 28) для полной поддержки EncryptedSharedPreferences
- Сеть для открытия сайтов

---

## 🏗️ Технический стек

```
Kotlin 1.9
Jetpack Compose + TV Foundation
Navigation Compose
Room 2.6
DataStore Preferences
AndroidX Security Crypto (EncryptedSharedPreferences)
Android Keystore
WebView
Coroutines + Flow
Material 3
Hilt (DI)
Gradle Kotlin DSL
```

---

## 📂 Структура проекта

```
app/
└── src/main/
    ├── java/com/alxdmk/yandxtv/
    │   ├── data/
    │   │   ├── db/           # Room — SiteDao, SiteEntity
    │   │   ├── repository/   # SiteRepository, CredentialRepository
    │   │   └── security/     # EncryptedCredentialStorage
    │   ├── domain/
    │   │   └── model/        # Site, Credential, UserSettings
    │   ├── presentation/
    │   │   ├── home/         # HomeScreen, SiteCard, HomeViewModel
    │   │   ├── viewer/       # SiteViewerScreen, ViewerViewModel
    │   │   ├── edit/         # EditSiteScreen, EditSiteViewModel
    │   │   ├── credentials/  # CredentialsScreen, CredentialsViewModel
    │   │   └── settings/     # SettingsScreen, SettingsViewModel
    │   ├── di/               # Hilt Modules
    │   └── util/             # UrlValidator, FaviconLoader, etc.
    └── res/
        ├── layout/           # activity_main.xml
        ├── values/           # strings, themes
        └── xml/              # network_security_config, backup_rules
```

---

## 🚀 Сборка Release APK

### Требования
- Android Studio Hedgehog или новее
- JDK 17+

### Шаги

```bash
# 1. Клонировать репозиторий
git clone https://github.com/AlxDmk/YandxTV.git
cd YandxTV

# 2. Создать keystore (один раз)
keytool -genkey -v -keystore yandxtv-release.jks \
  -alias yandxtv -keyalg RSA -keysize 2048 -validity 10000

# 3. Создать файл с паролями (НЕ добавлять в git!)
cat > keystore.properties << EOF
storeFile=../yandxtv-release.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=yandxtv
keyPassword=YOUR_KEY_PASSWORD
EOF

# 4. Собрать Release APK
./gradlew assembleRelease

# APK будет в: app/build/outputs/apk/release/app-release.apk
```

---

## 📺 Установка на Яндекс ТВ / Android TV через флешку

1. **Скопируйте** `app-release.apk` на USB-флешку (FAT32).
2. **Включите** «Неизвестные источники» в настройках ТВ:
   - Настройки → Безопасность → Неизвестные источники → Разрешить
3. **Вставьте** флешку в USB-порт телевизора.
4. **Откройте** файловый менеджер на ТВ (или установите ES File Explorer через ТВ-магазин).
5. **Перейдите** к APK файлу и нажмите **OK / Enter**.
6. **Подтвердите** установку.
7. **Запустите** YandxTV из меню приложений.

> ⚠️ **На Яндекс ТВ**: Настройки → Все настройки → Устройство → Безопасность и ограничения

---

## 🎯 Использование

### Добавление сайта
1. На главном экране выберите **«+ Добавить сайт»**
2. Введите название и URL
3. Опционально: включите Desktop UA, добавьте описание
4. Нажмите **Сохранить**

### Сохранение учётных данных
1. Откройте сайт через каталог
2. В меню браузера выберите **«Сохранить данные входа»**
3. Введите логин/пароль/заметку в форме
4. Данные хранятся зашифрованными на устройстве

### Автозаполнение
- Если для сайта сохранены данные, кнопка **«Заполнить»** появится в браузере
- Нажмите её — логин и пароль будут подставлены через JavaScript

---

## 🗺️ Roadmap

### v1.0 — MVP (текущая версия)
- [x] Каталог сайтов (Room DB)
- [x] WebView-браузер
- [x] Безопасное хранение credentials
- [x] TV-навигация (D-Pad)
- [x] Экспорт/импорт каталога (JSON)
- [x] Тёмная/светлая тема
- [x] Demo-каталог из 3 сайтов

### v1.1 — Улучшения UX
- [ ] Lazy favicon загрузка с кешированием
- [ ] История посещений
- [ ] Быстрый доступ (shortcuts) с главного экрана ТВ
- [ ] Улучшенный экспорт с именами

### v1.2 — Браузер
- [ ] Закладки внутри браузера
- [ ] Базовая навигация вперёд/назад
- [ ] Кастомный User-Agent редактор
- [ ] Блокировщик рекламы (hosts-based)

### v2.0 — Расширенные возможности
- [ ] Групповые категории сайтов
- [ ] Пин-код / биометрическая защита приложения
- [ ] Автозапуск конкретного сайта при старте
- [ ] Экспорт credentials (зашифрованный архив)
- [ ] Виджет на главном экране ТВ

---

## 🔒 Безопасность

- Все учётные данные хранятся в **EncryptedSharedPreferences** с Android Keystore
- Ключи шифрования никогда не покидают устройство
- Приложение не использует сеть для синхронизации данных
- WebView настроен с базовыми ограничениями безопасности
- Пароли **не** перехватываются со страниц — только ручное сохранение

---

## 📝 Лицензия

[MIT License](LICENSE) © 2024 AlxDmk
