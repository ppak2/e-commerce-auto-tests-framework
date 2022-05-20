package ImagesLoadTests;

import base.BaseTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPage;

import static assertions.ImageLoadAssert.assertThat;


public class ImagesTest extends BaseTest {

    private IndexPage homePage;

    @Parameters({"url", "item", "platform"})
    @Test
    public void searchForItemAndAddToCart(String url, String item, String platform) {

        homePage = new IndexPage(url);
        homePage.scrollToBestSellersProducts();
    }

    @Test(priority = 1)
    public void popularProductsValidation() {

        assertThat(homePage).popularProductsAreNotEmpty()
                .popularProductsImagesAreLoaded();
    }

    @Test(priority = 2)
    public void bestSellersProductsValidation() {

        assertThat(homePage).bestSellersProductsAreNotEmpty()
                .bestSellersProductsImagesAreLoaded();
    }


    @Parameters({"item"})
    @Test(priority = 3)
    public void imagesTestOnPLP(String item) {

        homePage.searchForItemAndGoToPLP(item);
        homePage.scrollToFooter();

        assertThat(homePage).productsOnPLPareNotEmpty()
                .productsImagesOnPLPAreLoaded();

    }
}
