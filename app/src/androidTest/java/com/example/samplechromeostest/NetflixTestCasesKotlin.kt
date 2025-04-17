package com.example.samplechromeostest


import android.accessibilityservice.GestureDescription
import android.view.MotionEvent
import android.view.MotionEvent.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.Path
import kotlin.io.path.moveTo


@RunWith(JUnit4::class)
class NetflixTestCasesKotlin {
    private val TIMEOUT = 5000L // 5 seconds wait time
    private val PLAY_STORE_PACKAGE = "com.android.vending"
    private val NETFLIX_PACKAGE = "com.netflix.mediaclient"
    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        assertNotNull(device)
    }

    @Test
    fun testInstallAndLaunchRoblox() {
        try {
            // Step 1: Open Play Store and search Netflix app
            device.executeShellCommand("am start -a android.intent.action.VIEW -d market://details?id=$NETFLIX_PACKAGE")
            if (!device.wait(Until.hasObject(By.pkg(PLAY_STORE_PACKAGE).depth(0)), TIMEOUT)) {
                throw Exception("Play Store failed to launch.")
            }

            // Step 2: Click Install to install "Netflix" app
            /*val installButton = device.wait(Until.findObject(By.text("Install")), TIMEOUT)
            if (installButton != null) {
                installButton.click()
            }*/

            clickInstallButton(device)
            //installOrUpdateNetflix()

            // Step 3: Wait for Installation
            Thread.sleep(10000) // Increase wait time for slower installations
            // Step 4: Wait for the "Open" button to appear
            var openButton = device.wait(Until.findObject(By.text("Open")), TIMEOUT)
            if (openButton == null) { // If "Play" is not found, check for "Open"
                openButton = device.wait(Until.findObject(By.text("Open")), TIMEOUT)
            }

            // Step 5: Click "Open" to launch Spotify
            if (openButton != null) {
                Thread.sleep(2000) // Short delay before clicking to ensure UI stability
                openButton.click()
                device.waitForIdle()
            } else {
                throw Exception("Play/Open button not found after installation.")
            }

            device.wait(Until.hasObject(By.pkg(NETFLIX_PACKAGE)), TIMEOUT)
            Thread.sleep(5000) // Allow app to load UI elements
            // Wait for the Login button and click it

            if (clickIfExistsRes(device, "com.google.android.gms:id/cancel")) {
                println("Close Icon  clicked")
                //com.netflix.mediaclient:id/2131428747 ---Present Working-----
                //com.netflix.mediaclient:id/2131428749
                if (clickIfExistsRes(device, "com.netflix.mediaclient:id/2131428747")) {
                    println("Sign In  clicked")
                    loginWithEmailAndPassword()

                   // clickAllowNotifications()


                }

            }

        } catch (e: Exception) {
            System.err.println("Test failed: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun clickIfExists(device: UiDevice, text: String): Boolean {
        return device.wait(Until.findObject(By.text(text)), TIMEOUT)?.let {
            device.findObject(By.text(text)).click()
            device.waitForIdle()
            true
        } ?: false
    }

    private fun clickIfExistsDesc(device: UiDevice, text: String): Boolean {
        return device.wait(Until.findObject(By.desc(text)), TIMEOUT)?.let {
            device.findObject(By.desc(text)).click()
            device.waitForIdle()
            true
        } ?: false
    }

    private fun clickIfExistsRes(device: UiDevice, resourceId: String): Boolean {
        return device.wait(Until.findObject(By.res(resourceId)), TIMEOUT)?.let {
            device.findObject(By.res(resourceId)).click()
            device.waitForIdle()
            true
        } ?: false
    }

    private fun enterText(device: UiDevice, resourceId: String, text: String) {
        device.findObject(By.res(resourceId))?.setText(text)
        device.waitForIdle()
        println("Entered text: $text")
    }

    private fun pressEnter(device: UiDevice) {
        device.pressEnter()
        println("Enter key pressed")
    }

    @Test
    fun loginWithEmailAndPassword() {
        try {
            // Enter email
            //com.netflix.mediaclient:id/2131428658 ---- Working -----
            //com.netflix.mediaclient:id/2131428660
            val emailField = device.wait(Until.findObject(By.res("com.netflix.mediaclient:id/2131428658")), TIMEOUT)
            if (emailField != null) {
                emailField.text = "arcappstesting5@gmail.com" // Replace with your email
                device.waitForIdle()
            } else {
                System.err.println("Email field not found.")
                return;
            }

            // Enter password
            //com.netflix.mediaclient:id/2131428661 ---- working -----
            //com.netflix.mediaclient:id/2131428663
            val passwordField = device.wait(Until.findObject(By.res("com.netflix.mediaclient:id/2131428661")), TIMEOUT)
            if (passwordField != null) {
                passwordField.text = "ArcNetflix#123" // Replace with your password
                device.waitForIdle()
            } else {
                System.err.println("Password field not found.")
                return;
            }

            // Click sign in
            //com.netflix.mediaclient:id/2131428649 --- Working
            //com.netflix.mediaclient:id/2131428651
            val signInButton = device.wait(Until.findObject(By.res("com.netflix.mediaclient:id/2131428649")), TIMEOUT)
            if (signInButton != null) {
                signInButton.click()
                device.waitForIdle()
                println("Sign in button clicked.")
                clickFirstProfile()
            } else {
                System.err.println("Sign in button not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error during login: ${e.message}")
            e.printStackTrace()
        }
    }


    @Test
    fun clickFirstProfile() {
        try {
            // Find the GridView containing the profiles
            val profilesGridView: UiObject2? = device.wait(
                Until.findObject(By.res("com.netflix.mediaclient:id/2131429229")),
                TIMEOUT
            )

            if (profilesGridView != null) {
                // Find all profile views within the GridView
                val profileViews: List<UiObject2> = profilesGridView.findObjects(
                    By.res("com.netflix.mediaclient:id/2131429202")
                )

                if (profileViews.isNotEmpty()) {
                    // Click the first profile view
                    val firstProfile: UiObject2 = profileViews[0]
                    if (firstProfile.isClickable) {
                        firstProfile.click()
                        device.waitForIdle()
                        println("First profile clicked.")
                    } else {
                        System.err.println("First profile is not clickable.")
                    }
                } else {
                    System.err.println("No profile views found.")
                }
            } else {
                System.err.println("Profiles GridView not found.")
            }
        } catch (e: Exception) {
            System.err.println("Error clicking the first profile: ${e.message}")
            e.printStackTrace()
        }
    }

   /* @Test
    fun clickFirstProfile() {
        try {
            // Wait for the GridView to appear

            val gridViewExists = device.wait(Until.hasObject(By.res("com.netflix.mediaclient:id/2131429231")), TIMEOUT)
            if (gridViewExists) {
                val profilesGridView = device.findObject(By.res("com.netflix.mediaclient:id/2131429231"))

                val profileViews = profilesGridView.findObjects(By.res("com.netflix.mediaclient:id/2131429204"))

                if (profileViews.isNotEmpty()) {
                    val firstProfile = profileViews[0]

                    // Scroll to the profile if needed.
                    val bounds = firstProfile.visibleBounds
                    val visibleCenter = firstProfile.visibleCenter

                    val centerX = bounds.left + bounds.width() / 2
                    val centerY = bounds.top + bounds.height() / 2

                    *//*if (visibleCenter.x != centerX || visibleCenter.y != centerY) {
                        firstProfile.parent.scroll(UiObject2.Direction.SCROLL_FORWARD)
                        device.waitForIdle()
                    }*//*

                    // Attempt to click the profile
                    if (firstProfile.isClickable) {
                        firstProfile.click()
                        device.waitForIdle()
                        println("First profile clicked.")
                        if(clickIfExistsRes(device,"com.netflix.mediaclient:id/2131427605")) {
                            System.out.println("Allow clicked");
                            clickSearchButton()

                        }

                    } else {
                        System.err.println("First profile is not clickable.")
                    }
                } else {
                    System.err.println("No profile views found.")
                }
            } else {
                System.err.println("Grid view not found.")
            }
        } catch (e: Exception) {
            System.err.println("Error during profile selection: ${e.message}")
            e.printStackTrace()
        }
    }*/

    @Test
    fun clickAllowNotifications() {
        try {
            // Attempt to find the "Allow" button using various locators
            val allowButton: UiObject2? = device.wait(Until.findObject(By.res("com.netflix.mediaclient:id/2131427605").text("Allow")), TIMEOUT)
                ?: device.wait(Until.findObject(By.desc("Allow")), TIMEOUT)
                ?: device.wait(Until.findObject(By.clickable(true).text("Allow")), TIMEOUT)

            if (allowButton != null) {
                if (allowButton.isClickable) {
                    allowButton.click()
                    device.waitForIdle()
                    println("Allow button clicked.")
                } else {
                    System.err.println("Allow button is not clickable.")
                }
            } else {
                System.err.println("Allow button not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error clicking Allow: ${e.message}")
            e.printStackTrace()
        }
    }

    @Test
    fun clickAllowNotifications2() {
        try {
            // Wait for the Allow button to appear
            val allowButton: UiObject2? = device.wait(Until.findObject(By.res("com.android.permissioncontroller:id/permission_allow_button").text("Allow")), TIMEOUT)

            if (allowButton != null) {
                allowButton.click()
                device.waitForIdle()
                println("Allow button clicked.")
            } else {
                System.err.println("Allow button not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error clicking Allow: ${e.message}")
            e.printStackTrace()
        }
    }


    @Test
    fun clickSearchButton() {
        try {
            // Find the search button using its resource ID and content description
            val searchButton: UiObject2? = device.wait(Until.findObject(
                By.res("com.netflix.mediaclient:id/2131427348").desc("Search")
            ), TIMEOUT)

            if (searchButton != null) {
                if (searchButton.isClickable) {
                    searchButton.click()
                    device.waitForIdle()
                    println("Search button clicked.")
                    searchTheGoodDoctor()
                } else {
                    System.err.println("Search button is not clickable.")
                }
            } else {
                System.err.println("Search button not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error clicking the search button: ${e.message}")
            e.printStackTrace()
        }
    }


    @Test
    fun searchTheGoodDoctor() {
        try {
            // Find the search input field
            val searchInput: UiObject2? = device.wait(Until.findObject(
                By.res("android:id/search_src_text")
            ), TIMEOUT)

            if (searchInput != null) {
                if (searchInput.isClickable) {
                    searchInput.click()
                    device.waitForIdle()
                }
                searchInput.text = "The Good Doctor"
                device.waitForIdle()

                // Perform the search (you might need to simulate pressing the enter key)
                device.pressEnter()
                device.waitForIdle()
                clickTheGoodDoctor()

                println("Searched for 'The Good Doctor'.")

            } else {
                System.err.println("Search input field not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error searching 'The Good Doctor': ${e.message}")
            e.printStackTrace()
        }
    }

    @Test
    fun clickTheGoodDoctor() {
        try {
            // Find the "The Good Doctor" ImageView
            //2131428787
            val theGoodDoctorImage: UiObject2? = device.wait(Until.findObject(
                By.res("com.netflix.mediaclient:id/2131428787").desc("The Good Doctor")
            ), TIMEOUT)

            if (theGoodDoctorImage != null) {
                if (theGoodDoctorImage.isClickable) {
                    theGoodDoctorImage.click()
                    device.waitForIdle()
                    println("Clicked 'The Good Doctor'.")
                    clickResumeButton()
                } else {
                    System.err.println("'The Good Doctor' item is not clickable.")
                }
            } else {
                System.err.println("'The Good Doctor' item not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error clicking 'The Good Doctor': ${e.message}")
            e.printStackTrace()
        }
    }


    @Test
    fun clickResumeButton() {
        try {
            // Find the "Resume" Button by resource ID
            //2131429081
            val resumeButton: UiObject2? = device.wait(Until.findObject(
                By.res("com.netflix.mediaclient:id/2131429081")
            ), TIMEOUT)

            if (resumeButton != null) {
                if (resumeButton.isClickable) {
                    resumeButton.click()
                    device.waitForIdle()
                    println("Clicked 'Resume' button.")
                    forwardBackwardVideoClick()
                } else {
                    System.err.println("'Resume' button is not clickable.")
                }
            } else {
                System.err.println("'Resume' button not found.")
            }

        } catch (e: Exception) {
            System.err.println("Error clicking 'Resume' button: ${e.message}")
            e.printStackTrace()
        }
    }


    /*private fun captureScreenshot(testName: String) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "${testName}_$timestamp.png"
        val path = Environment.getExternalStorageDirectory().toString() + "/Screenshots/"

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = File(path, fileName)

        try {
            val bitmap = device.takeScreenshot()
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            println("Screenshot saved: ${file.absolutePath}")
        } catch (e: IOException) {
            System.err.println("Failed to save screenshot: ${e.message}")
            e.printStackTrace()
        } catch (e: SecurityException) {
            System.err.println("Security Exception, check permissions: ${e.message}")
            e.printStackTrace()
        }
    }*/


    fun clickInstallButton(device: UiDevice) {
        try {
            // Try to find a clickable parent or sibling element
            val installButtonContainer: UiObject2? = device.findObject(
                By.hasDescendant(By.text("Install")).clickable(true)
            )

            if (installButtonContainer != null) {
                installButtonContainer.click()
                device.waitForIdle()
                println("Clicked the install button container.")
            } else {
                System.err.println("Could not find a clickable install button.")
            }
        } catch (e: Exception) {
            System.err.println("Error clicking install button: ${e.message}")
            e.printStackTrace()
        }
    }



    @Test
    fun forwardBackwardVideoClick() {
        try {
            val playerControls: UiObject2? = device.wait(
                Until.findObject(By.res("com.netflix.mediaclient:id/2131428614")),
                TIMEOUT
            )

            if (playerControls != null) {
                val bounds = playerControls.visibleBounds
                val centerX = bounds.centerX()
                val centerY = bounds.centerY()
                val leftX = bounds.left + bounds.width() / 4;
                val rightX = bounds.right - bounds.width() / 4;

                // Forward (click right side of the control)
                device.click(rightX, centerY)
                device.waitForIdle()
                println("Video forwarded by click.")
        //MAnibabu manibabu
                // Backward (click left side of the control)
                device.click(leftX, centerY)
                device.waitForIdle()
                println("Video backwarded by click.")

            } else {
                System.err.println("Player controls not found.")
            }
        } catch (e: Exception) {
            System.err.println("Error forwarding/backwarding video: ${e.message}")
            e.printStackTrace()
        }
    }






}