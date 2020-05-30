package tests.AndroidiOSRegression;

import common.CommonTestManager;
import jirautil.JiraPolicy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LogInLogOutTest extends CommonTestManager {

    String sEmailAddress;
    String sPassword;

    @BeforeMethod
    public void loadTestData() {
        setTestDataProperties("Login");

    }

    @DataProvider(name = "LoginDetail")
    public static Object[][] credentials() {
        return new Object[][]{{"iossetup@gmail.com", "test1234", "W04"}};

    }

    @JiraPolicy(raiseDefectFlag = false)
    @Test(description = "Check if user is already logged in then it logout, if user is logged out then login and then logout", groups = {"Regression"}, dataProvider = "LoginDetail")
    public void WebMakeMyTrip(String sEmailAddress, String sPassword) throws InterruptedException {

        step("Click on Log in");

    }

    @JiraPolicy(raiseDefectFlag = false)
    @Test(description = "Check if user is already logged in then it logout, if user is logged out then login and then logout", groups = {"Regression"}, dataProvider = "LoginDetail")
    public void AndroidMakeMyTrip(String sEmailAddress, String sPassword) throws InterruptedException {

        step("Click on Log in");

    }
}