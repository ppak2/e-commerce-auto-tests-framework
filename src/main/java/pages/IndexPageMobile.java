package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebElement;
import repository.ElementSelectors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;
import static pages.ChannelType.FURNITUREBOX;
import static pages.ChannelType.WEGOT;
import static repository.ElementSelectors.IndexPageSelectors.*;
import static repository.ElementSelectors.IndexPageSelectors.iFrameHotJar;
import static repository.ElementSelectors.IndexPageSelectors.iFrameHotJarCloseButton;
import static repository.ElementSelectors.MonitorButtonSelectors.monitorButtonOnPLP;
import static repository.ElementSelectors.ProductPageSelectors.sameSeriaDropDown;

public class IndexPageMobile extends IndexPage {


    private SelenideElement foundCategory;

    public IndexPageMobile(String url) {

        super(url);
    }

    @Override
    public ProductPageMobile searchForItem(String itemID, String platformName) {

        foundCategory = $x("//div[@id='searchPageTabs']//a[@title='" + itemID + "']");
        if (channelType == WEGOT && platformName.equals("ios")) handleIFrame();
        checkForCookieButton();
//        //new cookiebutton closelogic
//        cookieButton.waitUntil(appear, waitTimeout());
//        while (cookieButton.isDisplayed()) executeJavaScript("arguments[0].click()", cookieButton);

        if (bannerFrame.isDisplayed()) {
            switchTo().frame(bannerFrame).findElement(By.className("bannerContent__closeButton")).click();
        }
        if (platformName.equalsIgnoreCase("android")) {
            searchAndAddAndroid(itemID);
        } else {
            searchAndAddIOs(itemID);
        }

        if (channelType == FURNITUREBOX)

            sameSeriaDropDown.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();

        return new ProductPageMobile();

    }


    public IndexPageMobile searchForMonitoringProduct(String itemID, String platformName) {

        if (channelType == WEGOT && platformName.equals("ios")) handleIFrame();
        if (cookieButton.isDisplayed()) {
            while (cookieButton.isDisplayed()) cookieButton.click();
        }
        if (bannerFrame.isDisplayed()) {
            switchTo().frame(bannerFrame).findElement(By.className("bannerContent__closeButton")).click();
        }

        if (platformName.equalsIgnoreCase("android")) {
            searchAndroid(itemID);
        } else {
            searchIOs(itemID);
        }

        productListSection.waitUntil(appear, waitTimeout());
        monitorButtonOnPLP.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();

        return this;
    }

    @Override
    public IndexPageMobile searchAndSelectSingleItem(String itemID) {

        AppiumDriver apd = (AppiumDriver) WebDriverRunner.getWebDriver();
        String platformName = apd.getPlatformName();

        if (channelType == WEGOT && platformName.equalsIgnoreCase("ios")) handleIFrame();
        if (cookieButton.isDisplayed()) {
            executeJavaScript("arguments[0].click()", cookieButton);
        }

        if (bannerFrame.isDisplayed()) {
            switchTo().frame(bannerFrame).findElement(By.className("bannerContent__closeButton")).click();
        }

        if (platformName.equalsIgnoreCase("android")) {
            searchAndroid(itemID);
        } else {
            searchIOs(itemID);
        }

        productListSection.waitUntil(appear, waitTimeout());
        singleItemOnPLP.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();

        return this;
    }

    private void searchAndAddAndroid(String item) {

        System.out.println("====" + "Android method called" + "====");
        handleChromiumTranslate();
        searchFieldIconMobile.waitUntil(appear, waitTimeout()).click();
        searchFieldMobile.waitUntil(appear, waitTimeout()).setValue(item).pressEnter();
        sleep(2000);

        categoriesTab.waitUntil(appear, waitTimeout()).click();
        foundCategory.waitUntil(appear, waitTimeout()).click();

        firstProductInCategoryList.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", firstProductInCategoryList);
        sleep(2000);
    }

    private void searchAndAddIOs(String item) {

        AppiumDriver apd = (AppiumDriver) WebDriverRunner.getWebDriver();

        System.out.println("====" + "IOS method called" + "====");
        apd.switchTo().defaultContent();
        searchFieldIconMobile.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].style.border='3px dotted red'", searchFieldIconMobile);
        executeJavaScript("arguments[0].click()", searchFieldIconMobile);
        sleep(3000);

        searchFieldMobile.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].style.border='3px dotted red'", searchFieldMobile);
//        $x("//div[@class='headerSearch--resultsListTitle']").click();
        searchFieldMobile.click();

        apd.getKeyboard();
        searchFieldMobile.click();
        apd.getKeyboard().sendKeys(item);

        searchFieldSubmitIconMobile.waitUntil(appear, waitTimeout()).click();
        sleep(2000);

        categoriesTab.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].click()", categoriesTab);
        foundCategory.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].click()", foundCategory);
        firstProductInCategoryList.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", firstProductInCategoryList);
        sleep(2000);
    }

    private void searchAndroid(String item) {

        System.out.println("====" + "Android method called" + "====");
        handleChromiumTranslate();
        searchFieldIconMobile.waitUntil(appear, waitTimeout()).click();
        searchFieldMobile.waitUntil(appear, waitTimeout()).setValue(item).pressEnter();
        sleep(2000);
    }

    private void searchIOs(String item) {

        System.out.println("====" + "IOS method called" + "====");
        searchFieldIconMobile.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].click()", searchFieldIconMobile);
        searchFieldMobile.waitUntil(appear, waitTimeout()).click();
        AppiumDriver apd = (AppiumDriver) WebDriverRunner.getWebDriver();
        apd.getKeyboard().sendKeys(item);
        searchFieldSubmitIconMobile.waitUntil(appear, waitTimeout()).click();
        sleep(2000);
    }

    private void handleChromiumTranslate() {

        AppiumDriver apd = (AppiumDriver) WebDriverRunner.getWebDriver();
        Set<?> ctx = apd.getContextHandles();
        ctx.forEach(System.out::println);
        apd.context("NATIVE_APP").findElement(By.id("translate_infobar_menu_button")).click();
        System.out.println("++++ BUTTON CLICKED ++++");
        List<WebElement> all = apd.findElements(By.id("menu_item_text"));
        all.stream().filter(el -> "Never translate this site".equals(el.getAttribute("text"))).collect(Collectors.toList()).get(0).click();
        apd.context("CHROMIUM");
    }

    private void handleIFrame() {
        if (iFrameHotJar.isDisplayed()) {
            System.out.println("-=-= handleIFrame is called -=-=");
            System.out.println(iFrameHotJar.isEnabled());
            iFrameHotJar.waitUntil(appear, 4000).click();
            iFrameHotJarCloseButton.waitUntil(appear, 4000).click();
            System.out.println("HotJar clicked");
        }
    }

    private void checkForCookieButton() {

        try {
            //new cookiebutton closelogic
//            cookieButton.waitUntil(appear, waitTimeout());
            while (cookieButton.isDisplayed()) executeJavaScript("arguments[0].click()", cookieButton);
        }
        catch (NoSuchElementException e){

            e.printStackTrace();
        }
    }
}
