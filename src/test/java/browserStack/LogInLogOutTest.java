package browserStack;

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
        sEmailAddress = getProperty("EmailAddress");
        sPassword = getProperty("Password");

    }

	@DataProvider(name = "LoginDetail")

	public static Object[][] credentials() {
		return new Object[][]{{"h@a.co", "test1234", "W04"}};

	}
	
    @JiraPolicy(raiseDefectFlag = false)
    @Test(description = "", groups = {"Regression"}, dataProvider = "LoginDetail")
    public void WebMakeTripTest(String sEmailAddress,String sPassword, String sDeviceType) throws InterruptedException {

        step("Click on Log in");
        step("Login as " + sEmailAddress);
        step("Password is " + sPassword);
        step("Device Type " + sDeviceType);



    }
}