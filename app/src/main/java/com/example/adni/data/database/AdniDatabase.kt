package com.example.adni.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adni.data.dao.CompanyDao
import com.example.adni.data.entity.*

@Database(entities = [CompanyEntity::class], version = 1)
abstract class AdniDatabase: RoomDatabase() {

    abstract fun getCompanyDao(): CompanyDao

    companion object {
        const val DB_NAME = "Adni"
    }
}