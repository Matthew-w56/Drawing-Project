package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the Room Repository for the Drawing App.
 *
 *  Phase 2:
 *
 */

class DrawingAppRepository(
    private val scope: CoroutineScope,
    private val dao: DrawingAppDAO
) {

    // val currentDrawing = dao.latestDrawing()
    val allDrawings = dao.allDrawing()

    /**
     * This function loads the drawing that is clicked
     * @param fileName - Name of file defined by user
     */
    fun loadImage(fileName: String, context: Context): Bitmap? {
        val inputStream: FileInputStream
        val bitmap: Bitmap
        try {
            inputStream = context.openFileInput(fileName)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * This function saves the most recent drawing to file and file name to database.
     * @param fileName  - Name of file defined by user
     * @param image     - Drawing created by user
     * @param context   - Android thing for saving to files.
     */
    fun saveImage(fileName: String, image: Bitmap, context: Context, isOverride: Boolean) {
        var success = true

        // Adds ".png" to the end of fileName
        val fileNameWithPNG = "$fileName.png"

        val outputStream = context.openFileOutput(fileNameWithPNG, Context.MODE_PRIVATE)
        try {
            // Use the compress method on the BitMap object to write image to outputStream
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        } catch (e: Exception) {
            success = false
            e.printStackTrace()
        } finally {
            try {
                outputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // Assuming the file actually got saved, send the filename to the DB
            if (success && !isOverride) {
                scope.launch {
                    dao.addDrawing(Drawing(fileNameWithPNG))
                }
            }
        }
    }

}