package com.alxdmk.yandxtv.di

import android.content.Context
import androidx.room.Room
import com.alxdmk.yandxtv.data.db.AppDatabase
import com.alxdmk.yandxtv.data.db.SiteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "yandxtv.db"
        ).build()
    }

    @Provides
    fun provideSiteDao(db: AppDatabase): SiteDao = db.siteDao()
}
