# Automotive Projection Android auto and Carplay Test

This project contains a test for the automotive projection capability using Appium and OpenCV.

Test example was created for automotive with "800x480" screen size and Google Maps application.
Images on resources folder were taken from that screen, if you are using a different screen size you should take images from your screen.

## Prerequisites
- Appium
- OpenCV - OpenCV is an open-source computer vision library.
  Using matchTemplate method of OpenCV to find the location of elements on the DHU screen and interact with it.
See build.gradle file

## Setup

1. Clone the repository.
2. Install the required dependencies using Gradle.
3. Set up the environment variables for `CLOUD_URL`, `ACCESS_KEY`, `APPIUM_VERSION`, and `DHU_SCREEN_SIZE`.

## Android auto Test Description

The test performs the following steps:

1. Sets up the desired capabilities for the Android driver.
2. Launches the Google Maps application.
3. Simulates location playback using a predefined file. file should be uploaded to file repository first. for more info: 
https://docs.digital.ai/continuous-testing/docs/te/test-execution-home/mobile-android-and-ios/appium/seetest-appium-extension/device-commands/setlocationplaybackfile
4. Capture an image of the elements on the DHU screen that you want to interact with (in this example we will tap on location, exit, start buttons and on search bar).
5. Uses OpenCV to find and interact with UI elements in the DHU screen.
6. Taps on the search bar, enters a location, and starts navigation.

## Carplay Test Description
The test performs the following steps:
1. Sets up the desired capabilities for the iOS driver.
2. getting dump from the application running on Carplay screen (must be 3rd part application).
3. parse the dump and find the element to interact with (in this example we will tap on search bar).
4. perform tap on the element.

## Additional Information

For more information about this capability, see https://docs.digital.ai/continuous-testing/docs/te/test-execution-home/integrations/automative-car-projection-testing/test-execution-with-android-auto#create-android-auto-test