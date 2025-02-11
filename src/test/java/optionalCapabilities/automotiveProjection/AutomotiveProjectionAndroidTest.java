package optionalCapabilities.automotiveProjection;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.jupiter.api.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class AutomotiveProjectionAndroidTest {

    static {
        nu.pattern.OpenCV.loadLocally(); // Load OpenCV native library
    }

    AndroidDriver<AndroidElement> driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();
    final String CLOUD_URL = "<CLOUD_URL>" + "/wd/hub";
    final String ACCESS_KEY = "<ACCESS_KEY>";
    final String APPIUM_VERSION = "<APPIUM_VERSION>";
    final String DHU_SCREEN_SIZE = "<DHU_SCREEN_SIZE>"; // "800x480" | "1280x720" |"1920x1080"

    @BeforeEach
    public void setUp() throws MalformedURLException {
        dc.setCapability("testName", "Android Auto quick start test");
        dc.setCapability("accessKey", ACCESS_KEY);
        dc.setCapability("appiumVersion", APPIUM_VERSION);
        dc.setCapability("deviceQuery", "@os='android'");
        dc.setCapability("appPackage", "com.google.android.apps.maps");
        dc.setCapability("appActivity", "com.google.android.maps.MapsActivity");
        dc.setCapability("digitalai:automotiveProjection", DHU_SCREEN_SIZE);
        dc.setCapability("autoGrantPermissions", true); //for location permission
        driver = new AndroidDriver<>(new URL(CLOUD_URL), dc);
    }

    @Test
    public void quickStartAndroidNativeDemo() throws InterruptedException {
        // This method walks through the values in the given file and sets the location after the delay in the delay parameter.
        // This is used to simulate the movement of a device. For more info see step 3 in test description
        driver.executeScript("seetest:client.setLocationPlaybackFile", "cloud:locationPoints", 1000, "gps");
        Thread.sleep(5000);

        //on 800x480 screen, side menu can interrupt the applications view, so we need to make sure the app is on full screen
        makeSureAppOnFullScreen();
        Thread.sleep(3000);
        Mat searchBarImage = Imgcodecs.imread("searchBar.png");
        Point pointToTap = findImageUsingOpencv(getDHUScreenshotAsMat(), searchBarImage);
        driver.executeScript("digitalai:automotive.tap", pointToTap.x, pointToTap.y);
        Thread.sleep(3000);
        pointToTap = findImageUsingOpencv(getDHUScreenshotAsMat(), searchBarImage);
        driver.executeScript("digitalai:automotive.tap", pointToTap.x, pointToTap.y);
        Thread.sleep(3000);

        //send keys to keyboard on mobile device
        driver.findElement(By.xpath("//*[@resource-id='com.google.android.projection.gearhead:id/open_search_view_edit_text']")).sendKeys("tel aviv");
        driver.executeScript( "mobile: performEditorAction", ImmutableMap.of("action", "Search"));
        Thread.sleep(5000);

        Mat startButtonImage = Imgcodecs.imread("startButton.png");
        pointToTap = findImageUsingOpencv(getDHUScreenshotAsMat(), startButtonImage);
        driver.executeScript("digitalai:automotive.tap", pointToTap.x, pointToTap.y);
        Thread.sleep(5000);
    }

    /**
     * Captures a screenshot from the DHU (Desktop Head Unit) and converts it to a Mat object(from Opencv Library).
     *
     * @return A Mat object containing the DHU screenshot.
     */
    private Mat getDHUScreenshotAsMat() {
        String base64String = (String) driver.executeScript("digitalai:automotive.getScreenshot");
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        // Write the byte array to the output PNG file
        try (FileOutputStream fos = new FileOutputStream("screenshot.png")) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Imgcodecs.imread("screenshot.png");
    }

    /**
     * Finds the template Using matchTemplate method image within the input image using OpenCV.
     * @param inputImage    The input image in which to search for the template.
     * @param templateImage The template image to find within the input image.
     */
    private Point findImageUsingOpencv(Mat inputImage, Mat templateImage) {
        if (inputImage.empty() || templateImage.empty()) {
            throw new RuntimeException("Error loading images");
        }

        Mat result = new Mat();
        Imgproc.matchTemplate(inputImage, templateImage, result, Imgproc.TM_CCOEFF_NORMED); //find the template in the input image
        MatOfPoint points = new MatOfPoint();

        Core.findNonZero(result, points);
        for (Point p : points.toList()) {
            // Check if the result at this location is above the threshold
            if (result.get((int) p.y, (int) p.x)[0] >= 0.8) {
                System.out.println("Found image at " + p);
                return p;
            }
        }
        throw new RuntimeException("Cannot find required image on the screen");
    }

    private void makeSureAppOnFullScreen() {
        Mat menuButtonImage = Imgcodecs.imread("menuButton.png");
        try {
            findImageUsingOpencv(getDHUScreenshotAsMat(), menuButtonImage);
            driver.executeScript("digitalai:automotive.tap", 200, 200); //tap in the middle of the screen
        }catch (RuntimeException ignored){
            System.out.println("App is already on full screen");
        }
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Report URL: " + driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }
}