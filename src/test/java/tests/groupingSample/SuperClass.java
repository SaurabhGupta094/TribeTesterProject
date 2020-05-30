package tests.groupingSample;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class SuperClass {

	@BeforeTest
	public void beforeTest() {

		System.err.println(" ------Before Test------ ");
	}
	
	
	@AfterTest
	public void afterTest() {

		System.err.println(" ------After Test------ ");
	}

}
