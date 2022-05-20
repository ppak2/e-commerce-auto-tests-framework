package assertions;

import org.assertj.core.api.AbstractAssert;
import pages.Page;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static pages.ChannelType.Country.FI;
import static pages.ChannelType.Country.NO;
import static pages.ChannelType.Country.SE;

public class PayMethodsAssert extends AbstractAssert<PayMethodsAssert, Page> {

    private PayMethodsAssert(Page page){
        super(page, PayMethodsAssert.class);
    }

    public static PayMethodsAssert assertThat(Page page){
        return new PayMethodsAssert(page);
    }

    @SuppressWarnings("unused")
    public static PayMethodsAssert assertThatPayMethods(Page page) { return new PayMethodsAssert(page); }

    @SuppressWarnings("unused")
    public PayMethodsAssert privateWorkflowPayMethodsAreConsistent(Page page, List<String> actualValues){

        isNotNull();

        List<String> expectedValues = getPrivatePaymentProvidersByCountry.apply(page);

        if(actualValues.size() != expectedValues.size() || !actualValues.containsAll(expectedValues)){
            failWithMessage("Private paymethods for <%s> channel are either have wrong size <%s> or " +
                    "have wrong values", page.getChannelType(), actualValues.size());
        }
        return this;
    }

    @SuppressWarnings("unused")
    public PayMethodsAssert companyWorkflowPayMethodsAreConsistent(Page page, List<String> actualValues){

        isNotNull();

        List<String> expectedValues = getCompanyPaymentProvidersByCountry.apply(page);
        if(actualValues.size() != expectedValues.size() || !actualValues.containsAll(expectedValues)){
            failWithMessage("Company paymethods for <%s> channel are either have wrong size <%s> or " +
                    "have wrong values", page.getChannelType(), actualValues.size());
        }
        return this;
    }


    private Function<Page, List<String>> getPrivatePaymentProvidersByCountry = page -> {

        List<String> result;
        if(page.getCountry()== SE) result = privateSweden.get();
        else if (page.getCountry() == NO) result = privateNorway.get();
        else if (page.getCountry() == FI) result = privateFinland.get();
        else result = privateDenmark.get();

        return result;
    };

    private Function<Page, List<String>> getCompanyPaymentProvidersByCountry = page -> {

        List<String> result;
        if(page.getCountry()== SE) result = companySweden.get();
        else if (page.getCountry() == NO) result = companyNorway.get();
        else if (page.getCountry() == FI) result = companyFinland.get();
        else result = companyDenmark.get();

        return result;
    };

    private static Supplier<List<String>> privateSweden = ()-> Arrays.asList("SVEA_CARD", "SVEA_DIRECT_BANK");

    private static Supplier<List<String>> companySweden = ()-> Arrays.asList("SVEA_CARD","SVEA_DIRECT_BANK", "SVEA_INVOICE");

    private static Supplier<List<String>> privateNorway = ()-> Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");

    private static Supplier<List<String>> companyNorway = ()-> Arrays.asList("SVEA_CARD"); //, "SVEA_INVOICE"

    private static Supplier<List<String>> privateFinland = ()-> Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "PAYTRAIL_DIRECT_BANK", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");

    private static Supplier<List<String>> companyFinland = ()-> Arrays.asList("SVEA_CARD", "SVEA_INVOICE", "PAYTRAIL_DIRECT_BANK");

    private static Supplier<List<String>> privateDenmark = ()-> Arrays.asList("SVEA_CARD");

    private static Supplier<List<String>> companyDenmark = ()-> Arrays.asList("SVEA_CARD");

}
