package ThankYouPageTests;

import _pages.*;
import assertions.assertpage.ThankYouPageAssert;
import experiment.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import listeners.RetryAnalyzer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DHLDelivery extends BaseTest {

    private LinkedList<String> deliveryItems;
    private SoftAssert softly;

    @BeforeMethod
    @Parameters({"deliveryItemA","deliveryItemB"})
    public void setUp(String deliveryItemA, String deliveryItemB){

        deliveryItems = Stream.of(deliveryItemA, deliveryItemB).collect(Collectors.toCollection(LinkedList::new));
        softly = new SoftAssert();
    }

    @Description("Validates if  several items can be searched and added to cart on specified channel")
    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"url", "platform", "zip"})
    public void completeDHL(String url, String platform, String zip){

        IndexPage indexPage = new IndexPage(url, platform);
        ProductListPage productListPage = indexPage.searchItem(deliveryItems.poll());
        ProductDetailsPage productDetailsPage = productListPage.selectSingleItem();

        productDetailsPage.addItemToCartAndContinueShopping();
        indexPage.searchItem(deliveryItems.poll());
        productListPage.selectSingleItem();
        CheckOutPage checkOutPage = productDetailsPage.addItemToCartAndGoToCheckOut();
        ThankYouPage thankYouPage = new ThankYouPage(checkOutPage);
        thankYouPage.completeDHL(zip);

        new ThankYouPageAssert(thankYouPage).assertSingleService(softly);
    }

    @AfterMethod
    public void afterMethod(){
        softly.assertAll();
    }
}
