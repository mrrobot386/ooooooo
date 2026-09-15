package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MovieEntity
import com.example.data.model.SceneEntity
import com.example.data.model.ShortEntity

@Database(
    entities = [MovieEntity::class, SceneEntity::class, ShortEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vastDao(): VastDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vast_ai_studio.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
