package testCases;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.assignment.operations.*;
import com.assignment.excelReader.*;

public class TestExecutor {
	WebDriver driver = null;

	@BeforeClass
	public void Setup() {

		System.out.println("Initiating Execution");

	}

	// provides data for tests multiple excel files can be created
	@DataProvider(name = "dataprov")
	public static Object[][] primeNumbers() {
		return new Object[][] { { "TestCase1.xls", "ie" },
				{ "TestCase1.xls", "firefox" } };
	}

	// Actual test Run
	@Test(dataProvider = "dataprov")
	public void Test(String excelfilename, String browser) throws Exception {

		switch (browser) {

		case "ie":
			// initiating ie driver
			System.out.println("Tests running on internet explorer");
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();

			break;

		case "firefox":
			// initiating the firefox driver
			driver = new FirefoxDriver();
			System.out.println("Tests running on firefox");

			break;

		default:
			// default behavior

			System.out.println("Please enter an appropriate browser");
			break;
		}

		System.out.println(browser + " initiated");

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// Reading Excel columns and implementation of logic from different
		// objects

		ExcelReader file = new ExcelReader();
		ObjectReader object = new ObjectReader();
		Properties allObjects = object.getObjectRepository();
		ObjectOperations objops = new ObjectOperations(driver);
		Sheet testsheet = file.readExcel(System.getProperty("user.dir")
				+ "\\TestCaseExcel\\", excelfilename, "Sheet1");

		if (testsheet != null) {
			System.out.println("getting testsheet");
		}

		int rowCount = testsheet.getLastRowNum() - testsheet.getFirstRowNum();

		for (int i = 1; i < rowCount + 1; i++) {

			System.out.println("Row:" + i + "________________________");
			Row row = testsheet.getRow(i);

			if (row.getCell(0,
					org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK)
					.toString().length() > 0) {
				System.out.println("Executing module--->"
						+ row.getCell(0).toString() + " Started");
			}

			else {
				objops.perform(
						allObjects,
						row.getCell(
								1,
								org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK)
								.toString(),
						row.getCell(
								2,
								org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK)
								.toString(),
						row.getCell(
								3,
								org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK)
								.toString(), row.getCell(4,

						org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK)
								.toString());

			}

		}

	}

	// quitting driver in the end of the testcase
	@AfterClass
	public void destroy() {
		driver.quit();
	}

}
