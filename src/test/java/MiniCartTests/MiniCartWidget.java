package MiniCartTests;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.IndexPage;
import pages.MiniCartPage;
import pages.ProductPage;
import repository.ElementSelectors;

import static org.testng.AssertJUnit.assertTrue;

public class MiniCartWidget extends BaseTest {

    private IndexPage indexPage;
    private MiniCartPage miniCart;
    private ProductPage productPage;
    private String itemName;

    @Parameters({"url"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void validateEmptyState(String url) {

        indexPage = new IndexPage(url);
        miniCart = new MiniCartPage(url).openWidget();
        assertTrue(ElementSelectors.MiniCartSelectors.miniCartEmpty.exists());
    }

    @Parameters({"item"})
    @Test(priority = 1, alwaysRun = true)
    public void validateMiniCart(String item) {

        productPage = new ProductPage();

        miniCart.clickOnWidget();
        indexPage.searchForItem(item);
        productPage.addItemToCartAndContinue();
        miniCart.openWidget();
        itemName = miniCart.getTitle();

        SoftAssert sf = new SoftAssert();
        sf.assertTrue(miniCart.isImagePresent(), "Product image should load");
        sf.assertTrue(!(miniCart.getTitle()).isEmpty(), "Product title should not be empty");
        sf.assertSame(miniCart.getCount(), 1, "Count should be 1");
        sf.assertTrue(miniCart.getPriceValue(miniCart.getPrice()) <= miniCart.getPriceValue(miniCart.getTotalPrice()));
        sf.assertAll();
    }

    @Test(priority = 2, alwaysRun = true)
    public void addItemMinusItem() {

        Double price = miniCart.getPriceValue(miniCart.getPrice());
        miniCart.addItemsQuantitySelector(3);
        Double priceAmountQC = Math.floor(miniCart.getPriceValue(miniCart.getPrice())); //price * 3
        miniCart.pressMinusButton().pressMinusButton();
        int countAfterMinusPress = miniCart.getCount(); //should be 1
        miniCart.pressPlusButton().pressPlusButton().pressPlusButton().pressPlusButton();
        int countAfterPlusPress = miniCart.getCount(); //should be 5
        Double priceAmountButtons = Math.floor(miniCart.getPriceValue(miniCart.getPrice())); //price * 5

        SoftAssert sf = new SoftAssert();
        sf.assertEquals(priceAmountQC, (Math.floor(price * 3)), "Price should be multiplied by 3");
        sf.assertSame(countAfterMinusPress, 1, "Count after minus should be 1");
        sf.assertSame(countAfterPlusPress, 5, "Count after plus should be 5");
        sf.assertEquals(priceAmountButtons, (Math.floor(price * 5)), "Price should be multiplied by 5");
        sf.assertAll();
    }

    @Test(priority = 3, alwaysRun = true)
    public void removeItem() {

        miniCart.pressRemoveButton();
        Double totalPrice = miniCart.getPriceValue(miniCart.getTotalPrice());

        SoftAssert sf = new SoftAssert();
        sf.assertTrue(miniCart.isImagePresent());
        sf.assertEquals(itemName, miniCart.getTitle());
        sf.assertTrue(miniCart.isUndoPresent());
        sf.assertTrue(miniCart.isGoToProductPresent());
        sf.assertEquals(totalPrice, 0.0);
        sf.assertAll();

    }

    @Test(priority = 4, alwaysRun = true)
    public void undo() {

        miniCart.pressUndoButton();

        SoftAssert sf = new SoftAssert();
        sf.assertTrue(miniCart.isImagePresent(), "Product image should load");
        sf.assertTrue(!(miniCart.getTitle()).isEmpty(), "Product title should not be empty");
        sf.assertTrue(miniCart.getCount() > 0, "Count should be 1+");
        sf.assertTrue(miniCart.getPriceValue(miniCart.getPrice()) <= miniCart.getPriceValue(miniCart.getTotalPrice()));
        sf.assertAll();

    }

    @Test(priority = 5, alwaysRun = true)
    public void removeItemFromCartAndGotToPDP() {

        miniCart.pressRemoveButton()
                .goToProductPage();

        Assert.assertTrue(ElementSelectors.ProductPageSelectors.addToCart.isDisplayed());

    }

}
