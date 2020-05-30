package tests.jira;

import base.BaseTest;
import jirautil.JiraPolicy;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestJiraDemo  {

	@JiraPolicy(raiseDefectFlag = true)
	@Test(description = "TestSomething")
	public void testSomething() {

		StringWriter stringWriter = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(stringWriter);
	    printWriter.println("Step 1");
	    printWriter.println("Step 2");
	    printWriter.println("Step 3");
	    printWriter.println("Step 4");
		
		Assert.fail(stringWriter.toString());
		printWriter.close();

	}
	
	@AfterMethod
	public void test(ITestResult result) {
		System.out.println("Description : "+result.getMethod().getDescription());

	}
	
}
