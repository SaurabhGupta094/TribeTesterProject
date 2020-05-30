package utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class WaitUntil {
	WebDriver driver;
	private static FluentWait<WebDriver> wait = null;
	public static FluentWait<WebDriver> shortWait = null;
	private static int short_wait_limit = 3;
	private static int long_wait_limit = 300;
	public WaitUntil(WebDriver driver) {
		this.driver = driver;
	}
	public void initWaitUntil() {
		if (wait == null) {
			wait = new WebDriverWait(driver, long_wait_limit).pollingEvery(250, TimeUnit.MILLISECONDS)
					.ignoring(StaleElementReferenceException.class);
			shortWait = new WebDriverWait(driver, short_wait_limit).pollingEvery(250, TimeUnit.MILLISECONDS)
					.ignoring(StaleElementReferenceException.class);
		}
	}

	public static boolean isElementDisplayed(By by) {
		try {
			if (shortWait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed())
				return true;
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return false;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return false;
		}
		return false;
	}

	public static boolean isMobileElementNotDisplayed(By by) {
		try {
			if (wait.until(ExpectedConditions.invisibilityOfElementLocated(by)))
				return true;
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return false;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return false;
		}

		return false;
	}

	public MobileElement waitForMobileElementToBeClickable(By by, int milliSeconds) {
		try {
			FluentWait<WebDriver> tempWait = (WebDriverWait) new WebDriverWait(driver, milliSeconds)
					.pollingEvery(250, TimeUnit.MILLISECONDS).ignoring(StaleElementReferenceException.class);

			return (MobileElement) tempWait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return null;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return null;
		}
	}

	public static MobileElement waitForMobileElementToBeClickable(By by) {
		try {
			return (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return null;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return null;
		}
	}

	public static boolean waitAndTapOnMobileElementByXpath(String xpath) {
		try {
			MobileElement e = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(ByXPath.xpath(xpath)));
			e.click();
			return true;
		} catch (NoSuchElementException nse) {
			System.out.println("No such element found !");
			return false;
		} catch (TimeoutException toe) {
			System.out.println("Timeout exception cought !");
			return false;
		}
	}
}
