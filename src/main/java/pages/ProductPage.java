package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.Color;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static pages.ChannelType.FURNITUREBOX;
import static pages.ChannelType.WEGOT;
import static repository.ElementSelectors.MonitorButtonSelectors.*;
import static repository.ElementSelectors.ProductPageSelectors.*;

public class ProductPage extends Page {

    public ProductPage() {

        System.out.println("ProductPage " + channelType);
    }

    public ProductPage(String url) {

        super(url);
    }

    public ProductPage addItemToCart() {

        System.out.println(">>>>>>>>>>"+channelType);
        addToCartAndConfirm();
        sleep(2000);
        return this;
    }

    public void addItemToCartAndContinue() {

        firstItemAddToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", firstItemAddToCart);
        addToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false).doubleClick();
        continueShopping.waitUntil(appear, waitTimeout()).click();
    }

    private void addToCartAndConfirm() {

        if (channelType == FURNITUREBOX || channelType == WEGOT) {

            SelenideElement cartStatus = defineCartStatusSelector();

            addToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false).doubleClick();
            sleep(3000);
            int amount = Integer.valueOf(cartStatus.getText().substring(0, 1));
            System.out.println("=========" + amount + "==========");
            if (amount == 0) {

                executeJavaScript("scroll(0, 100)");
                addToCart.click();
                goToCheckOut.waitUntil(appear, waitTimeout()).doubleClick();
            } else {
                goToCheckOut.waitUntil(appear, waitTimeout()).doubleClick();
            }

        } else {

            addToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false).doubleClick();
            goToCheckOut.waitUntil(appear, waitTimeout()).doubleClick();
        }
    }

    private SelenideElement defineCartStatusSelector() {

        SelenideElement cartStatus = $x("//div[@id='cartStatus']");

        if (channelType == WEGOT) {
            cartStatus = $x("//div[@id='cartStatus']//span[@class='cartStatus--summary']");
        }

        return cartStatus;
    }

    @Override
    public boolean validateElementsAttributes(ElementsCollection elements, String attributeName, String attributeValue) {

        identifyPDP.waitUntil(exist, waitTimeout());
        ElementsCollection elementsExtracted = elements;

        boolean result = super.validateElementsAttributes(elementsExtracted, attributeName, attributeValue);
        return result;
    }

    public boolean validateBreadCrumbs(String colorHex) {

        if (channelType == WEGOT){
            return validateWGBreadCrumbs(colorHex, "#bdbdbd");
        }
        else {
            return validateElementsAttributes(breadCrumbs, "color", colorHex);
        }
    }

    public boolean validateDiscountLabels(String attr, String colorHex) {

        boolean result;

        System.out.println("|||" + channelType + "|||");
        if (channelType == FURNITUREBOX || channelType == WEGOT) {
            result = (validateElementsAttributes(discountLabelFB, attr, colorHex) && validateElementsAttributes(discountLabelsFB, attr, colorHex));
        } else result = validateElementsAttributes(discountLabels, attr, colorHex);

        return result;
    }

    public boolean validateSameSeriaPrices(String attr, String colorHex) {

        return validateElementsAttributes(sameSeriePrices, attr, colorHex);
    }

    public boolean validatePricesCurrent(String colorHex) {

        ElementsCollection elementsCollection = pricesCurrent;

        if (channelType == FURNITUREBOX || channelType == WEGOT) {
            elementsCollection = pricesCurrentFB;
        }
        return validateElementsAttributes(elementsCollection, "color", colorHex);
    }

    public boolean validatePricesSavings(String colorHex) {

        return validateElementsAttributes(pricesSavings, "color", colorHex);
    }

    public void validateMonitorButtonSection() {

        addToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        emailField.shouldBe(visible);
        checkBox.shouldBe(visible);

        addToCart.click();
        toolTip.shouldBe(visible);

        checkBox.click();
        toolTip.shouldNot(be(visible));

        addToCart.click();
        toolTip.shouldBe(visible);

        emailField.setValue(randomEmail()).pressEnter();
        toolTip.shouldNot(be(visible));

        sleep(2000);
//        addToCart.click();
        reset.shouldBe(visible);

        reset.click();
        emailField.shouldBe(visible);

    }

    private boolean validateWGBreadCrumbs(String color2, String color3) {

        boolean result;
        SelenideElement lastBC = breadCrumbs.last();
        List<SelenideElement> collection = breadCrumbs.stream().limit((long) breadCrumbs.size() - 1).collect(Collectors.toList());
        String expectedColor3 = Color.fromString(lastBC.getCssValue("color")).asHex();

        result = (expectedColor3.equals(color3) && collection
                .stream().map(el -> el.getCssValue("color"))
                .map(col -> Color.fromString(col).asHex())
                .allMatch(attr -> attr.equals(color2)));

        return result;
    }


}
