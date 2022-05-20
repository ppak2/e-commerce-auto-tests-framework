package NewCheckoutTests;

import base.BaseTest;
import io.qameta.allure.Description;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.CheckOutPage;
import pages.CheckOutPageMobile;
import pages.IndexPageMobile;
import pages.ProductPageMobile;

import static assertions.PayMethodsAssert.assertThat;


public class PrivateWorkflowMobile extends BaseTest {


    @Description("Validates if Item can be found and added to cart on specified channel")
    @Parameters({"url", "item", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItemAndAddToCart(String url, String item, String platform) {
        IndexPageMobile homePage = new IndexPageMobile(url);
        ProductPageMobile productPage = homePage.searchForItem(item, platform);
        productPage.addItemToCart(url, platform);
    }

    @Description("Validates if Private customer Address form can be filled with provided address details")
    @Parameters({"address", "postcode", "phone"})
    @Test(priority = 1)
    public void fillAddress(String address, String postcode, String phone) {

        CheckOutPage checkOutPage = new CheckOutPageMobile()
                .fillAndValidateAddressFormPrivate(address, postcode, phone)
                .validateDeliveryServices()
                .getPayMethodValues();

        assertThat(checkOutPage).privateWorkflowPayMethodsAreConsistent(checkOutPage, checkOutPage.payMethodsList);

    }
}
