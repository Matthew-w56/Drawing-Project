package com.example.drawingapp

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file builds the Room Database and Repository of the Drawing App.
 */

class DrawingAppApplication : Application() {
    private val scope = CoroutineScope((SupervisorJob()))

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            DrawingAppDatabase::class.java,
            "drawingapp_database"
        ).build()
    }
    val drawingAppRepository by lazy { DrawingAppRepository(scope, db.drawingAppDao()) }
}