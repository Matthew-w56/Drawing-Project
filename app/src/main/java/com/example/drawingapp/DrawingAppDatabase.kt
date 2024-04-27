package com.example.drawingapp

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the Room Database for the Drawing App.
 *
 *  Phase 2:
 *
 */
@Database(entities = [Drawing::class], version = 1, exportSchema = false)
abstract class DrawingAppDatabase : RoomDatabase() {
    abstract fun drawingAppDao(): DrawingAppDAO

    companion object {
        @Volatile
        private var INSTANCE: DrawingAppDatabase? = null

        fun getDatabase(context: Context): DrawingAppDatabase {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE != null) return INSTANCE!!

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawingAppDatabase::class.java,
                    "drawingapp_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}

/**
 * This interface defines the following:
 *      Can query for one or more drawings from file name.
 */
@Dao
interface DrawingAppDAO {
    @Insert
    suspend fun addDrawing(drawing: Drawing)

    @Query("SELECT * from drawings ORDER BY fileName DESC LIMIT 1")
    fun latestDrawing(): Flow<Drawing>

    @Query("SELECT * from drawings ORDER BY fileName")
    fun allDrawing(): Flow<List<Drawing>>

}