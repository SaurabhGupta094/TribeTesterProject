package tests.groupingSample;

import org.testng.annotations.Test;

public class Test2 extends SuperClass {

	@Test(groups = { "Smoke", "Regression" })
	public void testSmoke() {
		System.err.println("Test2 Smoke");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression() {
		System.err.println("Test2 Regression");
	}

	@Test(groups = { "Sanity", "Common" })
	public void testSanity() {
		System.err.println("Test2 Sanity");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression1() {
		System.err.println("Test2 Regression1");
	}

	@Test(groups = { "Common" })
	public void testMethod1() {
		System.err.println("Test2 TestMethod1");
	}

}
