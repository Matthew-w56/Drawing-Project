package com.example.drawingapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentDrawScreenBinding
import com.skydoves.colorpickerview.listeners.ColorListener

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the non-canvas portion of the draw screen for the Drawing App.
 *
 *  Phase 2:
 *
 */

class DrawScreen : Fragment() {

    private val viewModel: MyViewModel by activityViewModels()
    private val binding: FragmentDrawScreenBinding by lazy {
        FragmentDrawScreenBinding.inflate(layoutInflater)
    }
    private var touchCoordinateScalar: Float = 1F

    /**
     * This function defines the look of the UI at creation.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.view.setBitmap(viewModel.bitmap.value!!)

        binding.view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                drawOnCustomView(
                    event.x / touchCoordinateScalar,
                    event.y / touchCoordinateScalar,
                    viewModel.penSize.value!!.penSize,
                    viewModel.penShape.value!!,
                    viewModel.penColor.value!!
                )
            }
            true
        }

        binding.view.instantiateRect(viewModel.screenWidth)
        takeNoteOfViewWidth()

        // Switch to main screen
        binding.mainScreenButton.setOnClickListener {
            Log.d("NAV", "navigating to art gallery screen")
            findNavController().navigate(R.id.action_drawScreen2_to_artGalleryScreen)
        }

        binding.newDrawingButton.setOnClickListener {
            Log.d("DRAW", "Setting up new drawing.  Losing all unsaved progress..")
            viewModel.currentFileName = ""
            drawOnCustomView(0f, 0f, 10000f, PenShape.Square, Color.WHITE.toColor())
            binding.view.invalidate()
        }

        // Switch to save screen
        binding.saveScreenButton.setOnClickListener {
            Log.d("NAV", "navigating to save screen")
            findNavController().navigate(R.id.action_drawScreen2_to_saveScreen2)
        }

        // Listeners for pen size buttons
        binding.smallBrushButton.setOnClickListener { viewModel.setPenSize(PenSize.Small) }
        binding.mediumBrushButton.setOnClickListener { viewModel.setPenSize(PenSize.Medium) }
        binding.largeBrushButton.setOnClickListener { viewModel.setPenSize(PenSize.Large) }

        // Listeners for pen shape buttons
        binding.circleShapeButton.setOnClickListener { viewModel.setPenShape(PenShape.Circle) }
        binding.ovalShapeButton.setOnClickListener {
            viewModel.setPenShape(PenShape.Oval)
        }
        binding.squareShapeButton.setOnClickListener {
            viewModel.setPenShape(PenShape.Square)
            // TEMPORARY TEST CODE - Easier than adding a test button
            // END TEMPORARY TEST CODE
        }
        binding.blurButton.setOnClickListener {
            viewModel.doBlurImage()
            binding.view.invalidate();
        }
        binding.invertButton.setOnClickListener {
            viewModel.doImageInvert()
            binding.view.invalidate();
        }

        // Set the color picker's listener and default color
        binding.colorPickerView.setInitialColor(Color.BLUE)
        binding.colorPickerView.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int, fromUser: Boolean) {
                viewModel.setPenColor(color.toColor())
            }
        })

        binding.view.instantiateRect(viewModel.screenWidth)
        takeNoteOfViewWidth()

        return binding.root
    }

    /**
     * This function implements how the various UI radio buttons on the draw screen work.
     */
    private fun drawOnCustomView(
        xIn: Float,
        yIn: Float,
        size: Float,
        shape: PenShape,
        color: Color
    ) {
        // Reassign so that we can optionally adjust in the if block below
        var x: Float = xIn
        var y: Float = yIn

        val viewCanvas = binding.view.canvas
        val viewPaint = Paint()

        viewPaint.color = color.toArgb()
        viewPaint.strokeWidth = 3F
        val halfSize: Float = size / 2f
        val ovalStretch = 2f
        when (shape) {
            PenShape.Circle -> viewCanvas.drawCircle(x, y, halfSize, viewPaint)
            PenShape.Oval -> viewCanvas.drawOval(
                RectF(
                    x - size - ovalStretch, y - halfSize + ovalStretch,
                    x + size + ovalStretch, y + halfSize - ovalStretch
                ), viewPaint
            )

            PenShape.Square -> viewCanvas.drawRect(
                x - halfSize,
                y - halfSize,
                x + halfSize,
                y + halfSize,
                viewPaint
            )
        }
        binding.view.invalidate()
    }

    /**
     *
     */
    private fun takeNoteOfViewWidth() {
        // 500f here is the hard-coded width of the internal Bitmap being used
        touchCoordinateScalar = viewModel.screenWidth / 500f
    }
}