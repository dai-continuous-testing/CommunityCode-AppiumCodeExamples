package advancedCommands.installApp;

import advancedCommands.AndroidTestBase;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstallAppAndroidTest extends AndroidTestBase {

    private static final String APP_PACKAGE = "com.experitest.ExperiBank";
    private String APP_BUILD_VERSION = "1";
    private String APP_RELEASE_VERSION = "1.0.1";
    private String APP_UNIQUE_NAME = "test1";



    @BeforeEach
    public void before() throws MalformedURLException {
        dc.setCapability("testName", "Install application test on Android device");
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank/.LoginActivity");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
        driver = new AndroidDriver<>(new URL(CLOUD_URL), dc);
    }

    @Test
    public void installAppByName() {
        assertNotInstalled();
        driver.installApp("cloud:" + APP_PACKAGE);
        assertInstalled();
    }

    @Test
    public void installAppByUniqueName() {
        assertNotInstalled();
        driver.installApp("cloud:uniqueName=" + APP_UNIQUE_NAME);
        assertInstalled();
    }

    @Test
    public void installAppByBuildVersion() {
        assertNotInstalled();
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":buildVersion=" + APP_BUILD_VERSION);
        assertInstalled();
    }

    @Test
    public void installAppByReleaseVersion() {
        assertNotInstalled();
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION);
        assertInstalled();
    }


    @Test
    public void installAppByReleaseVersionAndBuildVersion() {
        assertNotInstalled();
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION +
                        ":buildVersion=" + APP_BUILD_VERSION);
        assertInstalled();

    }

    private void assertNotInstalled() {
        driver.removeApp(APP_PACKAGE);
        assertFalse(driver.isAppInstalled(APP_PACKAGE));
    }

    private void assertInstalled() {
        assertTrue(driver.isAppInstalled(APP_PACKAGE));
    }
}