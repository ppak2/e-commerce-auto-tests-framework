package _pages.enums;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.Predicate;

import static io.vavr.API.*;


public enum PlatformType {

    Win,
    MacOS,
    Linux,
    iOs,
    Android;


    public static PlatformType definePlatformType(){

        String platformName;

        if(WebDriverRunner.getWebDriver() instanceof AppiumDriver)  {
            platformName = ((AppiumDriver) WebDriverRunner.getWebDriver()).getPlatformName().toLowerCase();
        }
        else  {

            Object o = ((RemoteWebDriver) WebDriverRunner.getWebDriver()).getCapabilities().getCapability("platform");
            platformName = (o).toString().toLowerCase();
        }

        return Match(platformName).of(Case($(isWindows), Win),
                Case($(isMac), MacOS),
                Case($(isIPhone), iOs),
                Case($(isAndroid), Android),
                Case($(), Linux));
    }

    private static Predicate<String> isWindows = val->val.startsWith("win") || val.equals("xp");
    private static Predicate<String> isMac = val->val.startsWith("mac");
    private static Predicate<String> isIPhone = val->val.equals("ios");
    private static Predicate<String> isAndroid = val->val.equals("android");
    private static Predicate<String> isLinux = val->val.equals("linux");

}
