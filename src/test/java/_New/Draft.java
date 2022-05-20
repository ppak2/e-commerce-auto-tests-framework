package _New;

import _pages.CheckOutPage;
import _pages.IndexPage;
import _pages.ProductDetailsPage;
import _pages.ProductListPage;
import assertions.assertpage.IndexPageAssert;
import assertions.assertpage.ProductDetailsPageAssert;
import assertions.assertpage.ProductListPageAssert;
import base.BaseTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static assertions.assertpage.CheckOutAssert.assertThatCheckOut;


public class Draft extends BaseTest {

    private SoftAssert softly;
    private CheckOutPage checkOutPage;


    @Parameters({"url", "platform"})
    @Test
    public void searchForItemAndAddToCart(String url, String platform){

        IndexPage indexPage = new IndexPage(url, platform);
        indexPage.getAction().scrollToFooter();

        new IndexPageAssert(indexPage).assertPage(softly);

        ProductListPage productListPage = indexPage.searchItem();
        productListPage.getAction().scrollPageToLoad();

        new ProductListPageAssert(productListPage).assertPage(softly);

        ProductDetailsPage productDetailsPage = productListPage.proceedToProductDetailsPage();
        productDetailsPage.getAction().scrollToFooter();

        new ProductDetailsPageAssert(productDetailsPage).assertPage(softly);

        checkOutPage = productDetailsPage.addItemToCartAndGoToCheckOut();
        checkOutPage.completePrivateForm();
//        checkOutPage.completeCompanyForm().getPayMethodValues();

    }

    @BeforeClass
    public void createAssert(){

        softly = new SoftAssert();
        checkOutPage = new CheckOutPage();
    }

    @AfterClass
    public void makeAssert(){

        softly.assertAll();
//        assertThatCheckOut(checkOutPage).privateWorkflowPayMethodsAreConsistent(checkOutPage, checkOutPage.payMethodsList);
    }


}
