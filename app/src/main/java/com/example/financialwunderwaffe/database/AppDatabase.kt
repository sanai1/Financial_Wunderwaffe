package com.example.financialwunderwaffe.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        UserInfo::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserInfoDao(): UserInfoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database.db"
                ).build()
                INSTANCE = instance
                instance
            }

    }

}