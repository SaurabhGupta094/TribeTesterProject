package tests.groupingSample;

import org.testng.annotations.Test;

public class Test1 extends SuperClass {

	Test1() {
		super();
	}

	@Test(groups = { "Smoke", "Regression" })
	public void testSmoke() {
		System.err.println("Test1 Smoke");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression() {
		System.err.println("Test1 Regression");
	}

	@Test(groups = { "Sanity", "Common" })
	public void testSanity() {
		System.err.println("Test1 Sanity");
	}

	@Test(groups = { "Regression", "Common" })
	public void testRegression1() {
		System.err.println("Test1 Regression1");
	}

	@Test(groups = { "Common" })
	public void testMethod1() {
		System.err.println("Test1 TestMethod1");
	}
}
