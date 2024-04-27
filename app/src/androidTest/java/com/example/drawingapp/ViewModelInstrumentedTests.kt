package com.example.drawingapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests, which will execute on an Android device.
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.drawingapp", appContext.packageName)
    }

    @Test
    fun splashScreenToCanvasTest() {
        // Start the app
        composeTestRule.setContent {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        }
        // Do assertion
        composeTestRule.onNodeWithText("New Drawing").performClick()
    }

    @Test
    fun splashScreenToArtGalleryTest() {
        // Start the app
        composeTestRule.setContent {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        }
        // Do assertion
        composeTestRule.onNodeWithTag("mainScreenButton").performClick()
    }

    fun saveImageTest() {

    }

    /*private val repository: DrawingAppRepository

    // Tests that setting the pen size works
    @Test
    fun myViewModel_SmallPenSize_ReturnsSmall() {
        val vm = MyViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    vm.setPenSize(PenSize.Small)
                    assertEquals(
                        "Small",
                        vm.penSize.value.toString()
                    )
                }
            }
        }
    }

    // Tests that changing the pen size works
    @Test
    fun myViewModel_ChangingPenSize_ReturnsLatestSize() {
        val vm = MyViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val expected = "Medium"
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    vm.setPenSize(PenSize.Small)
                    assertEquals(
                        "Small",
                        vm.penSize.value.toString()
                    )
                    vm.setPenSize(PenSize.Medium)
                    assertNotSame(
                        "Small",
                        vm.penSize.value.toString()
                    )
                    assertEquals(
                        "Medium",
                        vm.penSize.value.toString()
                    )
                }
            }
        }
    }

    // Tests that setting the pen shape works
    @Test
    fun myViewModel_CirclePenShape_ReturnsCircle() {
        val vm = MyViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    vm.setPenShape(PenShape.Circle)
                    assertEquals(
                        "Circle",
                        vm.penShape.value.toString()
                    )
                }
            }
        }
    }

    // Tests that changing the pen shape works
    @Test
    fun myViewModel_ChangingPenShape_ReturnsLatestShape() {
        val vm = MyViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    vm.setPenShape(PenShape.Circle)
                    assertEquals(
                        "Circle",
                        vm.penShape.value.toString()
                    )
                    vm.setPenShape(PenShape.Oval)
                    assertEquals(
                        "Oval",
                        vm.penShape.value.toString()
                    )
                }
            }
        }
    }*/
}