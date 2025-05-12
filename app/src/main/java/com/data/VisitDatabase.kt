package com.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.model.VetVisit

@Database(entities = [VetVisit::class], version = 1, exportSchema = false)
abstract class VisitDatabase : RoomDatabase() {

    // Abstract function to get the DAO
    abstract fun visitDao(): VisitDao

    companion object {
        @Volatile
        private var INSTANCE: VisitDatabase? = null

        // Singleton pattern to get the database instance
        fun getDatabase(context: Context): VisitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VisitDatabase::class.java,
                    "visit_database"
                )
                    .fallbackToDestructiveMigration() // In case of database version conflict
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
