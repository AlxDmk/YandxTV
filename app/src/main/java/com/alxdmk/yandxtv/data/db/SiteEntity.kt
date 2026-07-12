package com.alxdmk.yandxtv.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val url: String,
    val description: String = "",
    val iconLabel: String = "",
    val colorHex: String = "#1565C0",
    val useDesktopUserAgent: Boolean = false,
    val allowAutofill: Boolean = true,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
