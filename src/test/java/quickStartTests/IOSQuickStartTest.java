package quickStartTests;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;


public class IOSQuickStartTest {

    IOSDriver<IOSElement> driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();
    String accessKey = "<ACCESS_KEY>";
    String appiumVersion = "<APPIUM_VERSION>";
    String cloudUrl = "<CLOUD_URL>" + "/wd/hub";

    @BeforeEach
    public void setUp() throws MalformedURLException {
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("appiumVersion", appiumVersion);
        dc.setCapability("deviceQuery", "@os='ios'");
        dc.setCapability("testName", "Run Quickstart test on iOS device");
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
        driver = new IOSDriver<>(new URL(cloudUrl), dc);
    }

    @Test
    public void runQuickStartIOSNative() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='loginButton']")).click();
        driver.findElement(By.xpath("//*[@name='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@name='phoneTextField']")).sendKeys("0501234567");
        driver.findElement(By.xpath("//*[@name='nameTextField']")).sendKeys("John Snow");
        driver.findElement(By.xpath("//*[@name='amountTextField']")).sendKeys("50");
        driver.findElement(By.xpath("//*[@name='countryButton']")).click();
        driver.findElement(By.xpath("//*[@name='Switzerland']")).click();
        driver.findElement(By.xpath("//*[@name='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@name='Yes']")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}