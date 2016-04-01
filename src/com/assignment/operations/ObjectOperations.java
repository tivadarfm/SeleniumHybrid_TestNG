package com.assignment.operations;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ObjectOperations {

	WebDriver driver;
	String temp;

	public ObjectOperations(WebDriver driver) {
		this.driver = driver;
	}

	public void perform(Properties p, String operation, String objectName,
			String objectType, String value) throws Exception {
		System.out.println("");

		switch (operation.toUpperCase()) {

		case "GOTOURL":
			// Get url of application
			driver.get(p.getProperty(value));
			break;

		case "CLICK":
			// Perform click
			driver.findElement(this.getObject(p, objectName, objectType))
					.click();
			break;

		case "CLEAR":
			// Clear a field(especially before setting text)
			driver.findElement(this.getObject(p, objectName, objectType))
					.clear();
			break;

		case "SELBYVISIBLE":
			// Selecting an element by its visible text
			new Select(driver.findElement(this.getObject(p, objectName,
					objectType))).selectByVisibleText(value);

			break;

		case "SLEEP":
			//Sleep for STale element reference exception
			System.out.println("sleep" + value);
			Thread.sleep(Integer.parseInt(value) * 1000);
			break;

		

		case "SETTEXT":
			// Set text on control
			driver.findElement(this.getObject(p, objectName, objectType))
					.sendKeys(value);
			break;

		case "GETTEXT":
			// Get text of an element
			driver.findElement(this.getObject(p, objectName, objectType))
					.getText();
			break;

		case "MAX":

			// Maximizing window
			driver.manage().window().maximize();
			break;

		case "SWITCHTODEFAULT":
			// getting back to the default fram

			driver.switchTo().defaultContent();
			break;

		case "SWITCHFRAME":
			// Switching Frame
			driver.switchTo().frame(
					driver.findElement(this
							.getObject(p, objectName, objectType)));
			break;

		case "VERIFYTITLE":
			//Verifying title of the page
			if (!driver.getTitle().equals(value)) {
				throw new Exception("Title is incorrect" + " got "
						+ driver.getTitle() + "expected: " + value);
			}

			break;
			
			
		case "STORE":
			
			//Storing a web Element
			temp=driver.findElement(this.getObject(p, objectName, objectType)).getText();
			
			System.out.println("STORE :"+temp.toString()+"  done");
			break;
			
		case "CHECKSTORED":
			
			
			System.out.println("Checkstored :"+driver.findElement(this.getObject(p, objectName, objectType)).getText()+"  done");
			//checking a web element
			if(temp .equals(driver.findElement(this.getObject(p, objectName, objectType)).getText()))
			{
				System.out.println("Elements are same");
				
			}
			else{
				throw new Exception("The Web Element is not the same");
			}
			break;

		case "VERIFYCLASS":
			//verifying class for an element value

			if (!(driver.findElement(this.getObject(p, objectName, objectType))
					.getAttribute("class").contains(value))) {
				throw new Exception(
						"Class doesnt contain the specified value:  " + value);

			}

			break;

		default:

			System.out.println("Please provide correct Keyword");
			break;
		}
	}

	/**
	 * Find element BY using object type and value
	 * 
	 * @param p
	 * @param objectName
	 * @param objectType
	 * @return
	 * @throws Exception
	 */
	private By getObject(Properties p, String objectName, String objectType)
			throws Exception {

		System.out.println(p.getProperty(objectName));
		System.out.println(objectName);

		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) {

			return By.xpath(p.getProperty(objectName));
		}
		// find by class
		else if (objectType.equalsIgnoreCase("CLASSNAME")) {

			return By.className(p.getProperty(objectName));

		}
		// find by id
		else if (objectType.equalsIgnoreCase("ID")) {
			return By.id(p.getProperty(objectName));
		}

		// Find by tagname
		else if (objectType.equalsIgnoreCase("TAGNAME")) {
			return By.tagName(p.getProperty(objectName));
		}
		// find by name
		else if (objectType.equalsIgnoreCase("NAME")) {

			return By.name(p.getProperty(objectName));

		}
		// Find by css
		else if (objectType.equalsIgnoreCase("CSS")) {

			return By.cssSelector(p.getProperty(objectName));

		}
		// find by link
		else if (objectType.equalsIgnoreCase("LINK")) {

			return By.linkText(p.getProperty(objectName));

		}
		// find by partial link
		else if (objectType.equalsIgnoreCase("PARTIALLINK")) {

			return By.partialLinkText(p.getProperty(objectName));

		} else {
			throw new Exception("Wrong object type");
		}
	}
}
