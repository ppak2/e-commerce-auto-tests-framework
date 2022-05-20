package assertions.assertpage;

import _pages.Page;
import _pages.enums.ChannelType;
import assertions.IAssertable;
import lombok.extern.java.Log;
import org.assertj.core.api.AbstractAssert;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static _pages.enums.ChannelType.Country.*;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@Log
public class CheckOutAssert extends AbstractAssert<CheckOutAssert, Page> implements IAssertable {

    public CheckOutAssert(Page page) { super(page, CheckOutAssert.class);}

    @Override
    public void assertPage(SoftAssert softly) {

    }

    public static CheckOutAssert assertThatCheckOut(Page page){
        return new CheckOutAssert(page);
    }

    public CheckOutAssert privateWorkflowPayMethodsAreConsistent(Page page, List<String> actualValues){

        log.info("Entering CheckOutAssert PrivateWorkflow");
        if(page.getCountry() == null) failWithMessage("CheckOut page is NULL and cannot be asserted");

        List<String> expectedValues = getPrivatePaymentProvidersByCountry.apply(page);
        if (!actualValues.containsAll(expectedValues)){
            failWithMessage("Private paymethods for <%s>.<%s> channel are either have wrong size <%s> or have wrong values"
            ,page.getChannelType(), page.getCountry(), actualValues.size());
        }
        return this;
    }

    public CheckOutAssert companyWorkflowPayMethodsAreConsistent(Page page, List<String> actualValues){

        log.info("Entering CheckOutAssert CompanyWorkflow");
        if(page.getCountry() == null) failWithMessage("CheckOut page is NULL and cannot be asserted");

        List<String> expectedValues = getCompanyPaymentProvidersByCountry.apply(page);
        if (!actualValues.containsAll(expectedValues)){
            failWithMessage("Company paymethods for <%s>.<%s> channel are either have wrong size <%s> or have wrong values"
                    ,page.getChannelType(), page.getCountry(), actualValues.size());
        }
        return this;
    }

    private Function<Page, List<String>> getPrivatePaymentProvidersByCountry = page -> {

        ChannelType.Country country = page.getCountry();
        return Match(country).of(
                Case($(SE), privateSweden.get()),
                Case($(NO), privateNorway.get()),
                Case($(FI), privateFinland.get()),
                Case($(DK), privateDenmark.get())
        );
    };

    private Function<Page, List<String>> getCompanyPaymentProvidersByCountry = page -> {

        ChannelType.Country country = page.getCountry();
        return Match(country).of(
                Case($(SE), companySweden.get()),
                Case($(NO), companyNorway.get()),
                Case($(FI), companyFinland.get()),
                Case($(DK), companyDenmark.get())
        );
    };

    private static Supplier<List<String>> privateSweden = ()-> Arrays.asList("SVEA_CARD", "SVEA_DIRECT_BANK");

    private static Supplier<List<String>> companySweden = ()-> Arrays.asList("SVEA_CARD","SVEA_DIRECT_BANK", "SVEA_INVOICE");

    private static Supplier<List<String>> privateNorway = ()-> Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");

    private static Supplier<List<String>> companyNorway = ()-> Arrays.asList("SVEA_CARD"); //, "SVEA_INVOICE"

    private static Supplier<List<String>> privateFinland = ()-> Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "PAYTRAIL_DIRECT_BANK", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");

    private static Supplier<List<String>> companyFinland = ()-> Arrays.asList("SVEA_CARD", "PAYTRAIL_DIRECT_BANK");

    private static Supplier<List<String>> privateDenmark = ()-> Arrays.asList("SVEA_CARD");

    private static Supplier<List<String>> companyDenmark = ()-> Arrays.asList("SVEA_CARD");

}
