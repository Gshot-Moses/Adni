package com.example.adni.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adni.data.Repo
import com.example.adni.data.dao.CompanyDao
import com.example.adni.data.database.AdniDatabase
import com.example.adni.domain.IRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class Cache {

    @Singleton
    @Binds
    abstract fun provideRepo(repo: Repo): IRepo

}

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AdniDatabase {
        return Room.databaseBuilder(context, AdniDatabase::class.java,
            AdniDatabase.DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideCompanyDao(database: AdniDatabase): CompanyDao {
        return database.getCompanyDao()
    }
}