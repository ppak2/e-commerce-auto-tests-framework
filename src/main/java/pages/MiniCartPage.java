package pages;

import com.codeborne.selenide.SelenideElement;

import java.util.StringJoiner;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.sleep;
import static repository.ElementSelectors.IndexPageSelectors.*;
import static repository.ElementSelectors.MiniCartSelectors.*;

public class MiniCartPage extends Page {

//    private ChannelType channelType;

    private static boolean cookieButtonIsClicked; //


    public MiniCartPage(String url) {

        super(url);
    }

    public MiniCartPage clickOnWidget(){

        miniCartIcon.waitUntil(appear, waitTimeout()).scrollIntoView(true).click();
        return this;
    }

    public MiniCartPage openWidget() {

        checkForIFrame(iFrameHotJar, iFrameHotJarCloseButton);
        checkForCookieButton();
        checkForIFrame(iFrame, iFrameCloseButton);
        miniCartIcon.waitUntil(appear, waitTimeout()).scrollIntoView(true).click();
        return this;
    }

    public void addItemsQuantitySelector(int value) {

        inputField.waitUntil(appear, waitTimeout()).scrollIntoView(false).clear();
        inputField.setValue(String.valueOf(value)).pressEscape();
        sleep(3000);
    }

    public MiniCartPage pressPlusButton() {

        if (productContainerDisabled.exists()) productContainerDisabled.waitUntil(disappear, waitTimeout());
        actions().clickAndHold(plusButton).release().perform();
        sleep(3000);
        return this;
    }

    public MiniCartPage pressMinusButton() {

        if (productContainerDisabled.exists()) productContainerDisabled.waitUntil(disappear, waitTimeout());
        actions().clickAndHold(minusButton).release().perform();
        sleep(3000);
        return this;
    }

    public MiniCartPage pressRemoveButton() {

        removeButton.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
        miniCartTotal.shouldHave(text("0"));
        sleep(5000);
        return this;
    }

    public void pressUndoButton() {

        undoButton.waitUntil(appear, waitTimeout()).scrollIntoView(false).doubleClick();
        sleep(3000);
    }

    public void goToProductPage() {

        goToProduct.waitUntil(appear, waitTimeout()).scrollIntoView(false).doubleClick();
        sleep(3000);
    }

    public boolean isImagePresent() {

        imageSelector.waitUntil(appear, waitTimeout());
        return imageSelector.exists();
    }

    public String getTitle() {

        itemDescription.waitUntil(appear, waitTimeout());
        return itemDescription.getText();
    }

    public Integer getCount() {

        final SelenideElement total = itemsCount(this.channelType);
        if (productContainerDisabled.exists()) productContainerDisabled.waitUntil(disappear, waitTimeout());
        total.waitUntil(appear, waitTimeout());
        return Integer.valueOf(total.getText().substring(0, 1));
    }

    public String getPrice() {

        if (productContainerDisabled.exists()) productContainerDisabled.waitUntil(disappear, waitTimeout());
        pricePerItem.waitUntil(appear, waitTimeout());
        return pricePerItem.getText();
    }

    public String getTotalPrice() {

        if (productContainerDisabled.exists()) productContainerDisabled.waitUntil(disappear, waitTimeout());
        miniCartTotal.waitUntil(appear, waitTimeout());
        return miniCartTotal.getText();
    }

    public boolean isUndoPresent() {

        undoButton.waitUntil(appear, waitTimeout());
        return undoButton.isDisplayed();
    }

    public boolean isGoToProductPresent() {

        goToProduct.waitUntil(appear, waitTimeout());
        return goToProduct.isDisplayed();
    }

    public Double getPriceValue(String priceText) {

        String[] array = priceText.split("\\s");
        StringJoiner sj = new StringJoiner("");
        for (String s : array) {
            if (s.matches("\\d+") || s.matches("\\d+,\\d+")) sj.add(s);
            else break;
        }

        double result = Double.parseDouble(sj.toString().replace(',', '.'));
        result*=100;
        result = Math.round(result);
        result/=100;

        return result;
    }

    private SelenideElement itemsCount(ChannelType channelType) {

        SelenideElement itemCountIcon = itemsCount;
        if (channelType == ChannelType.KODIN1) itemCountIcon = $x("//span[@class='btnCheckoutCartStatus--quantity']");
        else if (channelType == ChannelType.FURNITUREBOX) itemCountIcon = $x("//div[@id='cartStatus']");
        else if (channelType == ChannelType.WEGOT)
            itemCountIcon = $x("//div[@id='cartStatus']//span[@class='cartStatus--summary']");

        return itemCountIcon;
    }

    private void checkForCookieButton() {

        if (cookieButton.isDisplayed() && !cookieButtonIsClicked) {
            cookieButton.click();
            cookieButtonIsClicked = true;
        }
    }

}
