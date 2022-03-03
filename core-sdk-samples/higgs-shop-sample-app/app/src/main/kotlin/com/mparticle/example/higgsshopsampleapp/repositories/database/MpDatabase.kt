package com.mparticle.example.higgsshopsampleapp.repositories.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mparticle.example.higgsshopsampleapp.repositories.database.daos.CartDao
import com.mparticle.example.higgsshopsampleapp.repositories.database.entities.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class MpDatabase : RoomDatabase() {
    abstract fun mpDao(): CartDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MpDatabase? = null

        fun getDatabase(context: Context): MpDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MpDatabase::class.java,
                    "dbMp"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}