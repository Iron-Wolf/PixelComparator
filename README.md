# PixelComparator
[![Dependencies](https://img.shields.io/librariesio/github/Iron-Wolf/PixelComparator.svg)](https://libraries.io/github/Iron-Wolf/PixelComparator) 
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/Iron-Wolf/PixelComparator.svg)](http://isitmaintained.com/project/Iron-Wolf/PixelComparator "Average time to resolve an issue")  

Detect nearest color from each pixel from a pre-pixelated picture

## Download SDK
Open-jdk is accessible on the [official website](https://jdk.java.net/).  
JavaFX is hosted on [Gluon](https://gluonhq.com/products/javafx/).  

## Compile & Debug
To compile the project with IntelliJ :
 - Check out the project from github
 - Configure the source folder in the [Content Root](https://www.jetbrains.com/help/idea/content-roots.html)
 - Build (choose one)
   - Maven : Create a Maven configuration with `javafx:run` (and `Build` before launch)
   - Application with VM options : `-p </path/jafx-sdk>/lib --add-modules javafx.controls,javafx.fxml,javafx.swing`
 - Run the configuration

Make sure you have the JDK referenced in the project settings.  

### JavaFX with Maven
This project is configured with JavaFX as a Maven dependency.  
The actual JavaFX situation with Maven is :
 - Use the [com.zenjava](https://github.com/openjfx/javafx-maven-plugin) maven plugin with Java 8 or 9
 - Use Java 8 or 9 and don't specify maven dependecy for JavaFX
 - Use the [org.openjfx](https://github.com/openjfx/javafx-maven-plugin) with Java 11+  

Source on [stackoverflow](https://stackoverflow.com/a/15283999)  

> Reminder  
As of OpenJDK-8/JDK-11, JavaFX is no longer included in the base JDK.  
You will need to install the *openjfx* package.  
Or make sure that your JDK contains this file : `<Java SDK root>/jre/lib/ext/jfxrt.jar`.  


## Package & Run
With Java 8 or 9, package the application in a simple 
[jar artifact](https://www.jetbrains.com/help/idea/creating-and-running-your-first-java-application.html#package) 
to access it outside of the IDE.  

Extra step for Java 11+ :  
You will need the JavaFX SDK libraries, corresponding to your platform.  
Then, you will be able to launch the app with this command : 
```java
java -p javafx-sdk-11.0.2/lib \
--add-modules javafx.controls,javafx.fxml,javafx.swing \
-jar pixel_comparator.jar
```

## IntelliJ Troubleshooting

> Error:java: error: release version 5 not supported  

Force target Maven version to the JDK version (default is 1.5).  
Change the source/target configuration in the POM.  
https://stackoverflow.com/a/12900859  

> IllegalAccessError with FXMLLoaderHelper  

IntelliJ doesn't load all JavaFX module by default.  
Add VM options : https://stackoverflow.com/a/54292408  
Or run Maven goal `javafx:run`  

Complete answer : https://stackoverflow.com/a/52470141


