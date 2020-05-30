package utils;

import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import static io.appium.java_client.touch.offset.ElementOption.element;


import static java.time.Duration.ofSeconds;

import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileBy.ByAccessibilityId;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

import static io.appium.java_client.touch.WaitOptions.waitOptions;

public class Actions extends WaitUntil {

	AppiumDriver<MobileElement> driver;
	protected Logger log = Logger.getLogger(Actions.class);

	private static final int STALENESS_MAX_RETRY_COUNT = 2;
	private int mStaleCount = 1;

	public Actions(AppiumDriver<MobileElement> driver) {
		super(driver);
		this.driver = driver;
	}

	public void click(MobileElement element, String sElement, int iTimeOut) {
		if (element == null) {
			throw new IllegalArgumentException( 
					"Unable to click on the element,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			element.click();
			log.info("Clicked on " + sElement);
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while clicking mobile element " + we);
			// assert false:"Time out exception is occurred";
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while clicking mobile element " + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while clicking mobile element " + we);
			// assert false:"WebDriver exception is occurred";
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
//	      Assert.fail("Exception found while clicking mobile element " + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while clicking mobile element " + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
	}

	public void type(final MobileElement element, final String textToType, String sElement, int iTimeOut) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to tye the text,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			element.sendKeys(textToType);
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while inputting text in text box " + we);
			// assert false:"Time out exception is occurred";
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while inputting text in text box " + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while inputting text in text box " + we);
			// assert false:"WebDriver exception is occurred";
		} catch (StaleElementReferenceException se) {
			if (mStaleCount <= STALENESS_MAX_RETRY_COUNT) {
				log.info("Staleness is observed and rrtying ");
				waitForElementVisibility(element, sElement, iTimeOut);
				mStaleCount++;
			} else {
				log.error("Stale element exception is occurred!!!!");
				log.error("Stack trace is" + se);
				Assert.fail("Exception found while inputting text in text box " + se);
			}
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
			Assert.fail("Exception found while inputting text in text box " + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while inputting text in text box " + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
	}

	
	public String getText(final MobileElement element, String sElement, int iTimeOut) {
		String text = "";
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to retrieve the text!!,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			text = element.getText();
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading text of an element " + we);
			// assert false:"Time out exception is occurred";
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while reading text of an element " + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading text of an element " + we);
			// assert false:"WebDriver exception is occurred";
		} catch (StaleElementReferenceException se) {
			if (mStaleCount <= STALENESS_MAX_RETRY_COUNT) {
				log.info("Staleness is observed and rrtying ");
				waitForElementVisibility(element, sElement, iTimeOut);
				mStaleCount++;
			} else {
				log.error("Stale element exception is occurred!!!!");
				log.error("Stack trace is" + se);
				Assert.fail("Exception found while reading text of an element " + se);
			}
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading text of an element " + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while reading text of an element " + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
		return text;
	}		
			
	/**
	 * Clears text in the particular mobile element.
	 *
	 * @param element  - mobile element that should be fetched.
	 * @param sElement
	 * @param iTimeOut
	 */
	public void clearText(final MobileElement element, String sElement, int iTimeOut) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to retrieve the text!!,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			element.clear();
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while clearing text of text box" + we);
			// assert false:"Time out exception is occurred";
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while clearing text of text box" + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while clearing text of text box" + we);
			// assert false:"WebDriver exception is occurred";
		} catch (StaleElementReferenceException se) {
			if (mStaleCount <= STALENESS_MAX_RETRY_COUNT) {
				log.info("Staleness is observed and rrtying ");
				waitForElementVisibility(element, sElement, iTimeOut);
				mStaleCount++;
			} else {
				log.error("Stale element exception is occurred!!!!");
				log.error("Stack trace is" + se);
				Assert.fail("Exception found while clearing text of text box" + se);
			}
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
			Assert.fail("Exception found while clearing text of text box" + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while clearing text of text box" + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
	}

	/**
	 * Gets attribute of particular mobile element.
	 *
	 * @param element       - mobile element that should be fetched.
	 * @param attributeName - attribute of mobile element.
	 * @param sElement
	 * @param iTimeOut
	 * @return return's attribute of a mobile element.
	 */
	public String getAttribute(final MobileElement element, final String attributeName, String sElement, int iTimeOut) {
		String text = "";
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to retrieve the attribute!!,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			text = element.getAttribute(attributeName);
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while reading attribute of element" + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
			// assert false:"WebDriver exception is occurred";
		} catch (StaleElementReferenceException se) {
			if (mStaleCount <= STALENESS_MAX_RETRY_COUNT) {
				log.info("Staleness is observed and rrtying ");
				waitForElementVisibility(element, sElement, iTimeOut);
				mStaleCount++;
			} else {
				log.error("Stale element exception is occurred!!!!");
				log.error("Stack trace is" + se);
				Assert.fail("Exception found while reading attribute of element" + se);
			}
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while reading attribute of element" + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
		return text;
	}

	/**
	 * Gets attribute value of particular mobile element.
	 *
	 * @param element       - mobile element that should be fetched.
	 * @param attributeName - attribute of mobile element.
	 * @param sElement
	 * @param iTimeOut
	 * @return return's attribute of a mobile element.
	 */
	public String getAttributeValue(final MobileElement element, final String attributeName, String sElement,
			int iTimeOut) {
		String text = "";
		final int sleepTime = 500;
		final int loopTill = 15;
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to retrieve the attribute!!,received mobile element is null! please check the mobile element details");
		}
		try {
			waitForElementVisibility(element, sElement, iTimeOut);
			for (int i = 0; i < loopTill; i++) {
				text = element.getAttribute(attributeName);
				if (text != null) {
					break;
				}
				Thread.sleep(sleepTime);
			}
		} catch (TimeoutException we) {
			log.error("Failed to retrieve the element within the time out!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
		} catch (InvalidSelectorException ie) {
			log.error("invalid xpath/css");
			Assert.fail("Exception found while reading attribute of element" + ie);
			// assert false:"InvalidSelector Exception is occurred";
		} catch (org.openqa.selenium.NoSuchElementException we) {
			log.error("Failed to retrieve the element, check the xpath/css selector!!");
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
			// assert false:"WebDriver exception is occurred";
		} catch (StaleElementReferenceException se) {
			if (mStaleCount <= STALENESS_MAX_RETRY_COUNT) {
				log.info("Staleness is observed and rrtying ");
				waitForElementVisibility(element, sElement, iTimeOut);
				mStaleCount++;
			} else {
				log.error("Stale element exception is occurred!!!!");
				log.error("Stack trace is" + se);
				Assert.fail("Exception found while reading attribute of element" + se);
			}
		} catch (WebDriverException we) {
			log.error("WebDriver exception is occurred" + we);
			log.error("stack trace is" + we);
			Assert.fail("Exception found while reading attribute of element" + we);
			// assert false:"NoSuchElement Exception is occurred";
		} catch (Exception e) {
			log.error("Failed to retrieve the element!! locator details are");
			log.error("Exception stack trace is:" + e);
			Assert.fail("Exception found while reading attribute of element" + e);
			// assert false:"Other Exception is occurred";
		} finally {
			mStaleCount = 1;
		}
		return text;
	}

	/**
	 * Waits till element is visible on activity.
	 *
	 * @param element - mobile element that should be checked.
	 */
	public void waitForExpectedElementVisibility(final MobileElement element) {
		final int waitingTime = 50;
		if (element == null) {
			throw new IllegalArgumentException("mobile element should not be null,please check element details");
		}
		final Wait<WebDriver> wait = new WebDriverWait(driver, waitingTime);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			log.info("Waiting for element visibility on the page !!element is now visible!!");
		} catch (WebDriverException we) {
			log.error("Waiting for element visibility on the page!! element is not visible within the Timeout!!!");
		}
	}

	/**
	 * Waits till element is present on activity.
	 *
	 * @param element - mobile element that should be checked.
	 */
	public void waitForExpectedElementPresence(final MobileElement element) {
		final int waitingTime = 60;
		if (element == null) {
			throw new IllegalArgumentException("mobile element should not be null,please check element details");
		}
		final Wait<WebDriver> wait = new WebDriverWait(driver, waitingTime);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocatorDetails(element)));
			log.info("Waiting for element presence on the DOM!! element is now visible!! ");
		} catch (WebDriverException we) {
			log.error("Waiting for element presence on the DOM!! element is not present within the Timeout !!!  ");
		}
	}

	/**
	 * Waits till element is visible on activity.
	 *
	 * @param element - mobile element that should be checked.
	 */
	public void waitForElementVisibility(final MobileElement element, String sElement, int timeOut) {
		final int pollingFrequencey = 3;
		if (element == null) {
			throw new IllegalArgumentException("mobile element should not be null,please check element details");
		}

		try {
			final Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS)
					.pollingEvery(pollingFrequencey, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException te) {
			log.error("Waiting for element presence on the DOM!! " + "element is not present within the Timeout !!!");
			log.error("stack trace" + te);
			Assert.fail("Waiting for element presence on the DOM!! " + "element is not present within the Timeout !!!"
					+ te);
		} catch (WebDriverException we) {
			log.error("Waiting for element presence on the DOM!! " + "element is not present within the Timeout !!!  ");
			log.error("stack trace" + we);
			Assert.fail("Waiting for element presence on the DOM!! " + "element is not present within the Timeout !!!"
					+ we);
		}
	}
	
	
//
	public String isElementEnable(MobileElement element,int iTimeOut) {
		boolean isDisplayed=false;
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, iTimeOut);
			wait.until(ExpectedConditions.visibilityOf(element));
			isDisplayed=true;
		}catch (Exception e) {
			isDisplayed=false;
		}
		if (isDisplayed)
		{
			return element+ " Element is Enable";
		}
		else
		{
			return element+" Element is Disable";			
		}
	}	
	
//
	public boolean isDisplayedWait(MobileElement element,int iTimeOut) {
		boolean isDisplayed=false;
	
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, iTimeOut);
			wait.until(ExpectedConditions.visibilityOf(element));
			isDisplayed=true;
		}catch (Exception e) {
			isDisplayed=false;
		}
	  return isDisplayed;
	}
	
	public void waitForTextToDisapppear(MobileElement element,String sText,int iWaitTime) {
		final Wait<WebDriver> wait = new WebDriverWait(driver, iWaitTime);
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, sText)));
	}
	
	public void waitForTextToApppear(MobileElement element,String sText,int iWaitTime) {
		final Wait<WebDriver> wait = new WebDriverWait(driver, iWaitTime);
		wait.until(ExpectedConditions.textToBePresentInElement(element, sText));
	}

	/**
	 * Waits test execution for particular seconds required.
	 *
	 * @param waitTimeInSeconds - seconds for which wait operation should be
	 *                          performed.
	 */
	public void waitForTimeInSeconds(final int waitTimeInSeconds) {
		try {

			log.info("waiting for " + waitTimeInSeconds + " Seconds");
			Thread.sleep(1000 * waitTimeInSeconds);
		} catch (Exception e) {
			log.error("Waiting for time interval");
			log.error("stack trace" + e);
			Assert.fail("Waiting for time interval" + e);
		}
	}

	/**
	 * Fetches locator detail of a mobile element.
	 *
	 * @param element - mobile element for which locator detail should be returned.
	 */
	protected final By getLocatorDetails(final MobileElement element) {
		if (element == null) {
			throw new IllegalArgumentException("Element should not be null! please check the mobile element details");
		}
		final String[] tokens = element.toString().split("->");
		final String token = tokens[tokens.length - 1].trim();
		By locator = null;

		if (token.contains("id:")) {
			locator = By.id(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (token.contains("xpath:")) {
			locator = By.xpath(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (!token.contains("class") && !token.contains("tag") && token.contains("name:")) {
			locator = By.name(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (token.contains("css selector:")) {
			locator = By.cssSelector(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (token.contains("class name:")) {
			locator = By.className(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (token.contains("partial") && token.contains("link text:")) {
			locator = By.partialLinkText(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (!token.contains("partial") && token.contains("link text:")) {
			locator = By.linkText(token.substring(0, token.length() - 1).split(":")[1].trim());
		} else if (token.contains("tag name:")) {
			locator = By.tagName(token.substring(0, token.length() - 1).split(":")[1].trim());
		}
		return locator;
	}

	/**
	 * Waits till element is not visible.
	 *
	 * @param element - mobile element that should be checked.
	 */
	public void waitForElementToNotExist(final MobileElement element) {
		final int pollingInterval = 1000;
		final int timeout = 180000;
		final WebDriverWait wait = new WebDriverWait(driver, 180);
		wait.pollingEvery(pollingInterval, TimeUnit.MILLISECONDS);
		wait.withTimeout(timeout, TimeUnit.MILLISECONDS);
		final ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver d) {
				try {
					d.findElement(getLocatorDetails(element));
					return false;
				} catch (final Exception e) {
					log.error("Element not found on DOM!");
					return true;
				}
			}
		};
		wait.until(expectation);
	}

	/**
	 * Call this method after typing input in text field for hiding keyboard.
	 */
	protected void hideKeyboard() {
		try {

			((AppiumDriver<?>) driver).hideKeyboard();

			((AppiumDriver<?>) driver).hideKeyboard();
		} catch (WebDriverException we) {
			log.error("Soft Keyboard isn't present.");
			log.error("stack trace is" + we);
			Assert.fail("Exception found in hideKeyboard()" + we);
		}
	}

	/**
	 * Pushes current activity to background for 3 seconds and resumes from
	 * background.
	 */
	public void pushAppToBackground() {
		((AndroidDriver<?>) driver).pressKeyCode(AndroidKeyCode.HOME);
		((AndroidDriver<?>) driver).pressKeyCode(AndroidKeyCode.KEYCODE_APP_SWITCH);
		driver.findElement(By.id("com.android.systemui:id/task_view_bar")).click();
	}

	public static boolean waitAndTapOnElementById(String id) {
		MobileElement e = WaitUntil.waitForMobileElementToBeClickable(ByAccessibilityId.id(id));
		if (e != null) {
			e.click();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if mobile element present in the DOM.
	 *
	 * @param element - mobile element that should be checked.
	 * @return returns true if present.
	 */
	public boolean isElementExistsOnDOM(final MobileElement element) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to check element existence on DOM,element is null,please check the mobile element details");
		}
		if (driver.findElements(getLocatorDetails(element)).size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if mobile element present in the DOM based on locator.
	 *
	 * @param element - locator based on which it should be checked.
	 * @return returns true if present.
	 */
	public boolean isElementExistsOnDOM(final By element) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Unable to check element existence on DOM,element is null,please check details");
		}
		if (driver.findElements(element).size() > 0) {
			return true;
		}
		return false;
	}

	public boolean isElementExistsOnDOM1(final By element) {
		if (element == null) {
			return false;
		}
		if (driver.findElements(element).size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if mobile element present in the Activity.
	 *
	 * @param element - mobile element that should be checked.
	 * @return returns true if present.
	 */
	public boolean isElementVisibleOnMobilePage(final MobileElement element) {
		final int sleepTime = 3000;
		if (element == null) {
			throw new IllegalArgumentException("mobile element should not be null,please check element details");
		}
		try {
			Thread.sleep(sleepTime);
			if (element.isDisplayed()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Waits till element is visible on activity.
	 *
	 * @param element          - mobile element that should be checked.
	 * @param timeOutInSeconds - wait time for element in seconds.
	 */
	public boolean waitForVisibilityOfElement(final MobileElement element, final int timeOutInSeconds) {
		try {
			new WebDriverWait(driver, timeOutInSeconds).until((ExpectedConditions.visibilityOf(element)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public MobileElement waitForMobileElementToBeClickable(MobileElement element) {
		try {

			WebDriverWait wait = new WebDriverWait(driver, 30);
			return (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return null;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return null;
		}
	}

//	public static String takeScreenShot() {
//		String destDir;
//		String sSeperator = System.getProperty("file.separator");
//		// Set folder name to store screenshots.
//		destDir = TestListener.sLatestReportFolderPath + sSeperator + "Screenshots";
//		// Capture screenshot.
//		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		// Set date format to set It as screenshot file name.
//		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_hh_mm_ssaa");
//		// Create folder under project with name "screenshots" provided to destDir.
//		new File(destDir).mkdirs();
//		// Set file name using current date time.
//		String destFile = dateFormat.format(new Date()) + ".png";
//
//		try {
//			// Copy paste file at destination folder location
//			FileUtils.copyFile(scrFile, new File(destDir + sSeperator + destFile));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return destDir + sSeperator + destFile;
//	}

	public String takeScreenshot() {
		String sScreenshotPath = null;

		String sSeperator=System.getProperty("file.separator");
		try {
			// Set folder name to store screenshots.
			String destDir = TestListener.sLatestReportFolderPath + sSeperator + "Screenshots";

			// Capture screenshot.
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			System.out.println("Taking screenshot");
			
			// Set date format to set It as screenshot file name.
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy_hh_mm_ssaa");
			// Set file name using current date time.
			String destFile = dateFormat.format(new Date()) + ".png";
			
			// Create folder under project with name "screenshots" provided to destDir.
			new File(destDir).mkdirs();
			sScreenshotPath = destDir + sSeperator + destFile;
			try {
				// Copy paste file at destination folder location
				FileUtils.copyFile(scrFile, new File(sScreenshotPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExtentTest test = ExtentTestManager.getTest();
			test.addScreenCaptureFromPath(sScreenshotPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sScreenshotPath "+ sScreenshotPath);
		return sScreenshotPath;
	}


	public String getToastMessage() {
		String result = null;
		try {
			String imgName = takeScreenshot();
			System.out.println("imgName "+ imgName);
			File imageFile = new File(imgName);
			System.out.println("Image name is :" + imageFile.toString());
			ITesseract instance = new Tesseract();

			File tessDataFolder = LoadLibs.extractTessResources("tessdata");
			instance.setDatapath(tessDataFolder.getAbsolutePath());
			result = instance.doOCR(imageFile);
			System.out.print("Extracted String :");
			System.out.println(result);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void scrollDown1() {
		Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.5;
		int scrollStart = screenHeightStart.intValue();
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();

		new TouchAction((PerformsTouchActions) driver).press(PointOption.point(0, scrollStart))
				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1))).moveTo(PointOption.point(0, scrollEnd))
				.release().perform();
	}

	public MobileElement getMobileElement(By by) {

		MobileElement element;
		try {

			element = driver.findElement(by);
		} catch (Exception e) {
			return null;
		}
		return element;

	}

	public List<MobileElement> getMobileElements(By by) {
		List<MobileElement> element;
		try {
			element = driver.findElements(by);
		} catch (Exception e) {
			return null;
		}

		return element;
	}

	public void clickOnVisibleTextInListBy(List<MobileElement> elements, String name) {
		boolean flag = false;

		for (MobileElement element : elements) {
			if (element.getAttribute("text").equals(name)) {
				System.out.println("Clicking on Text : " + element.getAttribute("text"));
				click(element, name, 5);
				flag = true;
				break;
			}
		}

		if (!flag) {
			Assert.fail("Cannot click based on text");
		}

	}

	public boolean clickOnVisibleTextInListBy(By by, String name) {
		boolean flag = false;
		java.util.List<MobileElement> ee = driver.findElements(by);

		for (MobileElement element : ee) {
			System.out.println("name : " + element.getAttribute("text"));
			if (element.getAttribute("text").equals(name)) {
				element.click();
				flag = true;
				break;
			}
		}
		return flag;
	}
	


//	public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage) {
//		System.out.println("Inside verticalSwipeByPercentages");
//        Dimension size = driver.manage().window().getSize();
//        System.out.println("Dim "+size);
//        int anchor = (int) (size.width * anchorPercentage);
//        int startPoint = (int) (size.height * startPercentage);
//        int endPoint = (int) (size.height * endPercentage);
//        
//        System.out.println("anchor "+anchor);
//        System.out.println("startPoint "+startPoint);
//        System.out.println("endPoint "+endPoint);   
//        
//    
//        
//        MobileElement swipeElement=driver.findElement(By.xpath("whistle_device_view_wgps_image"));
//        new TouchAction(driver)
//        .press(element(swipeElement))
//        .waitAction(waitOptions(ofSeconds(1)))
//        .moveTo(PointOption.point(1,2 ))
//        .release()
//        .perform();
//       }
	
	
	public void swipeAndroid(MobileElement swipeElement) {
	    Dimension size = driver.manage().window().getSize();
        System.out.println("Window dimenion "+size);
        
		Dimension dimension=swipeElement.getSize();
		int iXAxis=dimension.getWidth();
		int iYAxis=dimension.getHeight();
		
		System.out.println(iXAxis +"  "+iYAxis);
		
		new TouchAction(driver)
		.press(element(swipeElement))
		.waitAction(waitOptions(ofSeconds(1)))
		.moveTo(PointOption.point(iXAxis-950,0))
		.release()
		.perform();		
	}
	
	public void swipeIOS(MobileElement swipeElement) {

			System.out.println("Swiping start");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("direction", "left");
			params.put("element", ((RemoteWebElement) swipeElement).getId());
			js.executeScript("mobile: swipe", params);
			System.out.println("Swiping end");

	}
	
	// This is going to scroll up to get element.

//	public void scrollUpIOS(MobileElement swipeElement, MobileElement targetScrollElement) {
//			System.out.println("Swiping start & Target Element Text " + targetScrollElement.getText());
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("direction", "down");
//			
////			params.put("startX", "90");
////			params.put("startY", "400");
////			params.put("endX", "90"); //"90");
////		     params.put("endY", "350"); //"200");
////		     params.put("duration", "2000");
//			
//			params.put("element", ((RemoteWebElement) swipeElement).getId());
//			params.put("element", targetScrollElement.getText());
////			params.put("toVisible", "true");
//			js.executeScript("mobile: scroll", params);
//			System.out.println("Swiping end");
//		}

	//Horizontal Swipe by percentages
//    public void horizontalSwipeByPercentage (double startPercentage, double endPercentage, double anchorPercentage) {
//        Dimension size = driver.manage().window().getSize();
//        int anchor = (int) (size.height * anchorPercentage);
//        int startPoint = (int) (size.width * startPercentage);
//        int endPoint = (int) (size.width * endPercentage);
// 
//        new TouchAction(driver)
//                .press(point(startPoint, anchor))
//                .waitAction(waitOptions(ofMillis(1000)))
//                .moveTo(point(endPoint, anchor))
//                .release().perform();
//    }

    public void scrollDown() {
    	System.out.println("Scrolling Now Started");
    	
    	Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.8;
		int scrollStart = screenHeightStart.intValue();
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();
		
		new TouchAction((PerformsTouchActions) driver)
			.press(PointOption.point(0, scrollStart))
			.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
			.moveTo(PointOption.point(0, scrollEnd))
			.release().perform();
	
		sleep(1);

		System.out.println("Scorlling Now End");
	}  
    
    public void ScrollUp() {
    	System.out.println("Scorlling UP Now Started");
    	
    	Dimension dimensions = driver.manage().window().getSize();
    	
		Double screenHeightEnd = dimensions.getHeight() * 0.8;
		int  scrollEnd= screenHeightEnd.intValue();
		
		
		Double screenHeightStart = dimensions.getHeight() * 0.2;
		int scrollStart = screenHeightStart.intValue();
		System.out.println(" UP - scrollEnd "+scrollEnd);
		System.out.println(" UP - scrollStart "+scrollStart);

		new TouchAction((PerformsTouchActions) driver)
			.press(PointOption.point(0, scrollStart))
			.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
			.moveTo(PointOption.point(0, scrollEnd))
			.release().perform();
	
		sleep(1);

		System.out.println("Scorlling UP Now End");
	} 
 
    public void ScrollDownTest() {
//    	System.out.println("Scorlling Now Started");
    	
    	Dimension dimensions = driver.manage().window().getSize();
    	
		Double screenHeightEnd = dimensions.getHeight() * 0.8;
		int  scrollEnd= screenHeightEnd.intValue();
		
		
		Double screenHeightStart = dimensions.getHeight() * 0.2;
		int  scrollStart= screenHeightStart.intValue();
		
//		System.out.println(" DOWN - scrollEnd "+scrollEnd);
//		System.out.println(" DOWN - scrollStart "+scrollStart);
		
		new TouchAction((PerformsTouchActions) driver)
			.press(PointOption.point(0, scrollEnd))
			.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
			.moveTo(PointOption.point(0, scrollStart))
			.release().perform();
	
		sleep(1);

//		System.out.println("Scorlling Now End");
	} 
    
    public void sleep(int iSeconds) {
    	
    	try {
			Thread.sleep(iSeconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

   public void print(String iMessage) {
    	
    	System.out.println(iMessage);
    }
   
//    protected static String getEmailString() {
    public String getEmailString() {
	    String CHARS = "1234567890";
	    StringBuilder charRandom = new StringBuilder();
	    Random rnd = new Random();
	    while (charRandom.length() < 10) { // length of the random string.
	    int index = (int) (rnd.nextFloat() * CHARS.length());
	    charRandom.append(CHARS.charAt(index));
	    }
	    String emailString = "hasmukh." +charRandom.toString()+"@whistle.com";
	    return emailString;

    }
    
    public Date getSubscriptionEndDate(int noOfMonth) {
    	Calendar calendar = Calendar.getInstance();
        System.out.println("Current Date = " + calendar.getTime());

        // Add 8 months to current date
        calendar.add(Calendar.MONTH, noOfMonth);
        Date result = calendar.getTime();
        
        System.out.println("Updated Date = " + calendar.getTime());
        return result;
        
    }
    
    public void scrollToText(String visibleText) {
    	MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))."
    			+ "scrollIntoView(new UiSelector().textContains("+visibleText+").instance(0))");
    }
    
    public void scrollToElementDown(MobileElement element){
        if (BaseTest.sConfigPlatform.contentEquals("android")) {
           //isElementVisibleOnMobilePage
            int iScrollTimes=30;

            for(int j=0;j<iScrollTimes;j++) {
            	
            	try {
            		if(element.isDisplayed()) {
            			break;
            		}
            	}catch(Exception e) {
            		scrollDown();
            	}
            	
            }
        }
    }
    
	public void iOSActionScrollUp() {

		System.out.println("Scroll start");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", "up");
		js.executeScript("mobile: scroll", scrollObject);
		
		System.out.println("Scroll end");

	}
	
    public void scrollToElementUp(MobileElement element){
        if (BaseTest.sConfigPlatform.contentEquals("android")) {
           //isElementVisibleOnMobilePage
            int iScrollTimes=3;

            for(int j=0;j<iScrollTimes;j++) {
            	
            	try {
            		if(element.isDisplayed()) {
            			break;
            		}
            	}catch(Exception e) {
					ScrollUp();
            	}
            	
            }
        }
        else
		{
			System.out.println("iOS Scrolling Block");
			while(!element.isDisplayed())
			{
				ScrollUp();
			}
		}
    }
    
    public boolean tapOut() {
    	if(BaseTest.sConfigPlatform.contentEquals("ios")) 
    	{
    		new TouchAction((PerformsTouchActions) driver)
    		.press(PointOption.point(100, 100))
    		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
    		.release().perform();
    		return true;
    	}
    	else
    	{
            try {
                new TouchAction((MobileDriver) driver).press(PointOption.point(300,300)).perform();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    	}
    }

    public boolean tapXYCordinateAndroid() {
        try {
            new TouchAction((MobileDriver) driver).press(PointOption.point(300,300)).perform();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
