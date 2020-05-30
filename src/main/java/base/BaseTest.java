package base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.ExtentTestManager;
import utils.TestListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

@Listeners({utils.TestListener.class})
public class BaseTest {

    // Parent class of Android and IOSDriver

    protected AppiumDriver<MobileElement> driver = null;
    protected RemoteWebDriver driver1 = null;



    protected File file = new File("");
    public static String sSeperator = System.getProperty("file.separator");

    public static final String CONFIG_FILE_PATH = "/src/main/java/config/config.properties";
    public static final String ANDROID_CONFIG_FILE_PATH = "/src/main/java/config/android_config.properties";

    protected FileInputStream configFis;
    Properties configProp = new Properties();
    public static String sConfigPlatform;

    protected FileInputStream platformConfigFis;
    Properties platformConfigProp = new Properties();
    String OS;
    public static String sUDID;
    public static String sAppPath;
    public String sPlatformName;
    public static String sPlatformVersion;
    public static String sDeviceName;


    FileInputStream fis;
    Properties properties;
    Logger log = Logger.getLogger(BaseTest.class);

    @BeforeSuite
    public void setUp() throws Exception {

        // Log4j ImplementationS
        String propertiesFilePath = System.getProperty("user.dir") + "/src/main//resources/log4j/log4j.properties";

        PropertiesConfiguration log4jProperties = new PropertiesConfiguration(propertiesFilePath);
        log4jProperties.setProperty("log4j.appender.HTML.File",
                "Reports/" + TestListener.sLatestReportFolder + "/Logs/Htmllogs.html");
        log4jProperties.setProperty("log4j.appender.Appender2.File",
                "Reports/" + TestListener.sLatestReportFolder + "/Logs/Logs.log");
        log4jProperties.save();

        PropertyConfigurator.configure(propertiesFilePath);

        configFis = new FileInputStream(file.getAbsoluteFile() + CONFIG_FILE_PATH);
        configProp.load(configFis);
        sConfigPlatform = configProp.getProperty("Platform");

    }
//    

    /**
     * this method creates the driver depending upon the passed parameter (android
     * or iOS) and loads the properties files (config and test data properties
     * files).
     *
     * @param os         android or iOS
     * @param methodName - name of the method under execution
     * @throws Exception issue while loading properties files or creation of driver.
     */
    @Parameters({"os", "config", "environment"})
    @BeforeMethod
    public void createDriver(@Optional("") String os, @Optional("") String config_file, @Optional("") String environment, Method methodName) throws Exception {


        log.info("--------------BEFORE METHOD---------------");
        String sPlatformFromCmdLine = System.getProperty("Platform");

        log.info("sPlatformFromCmdLine   " + sPlatformFromCmdLine);
        if (sPlatformFromCmdLine == null) {
            System.out.println("Inside of If block " + os);
            if ((os.length() > 0)) {
                sConfigPlatform = os;
                log.info("Loading platform value from testng.xml file");
            } else {
                log.info("Loading platform value from configuration file");
            }
        } else {
            sConfigPlatform = sPlatformFromCmdLine;
            log.info("Loading platform value from maven command line");

        }
        log.info("sconfigPlatform : " + sConfigPlatform);
        log.info("Out of If Else block");
        platformPropertiesFileLoad(sConfigPlatform);

        JSONParser parser = new JSONParser();
        JSONObject config;

        if (sConfigPlatform.equalsIgnoreCase("android")) {
            System.out.println("appPath : " + sAppPath);
            if (config_file.length() > 0 && environment.length() > 0) {
                config = (JSONObject) parser.parse(new FileReader("src/main/java/config/androidconf/" + config_file));
                androidDriver(sAppPath, methodName, config, environment);

            } else {
                androidDriver(sAppPath, methodName, null, environment);
            }
            log.info("Android driver created");

        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    /**
     * purpose of this method is to load testdata properties file so that it can be
     * used to access test data from properties file to testcase
     *
     * @param sPropertiesFileName
     */
    public void setTestDataProperties(String sPropertiesFileName) {

        String sTestDataPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + "testdata" + File.separator;
        System.out.println(" sTestDataPath : " + sTestDataPath);

        sTestDataPath = sTestDataPath + sPropertiesFileName + ".properties";
        System.out.println(" sTestDataPath : " + sTestDataPath);

        try {
            fis = new FileInputStream(sTestDataPath);

            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occured while loading test data");
        }
    }

    /**
     * Reusable method to get value using key provided in testcase properties file.
     *
     * @param sKey
     * @return
     */
    public String getProperty(String sKey) {
        String sValue = properties.getProperty(sKey);
        return sValue;

    }

    /**
     * this method quit the driver after the execution of test(s)
     */
    @AfterMethod
    public void teardown() {
        log.info("-----------Shutting down driver-----------");
        driver.quit();
//		driver.resetApp();

    }

    /**
     * this method creates the android driver
     * <p>
     * // * @param buildPath - path to pick the location of the app
     *
     * @param methodName - name of the method under execution
     * @throws MalformedURLException Thrown to indicate that a malformed URL has
     *                               occurred.
     */
    public synchronized void androidDriver(String appPath, Method methodName, JSONObject jsonConfiguration, String environment) throws MalformedURLException {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (jsonConfiguration == null) {
                File app = new File(appPath);

                if (!new File(sAppPath).exists()) {
                    Assert.fail("Application path is incorrect, Please check path or application name");
                }
                capabilities.setCapability("platformName", sPlatformName);
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, sDeviceName);
                capabilities.setCapability("platformVersion", sPlatformVersion);
                capabilities.setCapability("udid", sUDID);
                capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

                capabilities.setCapability("noReset", true);
                capabilities.setCapability("fullReset", "false");
                capabilities.setCapability("locationServicesAuthorized", true);
                capabilities.setCapability("autoAcceptAlerts", true);
                capabilities.setCapability("autoGrantPermissions", true);

                capabilities.setCapability("maxTypingFrequency", 10);

                driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), capabilities);
            } else {
                JSONObject envs = (JSONObject) jsonConfiguration.get("environments");

                Map<String, String> envCapabilities = (Map<String, String>) envs.get(environment);
                Iterator it = envCapabilities.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
                }

                Map<String, String> commonCapabilities = (Map<String, String>) jsonConfiguration.get("capabilities");
                it = commonCapabilities.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if (capabilities.getCapability(pair.getKey().toString()) == null) {
                        capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
                    }
                }

                String username = System.getenv("BROWSERSTACK_USERNAME");
                if (username == null) {
                    username = (String) jsonConfiguration.get("user");
                }

                String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
                if (accessKey == null) {
                    accessKey = (String) jsonConfiguration.get("key");
                }

                String appID = System.getenv("BROWSERSTACK_APP_ID");
                if (appID != null && !appID.isEmpty()) {
                    capabilities.setCapability("app", appID);
                }

                capabilities.setCapability("build", "Android Parallel Build");
                capabilities.setCapability("name", methodName.getName());
                capabilities.setCapability("noReset", true);
                capabilities.setCapability("fullReset", "false");
                capabilities.setCapability("locationServicesAuthorized", true);
                capabilities.setCapability("autoAcceptAlerts", true);
                capabilities.setCapability("autoGrantPermissions", true);


                capabilities.setCapability("maxTypingFrequency", 10);
                driver = new AndroidDriver(new URL("http://" + username + ":" + accessKey + "@" + jsonConfiguration.get("server") + "/wd/hub"), capabilities);
                driver.resetApp();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occured while loading Android driver");
        }

    }


    /**
     * this method loads properties files config and file having test data.
     *
     * @param sPlatform android or ios, to load specific test data file.
     * @throws Exception property files are not loaded successfully
     */
    public void platformPropertiesFileLoad(String sPlatform) throws Exception {
        log.info("ConfigProperties loaded");

        if (sPlatform.toLowerCase().contentEquals("android")) {
            platformConfigFis = new FileInputStream(file.getAbsoluteFile() + ANDROID_CONFIG_FILE_PATH);
            platformConfigProp.load(platformConfigFis);

        } else {
            System.out.println("Else of properties file load");
            Assert.fail("Invalid platform name provided, It should be ios or android, Current platform value is "
                    + sPlatform);
        }
        sPlatformName = platformConfigProp.getProperty("PLATFORMNAME");
        sUDID = platformConfigProp.getProperty("UDID");
        sAppPath = platformConfigProp.getProperty("APPPATH");


        sPlatformVersion = platformConfigProp.getProperty("PLATFORMVERSION");
        sDeviceName = platformConfigProp.getProperty("DEVICENAME");

        System.out.println("sPlatformName :" + sPlatformName);
        System.out.println("sUDID :" + sUDID);
        System.out.println("sAppPath :" + sAppPath);
        System.out.println("sPlatformVersion :" + sPlatformVersion);
        System.out.println("sDeviceName :" + sDeviceName);
    }


    public String sStepInfo = "";

    public String stepToLoginToBug() {
        return sStepInfo;
    }

    public void step(String sStepMessage) {
        sStepInfo = sStepInfo + sStepMessage + "\n";
        log.info(sStepMessage);
        ExtentTestManager.getTest().log(Status.INFO, sStepMessage);
    }

    public String takeScreenshot(String sText) {

        String sScreenshotPath = null;
        try {
            if (driver != null) {
                String destDir = TestListener.sLatestReportFolderPath + sSeperator + "Screenshots";
                System.out.println("destDir : " + destDir);

                System.out.println("driver : " + driver);
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                System.out.println("Taking screenshot");
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_hh_mm_ssaa");
                String destFile = dateFormat.format(new Date()) + ".png";
                System.out.println("File name " + destFile);
                new File(destDir).mkdirs();
                sScreenshotPath = destDir + sSeperator + destFile;
                try {
                    // Copy paste file at destination folder location
                    FileUtils.copyFile(scrFile, new File(sScreenshotPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writeTextInImage(sScreenshotPath, sText);
                ExtentTest test = ExtentTestManager.getTest();
                test.addScreenCaptureFromPath(sScreenshotPath);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sScreenshotPath;
    }

    // Write text to image.
    public void writeTextInImage(String sImagePath, String sText) {

        BufferedImage img = null;
        Font testFont = null;
        try {
            img = ImageIO.read(new File(sImagePath));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // try/catch IOException

        /**
         * imageWidth and imageHeight are the bounds
         */
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // This is for size
        testFont = new Font("Lucida Console", Font.PLAIN, 30);

        FontMetrics fm = g2d.getFontMetrics(testFont);

        /**
         * requestedStringWidthSize e requestedStringHeightSize are the measures needed
         * to draw the text WITHOUT resizing.
         */
        final int requestedStringWidthSize = fm.stringWidth(sText);
        final int requestedStringHeightSize = fm.getHeight();

        double stringHeightSizeToUse = height;
        double stringWidthSizeToUse;

        double scale = stringHeightSizeToUse / requestedStringHeightSize;
        stringWidthSizeToUse = scale * requestedStringWidthSize;

        /**
         * Checking if fill in height makes the text go out of bound in width,
         * if it does, it rescalates it to size it to maximum width.
         */
        if (width < ((int) (Math.rint(stringWidthSizeToUse)))) {
            stringWidthSizeToUse = width;

            scale = stringWidthSizeToUse / requestedStringWidthSize;
        }
        // This is for color
        g2d.setPaint(Color.GREEN);
        g2d.setFont(testFont);

        // draw graphics
        g2d.drawImage(img, 0, 0, null);
        g2d.drawString(sText, 100, 100);

        g2d.dispose();

        File file = new File(sImagePath);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }


}
