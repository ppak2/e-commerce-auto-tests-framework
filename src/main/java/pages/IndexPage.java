package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import repository.ElementSelectors;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.*;
import static pages.ChannelType.FURNITUREBOX;
import static pages.ChannelType.WEGOT;
import static repository.ElementSelectors.IndexPageSelectors.*;
import static repository.ElementSelectors.MonitorButtonSelectors.monitorButtonOnPLP;


public class IndexPage extends Page {


   private static boolean cookieButtonIsClicked; //

    public IndexPage(String url) {

        super(url);
        open(url);
    }

    public ProductPage searchForItem(String itemID, String platformName) {

        final SelenideElement foundCategory = $x("//div[@id='searchPageTabs']//a[@title='" + itemID + "']");

        checkForIFrame(iFrame, iFrameCloseButton);
        checkForCookieButton();
        searchField.waitUntil(appear, waitTimeout()).click();
        searchField.setValue(itemID);
        sleep(2000);
        searchField.pressEnter();
        //////////////////////////
        categoriesTab.waitUntil(appear, waitTimeout()).scrollIntoView(false);  //-- click?
        executeJavaScript("arguments[0].click()", categoriesTab);
        foundCategory.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", foundCategory);
        sleep(2000);
        if (firstProductInCategoryList.isDisplayed()) {
            firstProductInCategoryList.scrollIntoView(false);
            executeJavaScript("arguments[0].click()", firstProductInCategoryList);
        } else {
            if ($x("//div[@id='scrollTo']").exists()) $x("//div[@id='scrollTo']").scrollIntoView(true);
            firstProductInCategoryList.waitUntil(appear, waitTimeout());
            executeJavaScript("arguments[0].click()", firstProductInCategoryList);
        }

        return new ProductPage();
    }

    public IndexPage searchForItem(String itemID) {

        final SelenideElement foundCategory = $x("//div[@id='searchPageTabs']//a[@title='" + itemID + "']");
        checkForIFrame(iFrame, iFrameCloseButton);
        checkForCookieButton();
        searchField.waitUntil(appear, waitTimeout()).click();
        searchField.setValue(itemID);
        sleep(2000);
        searchField.pressEnter();
        //////////////////////////
        categoriesTab.waitUntil(appear, waitTimeout()).click();
        foundCategory.waitUntil(appear, waitTimeout()).doubleClick();
        sleep(2000);
        return this;
    }

    public IndexPage searchForMonitoringProduct(String itemID) {

        checkForIFrame(iFrame, iFrameCloseButton);
        checkForCookieButton();
        searchField.waitUntil(appear, waitTimeout()).click();
        searchField.setValue(itemID);
        sleep(2000);
        searchField.pressEnter();
        //////////////////////////
        productListSection.waitUntil(appear, waitTimeout());
//        monitorButtonOnPLP.waitUntil(appear, waitTimeout()).scrollIntoView(false);
//        executeJavaScript("arguments[0].click()", monitorButtonOnPLP);
        handleMonitorButtonClick();
        sleep(3000);

        return this;
    }

    public IndexPage searchAndSelectSingleItem(String itemID){

        checkForIFrame(iFrame, iFrameCloseButton);
        checkForCookieButton();
        searchField.waitUntil(appear, waitTimeout()).setValue(itemID);
        searchField.pressEnter();
        productListSection.waitUntil(appear, waitTimeout());
        singleItemOnPLP.waitUntil(appear, waitTimeout()).click();

        return this;
    }

    public IndexPage searchForItemAndGoToPLP(String itemID){

        checkForIFrame(iFrame, iFrameCloseButton);
        checkForCookieButton();
        searchField.waitUntil(appear, waitTimeout()).click();
        searchField.setValue(itemID);
        sleep(2000);
        searchField.pressEnter();
        productListSection.waitUntil(appear, waitTimeout());

        return this;
    }

    public void scrollToBestSellersProducts(){

        scrollToFooter();
        sleep(3000);

        if(channelType == FURNITUREBOX || channelType == WEGOT){

            bestSellersProductsImgFBWG.last().waitUntil(appear, waitTimeout()).scrollIntoView(false);
        }

        else {

            bestSellersProductsImgTCK.last().waitUntil(appear, waitTimeout()).scrollIntoView(false);
        }
    }

    //hack for bad scrolling on Safari
    private void handleMonitorButtonClick() {

        SelenideElement monitorB = monitorButtonOnPLP;

        if (channelType == FURNITUREBOX || channelType == WEGOT)
            monitorB = $x("//article[@class='productItem productItem-extended']/a[1]");

        monitorB.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", monitorB);

    }

    private void checkForCookieButton() {

        if (cookieButton.isDisplayed()) {
            cookieButton.click();
        }
    }


}
