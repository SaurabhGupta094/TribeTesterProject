package tests.groupingSample;

import org.testng.annotations.Test;

public class Test3 extends SuperClass {

	@Test(groups = { "Smoke", "Regression" })
	public void testSmoke() {
		System.err.println("Test3 Smoke");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression() {
		System.err.println("Test3 Regression");
	}

	@Test(groups = { "Sanity", "Common" })
	public void testSanity() {
		System.err.println("Test3 Sanity");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression1() {
		System.err.println("Test3 Regression1");
	}

	@Test(groups = { "Common" })
	public void testMethod3() {
		System.err.println("Test3 TestMethod3");
	}

}
