package _pages;

import _pages.enums.ChannelType;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumDriver;

import java.util.function.Consumer;
import java.util.function.Function;

import static _pages.enums.ChannelType.FURNITUREBOX;
import static _pages.enums.ChannelType.WEGOT;
import static _pages.enums.PlatformType.Android;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.ProductPageSelectors.*;


public class ProductDetailsPage extends Page {


    ProductDetailsPage(Page page){
        super(page);
        addLogs("<ProductDetails page>: ");
    }
    @Override
    public boolean isAt() { return addToCartSection.isDisplayed(); }

    public ProductDetailsPage addItemToCartAndContinueShopping(){

//        this.waitForPageToLoad();
        if(!this.isMobile()) handleAddToCartClickDesktop.accept(continueShopping);
        else handleAddToCartClickMobile.accept(continueShopping);

        return this;
    }

    public CheckOutPage addItemToCartAndGoToCheckOut(){

        this.waitForPageToLoad();
        if (!this.isMobile()) handleAddToCartClickDesktop.accept(goToCheckOut);
        else handleAddToCartClickMobile.accept(goToCheckOut);

        return new CheckOutPage(this);
    }

    private Consumer<SelenideElement> handleAddToCartClickDesktop = element -> {

        action.click(addToCart);

        if(channelType == FURNITUREBOX || channelType == WEGOT){

            SelenideElement cartStatus = getCartStatusSelector.apply(channelType);

            int amount = Integer.valueOf(cartStatus.getText().substring(0, 1));

            if (amount == 0){
                executeJavaScript("scroll(0, 100)");
                action.click(addToCart);
                action.click(element);
            }
            else {
                action.click(element);
            }
        }
        else {
            action.click(element);
        }
    };

    private Consumer<SelenideElement> handleAddToCartClickMobile = element -> {

        SelenideElement addToCartButton = addToCartMobile;
        if (channelType == WEGOT){
            if (platformType == Android){
                executeJavaScript("scroll(0, 450)");
               addToCartButton.shouldBe(visible);
            }
            else {
                variantsFrame.scrollIntoView(true);
                $x("//div[@class='productInfoAvailability--stockText']").should(exist).scrollIntoView(false);
                addToCartButton.shouldBe(visible);
            }
        }
        else if (channelType == FURNITUREBOX){
            executeJavaScript("scroll(0, 450)");
            addToCartButton.shouldBe(visible);
        }
        else {
            addToCartButton = addToCart;
            addToCartButton.shouldBe(visible).scrollIntoView(false);
        }
        action.click(addToCartButton);
        action.click(element);
    };

    private static Function<ChannelType, SelenideElement> getCartStatusSelector = channelT -> {

        if(channelT == FURNITUREBOX) return $x("//div[@id='cartStatus']");
        else return $x("//div[@id='cartStatus']//span[@class='cartStatus--summary']");
    };
}
