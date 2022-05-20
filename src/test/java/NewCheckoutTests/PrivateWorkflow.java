package NewCheckoutTests;

import base.BaseTest;
import io.qameta.allure.Description;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.CheckOutPage;
import pages.IndexPage;
import pages.ProductPage;

import static assertions.PayMethodsAssert.assertThat;


public class PrivateWorkflow extends BaseTest {


    @Description("Validates if Item can be found and added to cart on specified channel")
    @Parameters({"url", "item", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItemAndAddToCart(String url, String item, String platform) {
        IndexPage homePage = new IndexPage(url);
        ProductPage productPage = homePage.searchForItem(item, platform);
        productPage.addItemToCart();
    }

    @Description("Validates if Private customer Address form can be filled with provided address details")
    @Parameters({"address", "postcode", "phone"})
    @Test(priority = 1)
    public void fillAndValidateAddressForm(String address, String postcode, String phone) {

        CheckOutPage checkOutPage = new CheckOutPage()
                .fillAndValidateAddressFormPrivate(address, postcode, phone)
                .validateDeliveryServices()
                .getPayMethodValues();

        assertThat(checkOutPage).privateWorkflowPayMethodsAreConsistent(checkOutPage, checkOutPage.payMethodsList);
    }

}
