package _pages.Actions;

import _pages.enums.PlatformType;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;

import java.util.function.Predicate;

import static _pages.enums.PlatformType.Android;
import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static repository.ElementSelectors.CheckOutPageSelectors.ssnField;

public abstract class Actions {

    @Getter
    @Setter
    private PlatformType platformType;

    @Getter
    @Setter
    private WebDriver webDriver;

    private Actions(){}

    Actions(PlatformType platformType){
        this.platformType = platformType;
        this.webDriver = WebDriverRunner.getWebDriver();
    }

    public static Actions initActionsObject(PlatformType platformType){
        return isMobile.test(platformType) ? new MobileActions(platformType) : new DesktopActions(platformType);
    }

    public abstract void click(SelenideElement element);

    public abstract void hoverAndClick(SelenideElement element);

    public abstract void sendKeys(SelenideElement textField, String textToType);

    public abstract void sendKeysEnter(SelenideElement textField, String textToType);

    public abstract void sendKeysTab(SelenideElement textField, String textToType);

    public abstract void checkForCookieButton();

    public abstract void checkForIFrame();

    public abstract void handleTranslatePopUp();

    public abstract void scrollPageToLoad();

    public void iOsSsnFieldType(AppiumDriver appiumDriver, String ssn){
        ssnField.click();
        appiumDriver.getKeyboard().sendKeys(ssn);
        ssnField.pressEnter();
    }

    void markActiveElement(SelenideElement element){
        executeJavaScript("arguments[0].style.border='3px dotted red'", element);
    }

    public void scrollToFooter(){
        $x("//footer").scrollIntoView(false);
    }

    private static Predicate<PlatformType> isMobile = platform -> platform.equals(iOs) || platform.equals(Android);
}
