package ColorTests.Mobile;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.IndexPageMobile;
import pages.ProductPageMobile;

import static org.testng.Assert.assertTrue;

public class ValidatePDPColorsFB_WG extends BaseTest {

    private ProductPageMobile productPage;

    @Parameters({"url", "item", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void goToPDP(String url, String item, String platform){

        IndexPageMobile homePage = new IndexPageMobile(url);
        productPage = homePage.searchForItem(item, platform);
    }

    @Parameters({"color2"})
    @Test(priority = 1)
    public void validateBreadCrumbs(String color2){

        assertTrue(productPage.validateBreadCrumbs(color2));
    }

    @Parameters({"color1", "color3"})
    @Test(priority = 2)
    public void validateDiscountLabels(String color1, String color3){

        SoftAssert sf = new SoftAssert();
        sf.assertTrue(productPage.validateDiscountLabels("background-color", color1));
        sf.assertTrue(productPage.validateDiscountLabels("color", color3));
        sf.assertAll();
    }

    @Parameters({"color1"})
    @Test(priority = 3)
    public void validateCurrentPrices(String color1){

        assertTrue(productPage.validatePricesCurrent(color1));
    }

    @Parameters({"color1"})
    @Test(priority = 4)
    public void validateSavingPrices(String color1){

        assertTrue(productPage.validatePricesSavings(color1));
    }

    @Parameters({"color1", "color3"})
    @Test(priority = 5)
    public void validateSameSeriaPrices(String color1, String color3){

        SoftAssert sf = new SoftAssert();
        sf.assertTrue(productPage.validateDiscountLabels("background-color", color1));
        sf.assertTrue(productPage.validateDiscountLabels("color", color3));
        sf.assertAll();
    }
}
