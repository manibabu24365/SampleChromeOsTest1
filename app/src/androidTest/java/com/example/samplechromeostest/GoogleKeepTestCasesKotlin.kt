package com.example.samplechromeostest

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GoogleKeepTestCasesKotlin {
    private val TIMEOUT = 10000L // 5 seconds wait time
    private val PLAY_STORE_PACKAGE = "com.android.vending"
    private val GOOGLE_KEEP_PACKAGE = "com.google.android.keep"
    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        assertNotNull(device)
    }

    @Test
    fun testInstallAndLaunchGoogleKeep() {
        try {
            // Step 1: Open Play Store and search Google Keep app
            device.executeShellCommand("am start -a android.intent.action.VIEW -d market://details?id=$GOOGLE_KEEP_PACKAGE")
            if (!device.wait(Until.hasObject(By.pkg(PLAY_STORE_PACKAGE).depth(0)), TIMEOUT)) {
                throw Exception("Play Store failed to launch.")
            }

            // Step 2: Click Install to install "Google Keep" app
            val installButton = device.wait(Until.findObject(By.text("Install")), TIMEOUT)
            Log.e("INSTALLBUTTON", installButton.toString())
            if (installButton != null) {
                installButton.click()
            }

            // Step 3: Wait for Installation
            Thread.sleep(15000) // Increase wait time for slower installations
            // Step 4: Wait for the "Open" button to appear
            var openButton = device.wait(Until.findObject(By.text("Open")), TIMEOUT)
            if (openButton == null) { // If "Play" is not found, check for "Open"
                openButton = device.wait(Until.findObject(By.text("Open")), TIMEOUT)
            }

            // Step 5: Click "Open" to launch Google Keep
            if (openButton != null) {
                Thread.sleep(2000) // Short delay before clicking to ensure UI stability
                openButton.click()
                device.waitForIdle()
            } else {
                throw Exception("Play/Open button not found after installation.")
            }

            device.wait(Until.hasObject(By.pkg(GOOGLE_KEEP_PACKAGE)), TIMEOUT)
            Thread.sleep(5000) // Allow app to load UI elements

            // Wait for the Login button and click it
            if (clickIfExistsRes(device, "android:id/button1")) {
                println("Continue clicked")
                // Wait for Who's watching account selection

                if (clickIfExistsRes(device, "com.android.permissioncontroller:id/permission_allow_button")) {
                    println("Allow clicked")

                    if (clickIfExistsRes(device, "com.google.android.keep:id/speed_dial_create_close_button")) {
                        println("Action Button clicked")

                        if (clickIfExistsRes(device, "com.google.android.keep:id/new_photo_note")) {
                            println("Image  Button clicked")
                            if (clickIfExistsRes(device, "com.google.android.keep:id/text")) {
                                println("Take Photo  Button clicked")

                                Thread.sleep(2000) // Wait for the camera app to initialize

                                // Try to find the shutter button by its resource ID
                                var shutterButton = device.wait(Until.findObject(By.res("com.android.camera:id/shutter_button")), TIMEOUT)
                                if (shutterButton != null) {
                                    shutterButton.click()
                                    println("Photo captured.")
                                } else {
                                    // If the shutter button is not found, print additional debugging info
                                    println("Shutter button not found in the camera app.")
                                    // Try searching for other possible camera button resource IDs
                                    shutterButton = device.wait(Until.findObject(By.res("com.sec.android.app.camera:id/shutter_button")), TIMEOUT) // Samsung example
                                    if (shutterButton != null) {
                                        shutterButton.click()
                                        println("Photo captured with alternative shutter button.")
                                    } else {
                                        throw Exception("Shutter button not found in the camera app.")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GoogleKeepTest", "Test failed: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun clickIfExistsRes(device: UiDevice, resourceId: String): Boolean {
        return device.wait(Until.findObject(By.res(resourceId)), 5000) != null &&
                device.findObject(By.res(resourceId)).let {
                    it.click()
                    device.waitForIdle()
                    true
                }
    }




}