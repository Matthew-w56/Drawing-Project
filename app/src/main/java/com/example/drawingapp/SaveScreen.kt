package com.example.drawingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentSaveScreenBinding

/**
 * @author          - Christian E. Anderson
 * @teammate(s)     - Crosby White & Matthew Williams
 * @version         - Phase 3 = 19-APR-2024; Phase 2 = 22-MAR-2024; Phase 1 = 16-FEB-2024
 *
 *      This file defines the saving screen for the Drawing App.
 *
 *  Phase 3: Add firebase authentication and image cloud storage.
 *
 */
class SaveScreen : Fragment() {

    private val vm: MyViewModel by activityViewModels()
    private var currentFileNameInput: String = ""

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSaveScreenBinding.inflate(layoutInflater, container, false)

        // Hide/clear output label
        binding.errorOutputLabel.text = ""

        // Fill out image preview
        binding.imagePreview.setImageBitmap(vm.bitmap.value)

        // Get any changes to filename
        binding.fileNameBox.addTextChangedListener(object : TextWatcher {
            // Unused, but we had to implement them
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                currentFileNameInput = s.toString()
            }
        })

        if (vm.currentFileName.isNotEmpty()) {
            binding.fileNameBox.text.append(vm.currentFileName)
        }

        // Switch to draw screen handler
        binding.drawScreenButton.setOnClickListener {
            Log.d("NAV", "navigating back to draw screen - no save")
            findNavController().navigate(R.id.action_saveScreen2_to_drawScreen2)
        }

        // Actually try to save their image
        binding.saveButton.setOnClickListener {
            if (currentFileNameInput.isEmpty() || currentFileNameInput == "File Name") {
                binding.errorOutputLabel.text = "Please enter a file name!"
            } else if (currentFileNameInput.contains(' ')) {
                binding.errorOutputLabel.text = "Invalid file name!  No spaces in name please."
            } else {

                // Make sure filename is unique
                var taken = false
                if (vm.allDrawings.value != null && vm.currentFileName.isEmpty()) {
                    for (drawing in vm.allDrawings.value!!) {
                        if (drawing.fileName == currentFileNameInput) {
                            binding.errorOutputLabel.text = "File name already taken!"
                            taken = true
                        }
                    }
                }

                if (!taken) {
                    // Actually go save now that we know we're good

                    val isOverride =
                        (vm.currentFileName.isNotEmpty() && vm.currentFileName == currentFileNameInput)
                    vm.currentFileName = currentFileNameInput
                    Log.d("SAVE", "Saving file under name: $currentFileNameInput")
                    vm.saveImage(currentFileNameInput, context, isOverride)
                    Log.d("SAVE", "Done saving file ($currentFileNameInput).  Navigating to draw")
                    Toast.makeText(
                        requireContext(),
                        "Drawing saved to gallery!",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_saveScreen2_to_drawScreen2)
                }
            }
        }
        binding.cloudStorageButton.setOnClickListener {
            Log.d("CLOUD", "Switching the cloud saving screen")
            vm.currentFileName = currentFileNameInput
            if (vm.currentFileName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter a file name!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(R.id.action_saveScreen2_to_cloud_saving_screen)
            }
        }
        return binding.root
    }
}