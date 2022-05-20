package assertions.assertpage;

import _pages.Page;
import _pages.ThankYouPage;
import assertions.IAssertable;
import org.assertj.core.api.AbstractAssert;
import org.testng.asserts.SoftAssert;


public class ThankYouPageAssert extends AbstractAssert<ThankYouPageAssert, Page> implements IAssertable {

    private ThankYouPage thankYouPage;

    public ThankYouPageAssert(ThankYouPage page) {
        super(page, ThankYouPageAssert.class);
        this.thankYouPage = page;
    }

    @Override
    public void assertPage(SoftAssert softly) {}

    public void assertSingleService(SoftAssert softly){

        softly.assertEquals(thankYouPage.getCartBlockItemValue(), thankYouPage.getCartBlockTotalSum(), "Cart block total is wrong");
        softly.assertEquals(thankYouPage.getResultBlockTotalSum(), thankYouPage.getCartBlockTotalSum(), "Result block total is wrong");
    }

    public void assertCombinationServices(SoftAssert softly){

        softly.assertEquals(thankYouPage.getPaidItemPrice(), thankYouPage.getCartTotalSum1(), "Paid service total cart is wrong");
        softly.assertEquals(thankYouPage.getFreeValue1(), "Free!", "DHL service should be free");
        softly.assertEquals(thankYouPage.getCartTotalSum2(), thankYouPage.getPaidItemPrice()+thankYouPage.getDhlItemPrice(), "Cart total of two services is wrong");
        softly.assertEquals(thankYouPage.getFreeValue2(), "Free!", "DHL service should be free");
        softly.assertEquals(thankYouPage.getCartTotalSum3(), thankYouPage.getDhlItemPrice(), "Cart total of two services is wrong");

    }
}
