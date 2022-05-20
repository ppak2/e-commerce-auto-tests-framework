package ColorTests;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPage;
import pages.ProductPage;

import static assertions.ElementsColorAssert.assertThatColors;


public class NewColorTests extends BaseTest {

    private ProductPage productPage;

    @Parameters({"url", "item", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void goToPDP(String url, String item, String platform){

        IndexPage homePage = new IndexPage(url);
        productPage = homePage.searchForItem(item, platform);
    }

    @Test(priority = 1)
    public void validateBreadCrumbs(){


        assertThatColors(productPage).ofBreadCrumbsAreConsistent()
                .ofDiscountLabelsAreConsistent()
                .ofCurrentPricesAreConsistent()
                .ofSavingsPricesAreConsistent()
                .ofSameSeriePricesAreConsistent();
    }
}
