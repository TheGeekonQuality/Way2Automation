package com.way2automation.demo.tests;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.way2automation.common.LocatorsForTests;
import com.way2automation.common.generalMethods;

public class AutomateWay2Automation {

	public static WebDriver driver;
	List<WebElement> navigationList;
	static String username = "G43MOY7ODK";
	static String password = "Challenge@321";
	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	ExtentTest test;

	@BeforeTest
	public void startReport() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/testReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Extent Report Demo");
		htmlReporter.config().setReportName("Test Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
	}

	@BeforeMethod
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://www.way2automation.com/demo.html");
		driver.manage().window().maximize();
		navigationList = driver.findElements(By.xpath(LocatorsForTests.navigationListXpath));

	}

	@Test(alwaysRun = true, enabled = true, priority = 1)
	public void registration() throws InterruptedException {
		test = extent.createTest("Registration");

		navigationList.get(5).click();
		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));
		generalMethods.waitForAjax(driver);
		List<WebElement> registrationFields = driver.findElements(By.xpath(LocatorsForTests.registrationFieldsXpath));
		test.log(Status.PASS, "Entering the registration details");
		registrationFields.get(0).sendKeys("Mr. Random Number");
		registrationFields.get(1).sendKeys("9988776655");
		registrationFields.get(2).sendKeys(generalMethods.randomAlphaNumeric(5) + "@gmail.com");
		registrationFields.get(3).sendKeys("Bangalore");
		username = generalMethods.randomAlphaNumeric(10);
		registrationFields.get(4).sendKeys(username);
		registrationFields.get(5).sendKeys(password);
		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSubmitXpath)).click();
		Thread.sleep(5000);
		test.log(Status.PASS, "Registration Completed");
	}

	@Test(enabled = true, priority = 2)
	public void dynamicElements() throws InterruptedException {
		test = extent.createTest("dynamicElements");

		test.log(Status.PASS, "Login into the application using the credentials used in Registration Test Case");
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath(LocatorsForTests.registrationTabXpath))).click().build()
				.perform();
		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));
		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSigninXpath)).click();
		generalMethods.waitForAjax(driver);
		List<WebElement> signInFields = driver.findElements(By.xpath(LocatorsForTests.signInXpaths));
		signInFields.get(0).sendKeys(username);
		signInFields.get(1).sendKeys(password);
		driver.findElement(By.xpath(LocatorsForTests.signInButtonXpath)).click();
		test.log(Status.PASS, "logged into the application");
		Thread.sleep(5000);
		driver.navigate().refresh();
		generalMethods.waitForAjax(driver);
		test.log(Status.PASS, "navigate to Dropdown option");
		act.clickAndHold(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]")))
				.moveToElement(driver.findElement(By.xpath(LocatorsForTests.dropDownXpath))).click().build().perform();

		Thread.sleep(5000);
		WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);
		Select countryList = new Select(driver.findElement(By.xpath(LocatorsForTests.selectCountryXpath)));
		countryList.selectByValue("India");
		Thread.sleep(3000);
		// String selectedCountry = countryList.getFirstSelectedOption().getText();
		Thread.sleep(3000);
		Assert.assertTrue(countryList.getFirstSelectedOption().getText().equalsIgnoreCase("India"));

		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.findElement(By.xpath(LocatorsForTests.dropdownTabsXpath)).click();
		Thread.sleep(3000);
		iframe = driver.findElements(By.tagName("iframe")).get(1);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);
		WebElement enterCountry = driver.findElement(By.xpath(LocatorsForTests.enterCountryXpath));
		enterCountry.clear();
		enterCountry.sendKeys("Argentina");
		Thread.sleep(3000);

		// this is failing as not able to get the text that i have entered.
		Assert.assertEquals(driver.findElement(By.xpath("//*[@class='custom-combobox']/input")).getText(), "Argentina");
		test.log(Status.PASS, "Dropdown functionality tested successfully");
	}

	@Test(enabled = true, priority = 3)
	public void WidgetsMenuTabs() throws InterruptedException {
		test = extent.createTest("WidgetsMenuTabs");
		Actions act = new Actions(driver);
		act.clickAndHold(navigationList.get(4))
				.moveToElement(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]//li[1]/a"))).click().build()
				.perform();

		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));
		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSigninXpath)).click();
		generalMethods.waitForAjax(driver);
		List<WebElement> signInFields = driver.findElements(By.xpath(LocatorsForTests.signInXpaths));
		signInFields.get(0).sendKeys(username);
		signInFields.get(1).sendKeys(password);
		driver.findElement(By.xpath(LocatorsForTests.signInButtonXpath)).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		generalMethods.waitForAjax(driver);
		act.clickAndHold(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]")))
				.moveToElement(driver.findElement(By.xpath(LocatorsForTests.WidgetTabsXpath))).click().build()
				.perform();

		Thread.sleep(5000);
		WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);

		List<WebElement> listOfTabs = driver.findElements(By.xpath("//div[@id='tabs']//li"));
		Thread.sleep(3000);
		listOfTabs.get(0).click();
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id='tabs-1']/p")).getText().startsWith("Proin elit arcu"));
		Thread.sleep(3000);
		listOfTabs.get(1).click();
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id='tabs-2']/p")).getText().startsWith("Morbi tincidunt"));
		Thread.sleep(3000);
		listOfTabs.get(2).click();
		Assert.assertTrue(
				driver.findElement(By.xpath("//*[@id='tabs-3']/p[1]")).getText().startsWith("Mauris eleifend est et"));
		Thread.sleep(3000);

		test.log(Status.PASS, "Widgets Menu Tabs Tested Succesfully");
	}

	@Test(enabled = true, priority = 4)
	public void Alerts() throws InterruptedException {
		test = extent.createTest("Alerts");
		Actions act = new Actions(driver);
		act.clickAndHold(navigationList.get(4))
				.moveToElement(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]//li[1]/a"))).click().build()
				.perform();

		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));
		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSigninXpath)).click();
		generalMethods.waitForAjax(driver);
		List<WebElement> signInFields = driver.findElements(By.xpath(LocatorsForTests.signInXpaths));
		signInFields.get(0).sendKeys(username);
		signInFields.get(1).sendKeys(password);
		driver.findElement(By.xpath(LocatorsForTests.signInButtonXpath)).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		generalMethods.waitForAjax(driver);
		act.clickAndHold(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]")))
				.moveToElement(driver.findElement(By.xpath(LocatorsForTests.AlertsXpath))).click().build().perform();

		Thread.sleep(5000);
		WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/button")).click();
		Thread.sleep(3000);
		Assert.assertEquals(driver.switchTo().alert().getText(), "I am an alert box!");
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
		Thread.sleep(3000);

		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@class='responsive-tabs']/li[2]")).click();
		Thread.sleep(3000);
		iframe = driver.findElements(By.tagName("iframe")).get(1);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/button")).click();
		Thread.sleep(3000);
		driver.switchTo().alert().sendKeys("Test Name");
		Thread.sleep(3000);
		driver.switchTo().alert().accept();
		Thread.sleep(3000);
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id='demo']")).getText().contains("Test Name"));
		test.log(Status.PASS, "Alerts Tested Succesfully");
	}

	@Test(enabled = true, priority = 5)
	public void WidgetDatePicker() throws InterruptedException {
		test = extent.createTest("WidgetDatePicker");
		Actions act = new Actions(driver);
		act.clickAndHold(navigationList.get(4))
				.moveToElement(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]//li[1]/a"))).click().build()
				.perform();

		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));
		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSigninXpath)).click();
		generalMethods.waitForAjax(driver);
		List<WebElement> signInFields = driver.findElements(By.xpath(LocatorsForTests.signInXpaths));
		signInFields.get(0).sendKeys(username);
		signInFields.get(1).sendKeys(password);
		driver.findElement(By.xpath(LocatorsForTests.signInButtonXpath)).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		generalMethods.waitForAjax(driver);
		act.clickAndHold(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]")))
				.moveToElement(driver.findElement(By.xpath(LocatorsForTests.WidgetDatePickerXpath))).click().build()
				.perform();

		Thread.sleep(5000);
		WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
		driver.switchTo().frame(iframe);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id='datepicker']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/a[1]/span")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@class='ui-datepicker-calendar']//tr[3]/td[3]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@id='datepicker']")).click();
//		String selectedDate = driver.findElement(By.xpath("//input[@id='datepicker']")).getText();

		// THis one is failing as well. Somehow not able to get the entered date. 
		Assert.assertTrue((driver.findElement(By.xpath("//*[@id='datepicker']")).getText()).equalsIgnoreCase("07/14/2020"));

	}

	@Test(enabled = true, priority = 6)
	public void FramesAndWindows() throws InterruptedException {
		test = extent.createTest("FramesAndWindows");
		Actions act = new Actions(driver);
		act.clickAndHold(navigationList.get(4))
				.moveToElement(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]//li[1]/a"))).click().build()
				.perform();

		ArrayList<String> mainTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(mainTabs.get(1));

		Thread.sleep(5000);
		driver.findElement(By.xpath(LocatorsForTests.registrationPageSigninXpath)).click();
		generalMethods.waitForAjax(driver);
		List<WebElement> signInFields = driver.findElements(By.xpath(LocatorsForTests.signInXpaths));
		signInFields.get(0).sendKeys(username);
		signInFields.get(1).sendKeys(password);
		driver.findElement(By.xpath(LocatorsForTests.signInButtonXpath)).click();
		Thread.sleep(5000);
		driver.navigate().refresh();
		generalMethods.waitForAjax(driver);
		act.clickAndHold(driver.findElement(By.xpath("//*[@id='toggleNav']/li[5]")))
				.moveToElement(driver.findElement(By.xpath(LocatorsForTests.FramesandWindowsXpath))).click().build()
				.perform();

		Thread.sleep(5000);
		WebElement iframe = driver.findElements(By.tagName("iframe")).get(0);
		driver.switchTo().frame(iframe);
		Thread.sleep(5000);
		driver.findElement(By.xpath("/html/body/div/p/a")).click();
		Thread.sleep(5000);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		Thread.sleep(3000);
		driver.switchTo().window(tabs.get(2));
		Thread.sleep(3000);
		Assert.assertTrue(
				(driver.findElement(By.xpath("/html/body/div/p/a")).getText()).equalsIgnoreCase("New Browser Tab"));
		test.log(Status.PASS, "FramesAndWindows Tested Succesfully");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
			test.fail(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
		} else {
			test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
			test.skip(result.getThrowable());
		}
		extent.flush();

		driver.quit();
	}

}
