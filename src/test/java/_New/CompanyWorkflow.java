package _New;

import _pages.CheckOutPage;
import _pages.IndexPage;
import _pages.ProductDetailsPage;
import _pages.ProductListPage;
import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import listeners.RetryAnalyzer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static assertions.assertpage.CheckOutAssert.assertThatCheckOut;

public class CompanyWorkflow extends BaseTest {

    private CheckOutPage checkOutPage;

    @Description("Validates if Item can be searched and added to cart on specified channel")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"url", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItemAndAddToCart(String url, String platform){

        IndexPage indexPage = new IndexPage(url, platform);

        ProductListPage productListPage = indexPage.searchItem();

        ProductDetailsPage productDetailsPage = productListPage.proceedToProductDetailsPage();

        checkOutPage = productDetailsPage.addItemToCartAndGoToCheckOut();

        checkOutPage.completeCompanyForm()
                .getPayMethodValues();
    }

    @Description("Validates if Private customer Address form can be filled with provided address details and " +
            "paymethods are consistent")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1)
    public void checkOutValidation(){

        assertThatCheckOut(checkOutPage).companyWorkflowPayMethodsAreConsistent(checkOutPage, checkOutPage.getCompanyPayMethods());
    }

    @BeforeClass
    public void beforeClass (){
        checkOutPage = new CheckOutPage();
    }

}
