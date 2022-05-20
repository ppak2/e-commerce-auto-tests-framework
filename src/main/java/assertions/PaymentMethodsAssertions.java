package assertions;

import pages.Page;

import java.util.Arrays;
import java.util.List;

import static pages.ChannelType.Country.FI;
import static pages.ChannelType.Country.NO;
import static pages.ChannelType.Country.SE;

@Deprecated
public class PaymentMethodsAssertions {


    public boolean assertPrivateWorkflowPayMethods(Page page, List<String> actualValues) {

        List<String> expectedValues = definePrivatePaymentProvidersByCountry(page);

        return actualValues.size() == expectedValues.size() && actualValues.containsAll(expectedValues);

    }

    public boolean assertCompanyWorkflowPayMethods(Page page, List<String> actualValues) {

        List<String> expectedValues = defineCompanyPaymentProvidersByCountry(page);

        return actualValues.size() == expectedValues.size() && actualValues.containsAll(expectedValues);
    }

    private List<String> definePrivatePaymentProvidersByCountry(Page page) {

        List<String> result;

        if(page.getCountry()== SE) result = privateSweden();
        else if (page.getCountry() == NO) result = privateNorway();
        else if (page.getCountry() == FI) result = privateFinland();
        else result = privateDenmark();

        return result;
    }

    private List<String> defineCompanyPaymentProvidersByCountry(Page page) {

        List<String> result;

        if(page.getCountry()== SE) result = companySweden();
        else if (page.getCountry() == NO) result = companyNorway();
        else if (page.getCountry() == FI) result = companyFinland();
        else result = companyDenmark();

        return result;
    }

    private static List<String> privateSweden() {

        return Arrays.asList("SVEA_CARD","SVEA_DIRECT_BANK");
    }

    private static List<String> companySweden() {

        return Arrays.asList("SVEA_CARD","SVEA_DIRECT_BANK", "SVEA_INVOICE");
    }

    private static List<String> privateNorway() {

        return Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");
    }

    private static List<String> companyNorway() {

        return Arrays.asList("SVEA_CARD"); //, "SVEA_INVOICE"
    }

    private static List<String> privateFinland() {

        return Arrays.asList("SVEA_CARD","SVEA_PAYMENT_PLAN", "SVEA_INVOICE", "PAYTRAIL_DIRECT_BANK", "SVEA_PAYMENT_PLAN_BUY_NOW_PAY_LATER");
    }

    private static List<String> companyFinland() {

        return Arrays.asList("SVEA_CARD", "SVEA_INVOICE", "PAYTRAIL_DIRECT_BANK");
    }

    private static List<String> privateDenmark() {

        return Arrays.asList("SVEA_CARD");
    }

    private static List<String> companyDenmark() {

        return Arrays.asList("SVEA_CARD");
    }
}
