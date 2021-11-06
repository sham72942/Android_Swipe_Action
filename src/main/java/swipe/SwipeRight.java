package swipe;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

final class Config {
    static Configuration config;

    static {
        try {
            config = new PropertiesConfiguration("bumble.txt");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    static String APP_PACKAGE = config.getString("APP_PACKAGE");
    static String APP_ACTIVITY = config.getString("APP_ACTIVITY");
    static String USER_NAME = config.getString("USER_NAME");
    static String USER_AGE = config.getString("USER_AGE");
    static String[] ERRORS = config.getString("ERRORS", "").split(",");

    static String PLATFORM_NAME = config.getString("PLATFORM_NAME", "Random Mobile OS");
    static String PLATFORM_VERSION = config.getString("PLATFORM_VERSION", "9");
    static String DEVICE_NAME = config.getString("DEVICE_NAME", "AOSP on IA Emulator");
}

public class SwipeRight {
    // Animation default time:
    //  - Android: 300 ms
    //  - iOS: 200 ms
    // final value depends on your app and could be greater
    private static final int ANIMATION_TIME = 200; // ms

    private static final int PRESS_TIME = 200; // ms

    public static void main(String[] args) {
        int totalRightSwipes = Integer.parseInt(args[0]);

        try {
            openBumble(totalRightSwipes);
        }
        catch (Exception exp){
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
        }
    }

    public static void openBumble(int totalRightSwipes) throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = getDesiredCapabilities();

        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        AppiumDriver driver = new AppiumDriver<MobileElement>(url, caps);

        Thread.sleep(10000);
        swipeScreen(driver,totalRightSwipes);
    }

    public static void swipeScreen(AppiumDriver driver, int totalRightSwipes) throws InterruptedException {
//        System.out.println("swipeScreen(): dir: '" + dir + "'"); // always log your actions

        int edgeBorder = 10; // better avoid edges
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Dimension dims = driver.manage().window().getSize();

        // init start point = center of screen
        //Swipe right options
        pointOptionStart = PointOption.point(dims.width / 2, dims.height / 2);
        pointOptionEnd = PointOption.point(dims.width - edgeBorder, dims.height / 2);

//        switch (dir) {
//            case DOWN: // center of footer
//                pointOptionEnd = PointOption.point(dims.width / 2, dims.height - edgeBorder);
//                break;
//            case UP: // center of header
//                pointOptionEnd = PointOption.point(dims.width / 2, edgeBorder);
//                break;
//            case LEFT: // center of left side
//                pointOptionEnd = PointOption.point(edgeBorder, dims.height / 2);
//                break;
//            case RIGHT: // center of right side
//                pointOptionEnd = PointOption.point(dims.width - edgeBorder, dims.height / 2);
//                break;
//            default:
//                throw new IllegalArgumentException("swipeScreen(): dir: '" + dir + "' NOT supported");
//        }

//        MobileElement navTool = (MobileElement) driver.findElement(By.id("com.bumble.app:id/mainApp_navigationToolbar"));
//        while(navTool.isDisplayed()){

//            System.out.printf("Swiped %s Right\n", name);
//        }

        int i=0,j=0;
        while(i++<totalRightSwipes){
//            Thread.sleep((int)(Math.random() * 200) + 100);
            MobileElement nameElem = getElement(driver, Config.USER_NAME);
            MobileElement ageElem = getElement(driver, Config.USER_AGE);
            if(nameElem==null || ageElem==null){
                removeErrors(driver, Config.ERRORS);
                continue;
            }
            String name = nameElem.getAttribute("text");
            String age = ageElem.getAttribute("text");
            touchAction(driver, pointOptionStart, pointOptionEnd);
            System.out.printf("%d.) Swiped %s Right, she's %s\n", ++j, name, age);
        }
    }

    public static MobileElement getElement(AppiumDriver driver, String id){
        try {
//            System.out.printf("Found element %s\n", id);
            return (MobileElement) driver.findElement(By.id(id));
        } catch (Exception exp) {
//            System.out.printf("Not found element %s\n", id);
            return null;
        }
    }

    public static void removeErrors(AppiumDriver driver, String[] errorIds){
        System.out.println("Unable to right swipe, trying to remove errors");
        for(String id: errorIds){
            try {
                MobileElement elem = (MobileElement) driver.findElement(By.id(id));
//                System.out.printf("Found error %s\t trying to mitigate it.", id);
                elem.click();
//                Thread.sleep(200);
            } catch (Exception exp) {
//                System.out.printf("Not found error %s\n", id);
            }
        }
    }

    public static void touchAction(AppiumDriver driver,
                                   PointOption pointOptionStart,
                                   PointOption pointOptionEnd){
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

//    public enum Direction {
//        UP,
//        DOWN,
//        LEFT,
//        RIGHT;
//    }

    public static DesiredCapabilities getDesiredCapabilities(){
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",Config.PLATFORM_NAME);
        caps.setCapability("platformVersion",Config.PLATFORM_VERSION);
        caps.setCapability("deviceName", Config.DEVICE_NAME);

        caps.setCapability("appPackage", Config.APP_PACKAGE);
        caps.setCapability("appActivity", Config.APP_ACTIVITY);
        caps.setCapability("automationName","UiAutomator2");

        return caps;
    }
}
