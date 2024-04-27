package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the view model for the Drawing App.
 *
 *  Phase 2:
 *      The UI should be display a list of images that the user has drawn, if any.
 *      The UI should be done using Jetpack Compose.
 *      If the user clicks on a drawing in the list, it should load that drawing in the drawing canvas.
 */

enum class PenSize(val penSize: Float) {
    Small(10F), Medium(16F), Large(25F)
}

enum class PenShape {
    Circle, Oval, Square
}


/**
 *
 */
class MyViewModel(private val repository: DrawingAppRepository) : ViewModel() {

    private val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(
        createSolidBitmap()
    )
    val bitmap = _bitmap as LiveData<Bitmap>

    private var _penSize: MutableLiveData<PenSize> = MutableLiveData(PenSize.Medium)
    var penSize = _penSize as LiveData<PenSize>

    private var _penShape: MutableLiveData<PenShape> = MutableLiveData(PenShape.Circle)
    var penShape = _penShape as LiveData<PenShape>

    private var _penColor: MutableLiveData<Color> = MutableLiveData(Color.valueOf(Color.RED))
    var penColor = _penColor as LiveData<Color>

    var allDrawings: LiveData<List<Drawing>> = repository.allDrawings.asLiveData()

    var currentFileName: String = ""

    // This gets set at the start of MainActivity.kt
    var screenWidth = 0

    fun setPenSize(newSize: PenSize) {
        _penSize.value = newSize
    }

    fun setPenShape(newShape: PenShape) {
        _penShape.value = newShape
    }

    fun setPenColor(newColor: Color) {
        _penColor.value = newColor
    }

    fun setBitmapImage(newImage: Bitmap) {
        _bitmap.value = newImage
    }

    fun getImageFromFilename(fileName: String, context: Context?): Bitmap {
        return repository.loadImage(fileName, context!!)!!
    }

    fun saveImage(fileName: String, context: Context?, isOverride: Boolean) {
        repository.saveImage(fileName, _bitmap.value!!, context!!, isOverride)
    }

    private external fun blurImage(bitmap: Bitmap)
    private external fun invertImage(bitmap: Bitmap)

    fun doBlurImage() {
        _bitmap.value?.let { blurImage(it) }
    }

    fun doImageInvert() {
        _bitmap.value?.let { invertImage(it) }
    }

    private fun createSolidBitmap(): Bitmap {
        val b = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        val p = Paint()
        p.color = Color.WHITE
        c.drawRect(0F, 0F, 500F, 500F, p)
        return b
    }

}

/**
 *
 */
class DrawingAppViewModelFactory(private val repository: DrawingAppRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}