package pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.be;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;
import static pages.ChannelType.FURNITUREBOX;
import static pages.ChannelType.WEGOT;
import static repository.ElementSelectors.MonitorButtonSelectors.*;
import static repository.ElementSelectors.MonitorButtonSelectors.emailField;
import static repository.ElementSelectors.MonitorButtonSelectors.reset;
import static repository.ElementSelectors.ProductPageSelectors.*;

public class ProductPageMobile extends ProductPage {

    public ProductPageMobile() {

        System.out.println("ProductPageMobile " + channelType);
    }

    public ProductPage addItemToCart(String url, String platform) {

        addToCartAndConfirm(channelType, platform);
        sleep(3000);
        return this;

    }

    @Override
    public void validateMonitorButtonSection(){

        AppiumDriver apd = (AppiumDriver) WebDriverRunner.getWebDriver();

        identifyPDPMobile.waitUntil(appear, waitTimeout());
        addToCart.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        emailField.shouldBe(visible);
        checkBox.shouldBe(visible);

        addToCart.click();
        toolTip.shouldBe(visible);

        checkBox.scrollIntoView(false).click();
        toolTip.shouldNot(be(visible));

        addToCart.click();
        toolTip.shouldBe(visible);

        emailField.click();
        apd.getKeyboard().sendKeys(randomEmail());
        executeJavaScript("arguments[0].click()", addToCart);

        toolTip.shouldNot(be(visible));

        sleep(2000);
        reset.shouldBe(visible);

        executeJavaScript("arguments[0].click()", reset);
        emailField.shouldBe(visible);
    }

    private void addToCartAndConfirm(ChannelType channelType, String platform) {

        SelenideElement addToCartButton = addToCartMobile;

        if (channelType == WEGOT) {

            if (platform.equalsIgnoreCase("android")) {

                executeJavaScript("scroll(0, 450)");
                addToCartButton.waitUntil(appear, waitTimeout());

            } else {

                try{
                    variantsFrame.scrollIntoView(true);
                }
                catch (WebDriverException e){
                    e.printStackTrace();
                }
                finally {
                    sleep(2000);
                    $x("//div[@class='productInfoAvailability--stockText']").scrollIntoView(false);
                    addToCartButton.waitUntil(appear, waitTimeout());
                }

            }
        }
        else if(channelType == FURNITUREBOX){

            executeJavaScript("scroll(0, 450)");
            addToCartButton.waitUntil(appear, waitTimeout());

        }
        else {

            addToCartButton = addToCart;
            addToCartButton.waitUntil(appear, waitTimeout()).scrollIntoView(false);

        }

        executeJavaScript("arguments[0].click()", addToCartButton);

        goToCheckOut.waitUntil(appear, waitTimeout()).click();
    }

//    private void scrollIOs() {
//
////        executeJavaScript("scroll(0, 450)");
////        variantsFrame.waitUntil(appear, waitTimeout()).scrollIntoView(true);
//        sleep(2000);
//    }
//
//    private void scrollAndroid(SelenideElement element) {
//
//        executeJavaScript("scroll(0, 450)");
////        $x("//div[@id='productSameCategory']//h2").waitUntil(appear, waitTimeout()).scrollIntoView(false);
//        sleep(2000);
//    }

}
