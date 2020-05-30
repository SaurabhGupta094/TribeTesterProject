package tests.testrail;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import testrailutil.TestRail;

public class TestRailDemo extends BaseTest {

    @Test
    @TestRail(testCaseId = {881700})
    public void testTestRail() {
//        To use Test rail implementation put "@TestRail(testCaseId = {yourTestSpecificID})" on top of test method
        System.out.println("Testing");
        Assert.fail("Intentionally failing the test case");
    }
}
