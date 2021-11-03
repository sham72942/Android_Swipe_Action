package appiumTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BumbleSwipe {
    public static void main(String[] args) {
        try {
            openBumble();
        }
        catch (Exception exp){
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
        }
    }

    public static void openBumble() throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = getDesiredCapabilities();

        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        AppiumDriver driver = new AppiumDriver<MobileElement>(url, caps);
        Thread.sleep(20000);
        while(true){
            swipeScreen(Direction.RIGHT, driver);
            Thread.sleep((int)(Math.random() * 400) + 100);
        }



//        System.out.println("Bumble started...");
    }

    public static void swipeScreen(Direction dir, AppiumDriver driver) {
        System.out.println("swipeScreen(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 200; // ms

        int edgeBorder = 10; // better avoid edges
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Dimension dims = driver.manage().window().getSize();

        // init start point = center of screen
        pointOptionStart = PointOption.point(dims.width / 2, dims.height / 2);

        switch (dir) {
            case DOWN: // center of footer
                pointOptionEnd = PointOption.point(dims.width / 2, dims.height - edgeBorder);
                break;
            case UP: // center of header
                pointOptionEnd = PointOption.point(dims.width / 2, edgeBorder);
                break;
            case LEFT: // center of left side
                pointOptionEnd = PointOption.point(edgeBorder, dims.height / 2);
                break;
            case RIGHT: // center of right side
                pointOptionEnd = PointOption.point(dims.width - edgeBorder, dims.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeScreen(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(driver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeScreen(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    public static DesiredCapabilities getDesiredCapabilities(){
        DesiredCapabilities caps = new DesiredCapabilities();
        //For Android emulator
        caps.setCapability("platformName","Android");
        caps.setCapability("platformVersion","9");
        caps.setCapability("deviceName", "AOSP on IA Emulator");

        //For my realme Phone
//        caps.setCapability("platformName","Android");
//        caps.setCapability("platformVersion","11");
//        caps.setCapability("deviceName", "realme narzo 20 pro");

        caps.setCapability("appPackage","com.bumble.app");
        caps.setCapability("appActivity","com.bumble.app.ui.launcher.BumbleLauncherActivity");
        caps.setCapability("automationName","UiAutomator2");

        return caps;
    }
}
