package experiment;

import _pages.CheckOutPage;
import _pages.IndexPage;
import _pages.ProductDetailsPage;
import _pages.ProductListPage;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class PrivateWorkflow extends BaseTest {


    @Parameters({"url", "platform"})
    @Test
    public void searchForItemAndAddToCart(String url, String platform){

        IndexPage indexPage = new IndexPage(url, platform);

        ProductListPage productListPage = indexPage.searchItem();

        ProductDetailsPage productDetailsPage = productListPage.proceedToProductDetailsPage();

        CheckOutPage checkOutPage = productDetailsPage.addItemToCartAndGoToCheckOut();

        checkOutPage.completePrivateForm();
    }
}
