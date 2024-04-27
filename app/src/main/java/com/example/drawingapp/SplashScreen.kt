package com.example.drawingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentSplashScreenBinding

/**
 * @author          - Christian E. Anderson
 * @teammate        - Crosby White & Matthew Williams
 * @version         - Phase 2.5 = 05-Apr-2024; Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 * File Contents:
 *      This file defines the splash screen for the Drawing App.
 */
@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {
    private val vm: MyViewModel by activityViewModels()
    /**
     * This method constructs the view for this screen.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)

        // Switches to art gallery screen
        binding.mainScreenButton.setOnClickListener {
            Log.d("NAV", "navigating to art gallery screen")
            findNavController().navigate(R.id.action_splashScreen_to_artGalleryScreen)
        }

        // Switches to drawing canvas screen
        binding.drawScreenButton.setOnClickListener {
            Log.d("NAV", "navigating to drawing canvas screen")
            findNavController().navigate(R.id.action_splashScreen_to_drawScreen2)
        }
        return binding.root
    }
}