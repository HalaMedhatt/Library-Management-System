package com.hala.librarymanagementsystem.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [BookData::class, MemberData::class], version = 1)
@TypeConverters(Converters::class)
abstract class LibraryDB: RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        private var INSTANCE: LibraryDB? = null
        fun getDatabase(context: Context): LibraryDB {
            return INSTANCE ?: synchronized(this) {val instance = Room.databaseBuilder(
                context.applicationContext,
                LibraryDB::class.java,
                "library_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}