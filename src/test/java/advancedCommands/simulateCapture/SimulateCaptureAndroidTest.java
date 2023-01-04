package advancedCommands.simulateCapture;

import advancedCommands.AndroidTestBase;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 * The command allows users to test applications that use the camera on a mobile device.
 * The command injects an image file to the camera preview screen.
 * The command can run using file with unique name in file repository or with file URL
 */
class SimulateCaptureAndroidTest extends AndroidTestBase {

    @BeforeEach
    public void before() throws MalformedURLException {
        dc.setCapability("testName", "Run simulate capture test on Android device");
        dc.setCapability("autoGrantPermissions", true);
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.uicatalog/.MainActivity");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.uicatalog");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        driver = new AndroidDriver<>(new URL(CLOUD_URL), dc);
    }


    private void runSimulateCapture(boolean fromURL) throws InterruptedException {
        try {
            driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
        } catch (NoSuchElementException e) {
            // ignore
        }

        driver.findElement(By.xpath("//*[@text='Camera']")).click();
        driver.findElementByXPath("//*[@text='CAMERA API2 (JPEG)' or @id='scanner3']").click();
        if (fromURL) {
            driver.executeScript("seetest:client.simulateCapture", "<FILE_URL>");
        } else {
            driver.executeScript("seetest:client.simulateCapture", "cloud:<FILE_UNIQUE_NAME>");
        }
        Thread.sleep(5000); // time to wait until the image will be shown
    }


    @Test
    void simulateCaptureFromURL() throws InterruptedException {
        runSimulateCapture(true);
    }

    @Test
    void simulateCaptureFromFileRepository() throws Exception {
     runSimulateCapture(false);
    }

}