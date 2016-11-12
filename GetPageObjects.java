/*
 * 
 * User: knigam
 * Date: 11/12/2016
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates Page Objects for the page navigated to
 */
public class GetPageObjects {

    private static final Logger logger = LoggerFactory.getLogger(GetPageObjects.class);
    
    String username = "email";
    String password = "Password1#";
    String[] tags = {"h1", "h2", "input", "button", "select", "a", "textarea"};
    boolean excludeDisplayNone = true;
    WebDriver driver;

    @Test
    public void getObjects() {
        //initiate the driver and navigate to the page
        String pageName = "nameofthepage";
        //Get all elements
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int count = 0;
        String html = driver.getPageSource();
        String exp = "<img src=\"/img/loading.gif\" alt=\"loading\" title=\"loading, please wait.....\" />";
        while (html.contains(exp) && count < 5) {
            driver.navigate().refresh();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            html = driver.getPageSource();
            count++;
        }
        parseHtml(html, pageName);
    }

    private void parseHtml(String html, String pageName) {

        Document doc = Jsoup.parse(html);
        List<String> locatorDeclaration = new ArrayList<>();
        List<String> methodDeclaration = new ArrayList<>();
        Element mainElement = doc.getElementById("tabbed-content");
        for (String tag : tags) {
            Elements elements = mainElement.getElementsByTag(tag);
            ele:
            for (Element elem : elements) {
                String txt = elem.text();
                if (txt.equals(""))
                    txt = elem.attr("name");
                String typeAttr = elem.attr("type");
                String element_name = getVarNameInCamelCase(txt, tag, typeAttr);
                if (elem.hasAttr("href"))
                    element_name = element_name.split("_")[0] + "_link";
                String element_locator = "";
                String element_method = "";
                Attributes att = elem.attributes();
                if (att.size() == 0) {
                    element_locator = "@FindBy(xpath=\"//" + tag + "[contains(text(),'" + txt + "')]\")\n" +
                            "WebElement " + element_name + ";\n\n";
                } else if (att.hasKey("id")) {
                    if (elem.attr("id").startsWith("footer"))
                        continue;
                    if (element_name.startsWith("_") || tag.equals("select"))
                        element_name = getVarNameInCamelCase(elem.attr("id"), tag, typeAttr);
                    else if (element_name.endsWith("_span") && elem.attr("id").contains("arrow"))
                        element_name = element_name.replace("_span", "_link");
                    element_locator = "@FindBy(id=\"" + elem.attr("id") + "\")\n" +
                            "WebElement " + element_name + ";\n\n";
                } else if (att.hasKey("name")) {
                    if (element_name.startsWith("_") || tag.equals("select"))
                        element_name = getVarNameInCamelCase(elem.attr("name"), tag, typeAttr);
                    element_locator = "@FindBy(name=\"" + elem.attr("name") + "\")\n" +
                            "WebElement " + element_name + ";\n\n";
                } else if (att.hasKey("href")) {
                    if (elem.attr("href").equals("/supportportal"))
                        continue;
                    if (element_name.startsWith("_") || tag.equals("select"))
                        continue ele;
                    element_locator = "@FindBy(linkText=\"" + elem.text() + "\")\n" +
                            "WebElement " + element_name + ";\n\n";
                } else if (att.hasKey("class")) {
                    if (element_name.startsWith("_") || tag.equals("select"))
                        element_name = getVarNameInCamelCase(elem.attr("class"), tag, typeAttr);
                    element_locator = "@FindBy(className=\"" + elem.attr("class") + "\")\n" +
                            "WebElement " + element_name + ";\n\n";
                } else {
                    continue;
                }
                String methodName = element_name.substring(0, 1).toUpperCase() + element_name.substring(1, element_name.length()).split("_")[0];
                if (element_name.contains("_header")) {
                    element_method = "/**\n" +
                            " * Get Header value for " + element_name + " \n" +
                            "*/\n" +
                            "public String get" + methodName + "PageHeader(){\n" +
                            "        return " + element_name + ".getText();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_select")) {
                    element_method = "/**\n" +
                            " * Select value in " + element_name + " \n" +
                            "*/\n" +
                            "public void select" + methodName + "(String value){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "selectByVisibleText(" + element_name + ",value);\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "}\n\n";
                } else if (element_name.contains("_btn")) {
                    element_method = "/**\n" +
                            " * Click " + element_name + " \n" +
                            "*/\n" +
                            "public void click" + methodName + "(){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "        " + element_name + ".click();\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_chkbox")) {
                    element_method = "/**\n" +
                            " * Set " + element_name + " \n" +
                            "*/\n" +
                            "public void click" + methodName + "(){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "setCheckBox(" + element_name + ");\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n" +
                            "/**\n" +
                            " * UnSet " + element_name + " \n" +
                            "*/\n" +
                            "public void click" + methodName + "(){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "unsetCheckBox(" + element_name + ");\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_radio")) {
                    element_method = "/**\n" +
                            " * Click " + element_name + " \n" +
                            "*/\n" +
                            "public void click" + methodName + "(){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            element_name + ".click();\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_txt")) {
                    element_method = "/**\n" +
                            " * Enter text in " + element_name + " \n" +
                            "*/\n" +
                            "public void enter" + methodName + "(String value){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "        " + element_name + ".sendKeys(value+Keys.TAB);\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_txtarea")) {
                    element_method = "/**\n" +
                            " * Enter text in " + element_name + " \n" +
                            "*/\n" +
                            "public void enter" + methodName + "(String value){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "        " + element_name + ".sendKeys(value+Keys.TAB);\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else if (element_name.contains("_link")) {
                    element_method = "/**\n" +
                            " * Click " + element_name + " \n" +
                            "*/\n" +
                            "public void click" + methodName + "(){\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "        " + element_name + ".click();\n" +
                            "waitForLiftAjaxToComplete();\n" +
                            "    }\n\n";
                } else {
                    continue;
                }
                locatorDeclaration.add(element_locator);
                methodDeclaration.add(element_method);
            }
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(pageName + ".java");
            writer.write(
                    "import org.openqa.selenium.*;\n" +
                    "import org.openqa.selenium.support.FindBy;\n" +
                    "import org.openqa.selenium.support.PageFactory;\n" +
                    "import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;\n" +
                    "import org.slf4j.Logger;\n" +
                    "import org.slf4j.LoggerFactory;\n\n");
            writer.write("public class " + pageName + " extends AbstractPage{\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "private static final Logger logger = LoggerFactory.getLogger(" + pageName + ".class);\n" +
                    "WebDriver driver;\n" +
                    " int timeout = 30;\n" +
                    " int pollingTime = 5;\n\n" +
                    " public " + pageName + "(WebDriver driver)\n" +
                    "{\n" +
                    "super(driver);\n" +
                    "  this.driver = driver;\n" +
                    "  //This initElements method will create all WebElements\n" +
                    "    PageFactory.initElements(new AjaxElementLocatorFactory(this.driver, timeout),this);\n" +
                    "}\n\n");
            for (String str : locatorDeclaration) {
                writer.write(str);
            }
            writer.write("/**\n" +
                    " * Get title of the webpage\n" +
                    "*/\n" +
                    "public String getTitle()\n" +
                    "{\n" +
                    "try {\n" +
                    "return this.driver.getTitle();\n" +
                    "} catch (Exception e) {\n" +
                    "logger.error(\"Title can not be found \", e);\n" +
                    "}\n" +
                    "return null;\n" +
                    "}\n\n");
            for (String str : methodDeclaration) {
                writer.write(str);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getVarNameInCamelCase(String txt, String tag, String type) {

        if (tag.equals("h1") || tag.equals("h2"))
            tag = "header";
        else if (tag.equals("button"))
            tag = "btn";
        else if (tag.equals("select"))
            tag = "select";
        else if (type.equalsIgnoreCase("checkbox"))
            tag = "chkbox";
        else if (type.equalsIgnoreCase("radio"))
            tag = "radio";
        else if (type.equalsIgnoreCase("text"))
            tag = "txt";
        else if (type.equalsIgnoreCase("textarea"))
            tag = "txtarea";
        txt = txt + "_" + tag;
        String[] tmp = txt.split(" ");
        String name = "";
        for (int i = 0; i < tmp.length; i++) {
            String s = tmp[i];
            if (i == 0) {
                String a = s.substring(0, 1).toLowerCase();
                String b = s.substring(1, s.length());
                s = a + b;
            } else {
                String a = s.substring(0, 1).toUpperCase();
                String b = s.substring(1, s.length());
                s = a + b;
            }
            name = name + s;
        }
        return name;
    }
}
