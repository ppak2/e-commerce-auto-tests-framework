package _pages.Actions;

import _pages.enums.PlatformType;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.remote.HideKeyboardStrategy;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static _pages.enums.PlatformType.Android;
import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.CheckOutPageSelectors.ssnField;
import static repository.ElementSelectors.IndexPageSelectors.*;

@Log
public class MobileActions extends Actions {

    private AppiumDriver appiumDriver;

    MobileActions(PlatformType platformType) {
        super(platformType);
        appiumDriver = (AppiumDriver) getWebDriver();
    }

    @Override
    public void click(SelenideElement element) {

        element.shouldBe(Condition.visible);
        element.scrollIntoView(false);
        markActiveElement(element);
        executeJavaScript("arguments[0].click();", element);

    }

    @Override
    public void hoverAndClick(SelenideElement element) {
        log.info("Entering MobileActions Hover and click");
        if (getPlatformType() == iOs) iOsHoverAndClick(element);
        else androidHoverAndClick(element);
    }

    @Override
    public void sendKeys(SelenideElement textField, String textToType) {

        textField.shouldBe(Condition.visible);
        textField.scrollIntoView(false);
        textField.clear();
        if (getPlatformType()==iOs) this.iOsType(textField, textToType);
        else this.androidType(textField, textToType);
    }

    @Override
    public void sendKeysEnter(SelenideElement textField, String textToType) {

        textField.shouldBe(Condition.visible);
        textField.scrollIntoView(false);
        textField.clear();
        if (getPlatformType()==iOs) this.iOsType(textField, textToType);
        else this.androidType(textField, textToType);
        textField.pressEnter();
    }

    @Override
    public void sendKeysTab(SelenideElement textField, String textToType) {

        textField.shouldBe(Condition.visible);
        textField.scrollIntoView(false);
        textField.clear();
        if (getPlatformType()==iOs) this.iOsType(textField, textToType);
        else this.androidType(textField, textToType);
        textField.pressTab();
    }

    @Override
    public void checkForCookieButton() {
        try {

            while (cookieButton.isDisplayed())
                executeJavaScript("arguments[0].click()", cookieButton);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkForIFrame() {
        if (iFrameHotJar.exists()) {
            this.click(iFrameHotJar);
            this.click(iFrameHotJarCloseButton);
        }

        if (bannerFrame.isDisplayed()) {
            switchTo().frame(bannerFrame).findElement(By.className("bannerContent__closeButton")).click();
            switchTo().defaultContent();
        }
        if($x("//div[@class='mc-banner']//iframe").exists()){
            log.info("CLICK CLOSE BUTTON");
            switchTo().frame($x("//div[@class='mc-banner']//iframe"));     //.findElement(By.className("bannerContent__closeButton")).click();
            executeJavaScript("arguments[0].click()", $x("//div[@class='bannerContent']//div[@class='bannerContent__closeButton']"));

            switchTo().defaultContent();
            log.info("CLICKED CLOSE BUTTON");

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleTranslatePopUp() {

        if (getPlatformType() == Android) {
            try {
                appiumDriver.context("NATIVE_APP").findElement(By.id("translate_infobar_menu_button")).click();
                List<WebElement> l = appiumDriver.findElements(By.id("menu_item_text"));
                l.stream().filter(el -> "Never translate this site".equals(el.getAttribute("text")))
                        .collect(Collectors.toList()).get(0).click();
                appiumDriver.context("CHROMIUM");
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    public void scrollPageToLoad() {
        if (getPlatformType() == iOs) scrollIOs();
        else scrollAndroid();
    }

    public static void iOsSingleScroll(){
        Map<String, Object> args = new HashMap<>();
        log.info("Scrolling down iOs");
        args.put("direction","down");
        executeJavaScript("mobile: scroll",args);
    }

    //text fields regular click?
    private void iOsType(SelenideElement textField, String text){
        log.info("Entering MobileActions iOs Send Keys");
//        textField.click();  //<<--- for ssn
        textField.setValue(text);
//        textField.sendKeys(text);
//        appiumDriver.getKeyboard().sendKeys(text);  //<<--- for ssn
    }


    private void androidType(SelenideElement textField, String text){
        log.info("Entering MobileActions Android Send Keys");
        textField.setValue(text);
    }

    private void iOsHoverAndClick(SelenideElement element){
        element.click();
    }

    private void androidHoverAndClick(SelenideElement element){
        actions().click(element).perform();
    }

    private void scrollIOs(){
        iOsSingleScroll();
        sleep(2000);
        iOsSingleScroll();
        sleep(2000);
        iOsSingleScroll();
        sleep(2000);
    }

    private void scrollAndroid(){
        Number result = executeJavaScript("return document.body.scrollHeight");
        int height = result.intValue();
        log.info("Scrolling down Android");
        executeJavaScript("scroll(0,"+height*0.4+")");
        sleep(2000);
        executeJavaScript("scroll(0,"+height*0.6+")");
        sleep(2000);
        executeJavaScript("scroll(0,"+height*0.8+")");
    }
}
