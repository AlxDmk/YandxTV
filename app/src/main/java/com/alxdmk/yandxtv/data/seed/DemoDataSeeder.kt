package com.alxdmk.yandxtv.data.seed

import com.alxdmk.yandxtv.data.db.SiteDao
import com.alxdmk.yandxtv.data.db.SiteEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDataSeeder @Inject constructor(
    private val siteDao: SiteDao
) {
    suspend fun seedIfEmpty() {
        val existing = siteDao.getAllSites().first()
        if (existing.isNotEmpty()) return

        val preinstalledSites = listOf(

            // ── Видео и кино ──────────────────────────────────────────────────
            SiteEntity(
                title = "YouTube",
                url = "https://www.youtube.com",
                description = "Крупнейший видеохостинг",
                iconLabel = "YT",
                colorHex = "#FF0000",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 1
            ),
            SiteEntity(
                title = "Кинопоиск",
                url = "https://www.kinopoisk.ru",
                description = "Фильмы, сериалы, онлайн-кинотеатр",
                iconLabel = "КП",
                colorHex = "#F18F01",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 2
            ),
            SiteEntity(
                title = "Иви",
                url = "https://www.ivi.ru",
                description = "Онлайн-кинотеатр — фильмы и сериалы",
                iconLabel = "IVI",
                colorHex = "#00B388",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 3
            ),
            SiteEntity(
                title = "Okko",
                url = "https://okko.tv",
                description = "Онлайн-кинотеатр Okko",
                iconLabel = "OK",
                colorHex = "#6A1B9A",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 4
            ),
            SiteEntity(
                title = "Rutube",
                url = "https://rutube.ru",
                description = "Российский видеохостинг",
                iconLabel = "RT",
                colorHex = "#1A73E8",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 5
            ),
            SiteEntity(
                title = "Twitch",
                url = "https://www.twitch.tv",
                description = "Стриминговая платформа",
                iconLabel = "TW",
                colorHex = "#9146FF",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 6
            ),

            // ── Музыка ────────────────────────────────────────────────────────
            SiteEntity(
                title = "Яндекс Музыка",
                url = "https://music.yandex.ru",
                description = "Музыкальный стриминг от Яндекса",
                iconLabel = "ЯМ",
                colorHex = "#FFCC00",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 7
            ),
            SiteEntity(
                title = "VK Музыка",
                url = "https://vk.com/music",
                description = "Музыка ВКонтакте",
                iconLabel = "VK",
                colorHex = "#0077FF",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 8
            ),

            // ── Новости и информация ──────────────────────────────────────────
            SiteEntity(
                title = "Яндекс Новости",
                url = "https://news.yandex.ru",
                description = "Агрегатор новостей",
                iconLabel = "ЯН",
                colorHex = "#FF0000",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 9
            ),
            SiteEntity(
                title = "РБК",
                url = "https://www.rbc.ru",
                description = "Деловые новости и финансы",
                iconLabel = "РБК",
                colorHex = "#C62828",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 10
            ),
            SiteEntity(
                title = "Погода Яндекс",
                url = "https://weather.yandex.ru",
                description = "Прогноз погоды",
                iconLabel = "П",
                colorHex = "#039BE5",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 11
            ),

            // ── Социальные сети ───────────────────────────────────────────────
            SiteEntity(
                title = "ВКонтакте",
                url = "https://vk.com",
                description = "Социальная сеть ВКонтакте",
                iconLabel = "VK",
                colorHex = "#0077FF",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 12
            ),
            SiteEntity(
                title = "Telegram Web",
                url = "https://web.telegram.org",
                description = "Мессенджер Telegram в браузере",
                iconLabel = "TG",
                colorHex = "#2CA5E0",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 13
            ),

            // ── Спорт ─────────────────────────────────────────────────────────
            SiteEntity(
                title = "Матч ТВ",
                url = "https://matchtv.ru",
                description = "Спортивные новости и трансляции",
                iconLabel = "МТВ",
                colorHex = "#E53935",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 14
            ),
            SiteEntity(
                title = "Sports.ru",
                url = "https://www.sports.ru",
                description = "Спортивный портал",
                iconLabel = "SP",
                colorHex = "#1B5E20",
                useDesktopUserAgent = true,
                allowAutofill = false,
                sortOrder = 15
            ),

            // ── Покупки ───────────────────────────────────────────────────────
            SiteEntity(
                title = "Ozon",
                url = "https://www.ozon.ru",
                description = "Интернет-магазин Ozon",
                iconLabel = "OZ",
                colorHex = "#005BFF",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 16
            ),
            SiteEntity(
                title = "Wildberries",
                url = "https://www.wildberries.ru",
                description = "Крупнейший маркетплейс России",
                iconLabel = "WB",
                colorHex = "#CB11AB",
                useDesktopUserAgent = true,
                allowAutofill = true,
                sortOrder = 17
            )
        )

        preinstalledSites.forEach { siteDao.insertSite(it) }
    }
}
