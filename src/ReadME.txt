-----------------------Release Notes--------------------------------------

Import the project into eclipse and add the following as external libraries (also provided within the project directory)

#Selenium-server-standalone-2.53.0.jar (latest)
#poi-3.14-beta1-20151223.jar (apache poi for excel operations)
#testng Framework (installed using eclipse market place)


All the other things are being provided within the structure

The framework uses selenium with TestNG to create a hybrid framework which can be easily used by adding :

--> key value pairs in property files
--> action in Excel files

the framework is extensive and can be used over any other website although customization are being made for the specific target PTC

I could have also do it with pagefactory and POM in selenium however , in terms of scalabilty, reliabilty and ease of use the hybrid framework approch seemed the most soothing.
As it required browser specific testing and not os i have not included selenium grid, or mobile devices ; i however have extensive knowledge over selenium grid, appium and selendroid

<-------------------------------Basic Usage Guide----------------------------->

multiple excels can be added in the data provider in the TestExecutor.java file named "dataprov".
As the framework supports two browsers(as per problem statement can be scaled for all browsers) the same can be added in "data prov"
the logic for initiating browsers and delegating is inside the "testcases.TestExecutor.java"
Key value pair of objects and their value must be kept in objectProperty/object.properties
Key words and actions and module names can be added via TestCaseExcel director as Excels; I have already added 1 as per problem description

All other basic reading executing logic are kept in respective folder and can be seen as classes however not necessary for execution of the framework

ENJOY!!!! :)
