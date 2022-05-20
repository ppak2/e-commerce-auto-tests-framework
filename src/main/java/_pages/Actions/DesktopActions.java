package _pages.Actions;

import _pages.enums.PlatformType;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;
import static repository.ElementSelectors.IndexPageSelectors.cookieButton;
import static repository.ElementSelectors.IndexPageSelectors.iFrameCloseButton;

public class DesktopActions extends Actions {


    DesktopActions(PlatformType platformType) {
        super(platformType);
    }

    @Override
    public void click(SelenideElement element) {

        element.shouldBe(Condition.visible);
        element.scrollIntoView(false);
        executeJavaScript("arguments[0].click()", element);
    }

    @Override
    public void hoverAndClick(SelenideElement element) {
        actions().moveToElement(element).click().build().perform();
    }

    @Override
    public void sendKeys(SelenideElement textField, String textToType) {

        textField.shouldBe(Condition.visible);
        textField.scrollIntoView(false);
        textField.clear();
        textField.setValue(textToType);
    }

    @Override
    public void sendKeysEnter(SelenideElement textField, String textToType) {

        sendKeys(textField, textToType);
        textField.pressEnter();
    }

    @Override
    public void sendKeysTab(SelenideElement textField, String textToType) {

        sendKeys(textField, textToType);
        textField.pressTab();
    }

    @Override
    public void checkForCookieButton() {
        if (cookieButton.isDisplayed()) cookieButton.click();
    }

    @Override
    public void checkForIFrame() {
        if (iFrameCloseButton.isDisplayed()) iFrameCloseButton.click();
    }

    @Override
    public void handleTranslatePopUp() {}

    @Override
    public void scrollPageToLoad() {
        Number result = executeJavaScript("return document.body.scrollHeight");
        int height = result.intValue();
        executeJavaScript("scroll(0,"+height*0.4+")");
        sleep(2000);
        executeJavaScript("scroll(0,"+height*0.6+")");
        sleep(2000);
        executeJavaScript("scroll(0,"+height*0.8+")");
    }
}
