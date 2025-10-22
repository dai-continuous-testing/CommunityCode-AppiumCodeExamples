package optionalCapabilities.automotiveProjection;

import io.appium.java_client.ios.IOSDriver;
import java.awt.Point;
import java.net.MalformedURLException;
import java.net.URL;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutomotiveProjectionCarplayTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";
    private static final String CARPLAY_SCREEN_SIZE = "800x480";

    private IOSDriver driver = null;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .amend("digitalai:testName", "Android Auto quick start test")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:automotiveProjection", CARPLAY_SCREEN_SIZE);
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void quickStartCarplayDemo() throws JSONException {
        String dump = (String) driver.executeScript("digitalai:automotive.getdump");
        Point p = findElementByLabel(dump, "searchButton");
        driver.executeScript("digitalai:automotive.tap", p.x, p.y);
    }

    private Point findElementByLabel(String dump, String label) throws JSONException {
        JSONObject root = new JSONObject(dump);
        JSONObject element = findMatchObject(root, label, "label");
        if (element == null) {
            throw new RuntimeException(String.format("Element with label %s not found.", label));
        }
        return getPointFromElement(element);
    }

    private JSONObject findMatchObject(JSONObject obj, String targetLabel, String attr) throws JSONException {
        if (obj.has(attr) && targetLabel.equals(obj.getString(attr))) {
            return obj;
        }

        if (obj.has("subviews")) {
            JSONArray subviews = obj.getJSONArray("subviews");
            for (int i = 0; i < subviews.length(); i++) {
                JSONObject child = subviews.getJSONObject(i);
                JSONObject found = findMatchObject(child, targetLabel, attr);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private Point getPointFromElement(JSONObject element) throws JSONException {
        JSONObject frame = element.getJSONObject("accessibilityFrame");
        JSONObject origin = frame.getJSONObject("origin");
        double x = origin.getDouble("x");
        double y = origin.getDouble("y");
        return new Point((int) x, (int) y);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Report URL: " + driver.getCapabilities().getCapability("digitalai:reportUrl"));
        driver.quit();
    }
}
