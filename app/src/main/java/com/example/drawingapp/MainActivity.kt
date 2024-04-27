package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the main activity for the Drawing App.
 *
 *  Phase 2:
 *
 */

class MainActivity : AppCompatActivity() {

    /**
     *  This method creates the main activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val myViewModel: MyViewModel by viewModels {
            DrawingAppViewModelFactory((application as DrawingAppApplication).drawingAppRepository)
        }

        myViewModel.bitmap.observe(this) {
            Log.e("Debug", "Bitmap changed!")
        }

        // Note down the width of the screen for later
        myViewModel.screenWidth = windowManager.currentWindowMetrics.bounds.width()

        setContentView(R.layout.activity_main)
    }

    companion object {
        init {
            System.loadLibrary("drawingapp")
        }
    }
}