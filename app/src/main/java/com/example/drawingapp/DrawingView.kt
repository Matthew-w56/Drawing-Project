package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the canvas portion of the drawing screen for the Drawing App.
 *
 *  Phase 2:
 *
 */

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private lateinit var drawingBitmap: Bitmap
    var canvas = Canvas()
    private var widthChecked: Boolean = false

    // This will get overridden in the instantiateRect method
    private var destRect: Rect = Rect(0, 0, 500, 500)
    private val srcRect: Rect = Rect(0, 0, 500, 500)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(drawingBitmap, srcRect, destRect, paint)
    }

    fun setBitmap(bitmapToSet: Bitmap) {
        drawingBitmap = bitmapToSet
        canvas.setBitmap(drawingBitmap)
    }

    fun instantiateRect(screenWidth: Int) {
        destRect = Rect(0, 0, screenWidth-20, screenWidth-20)
        widthChecked = true
    }
}
