package SamplesTests;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPage;
import pages.ProductPage;
import pages.SamplesPage;

public class SamplesTest extends BaseTest {

    private SamplesPage samplesPage;

    @Parameters({"url", "item"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void validateSamplesPopUp(String url, String item) {

        new IndexPage(url).searchAndSelectSingleItem(item);
        samplesPage = new SamplesPage()
                .findAndClickSamplesLink()
                .validateSamplesPopUp();

    }

    @Test(priority = 1)
    public void validateSamplePreview() {

        samplesPage.validateSamplePreview();
    }

    @Parameters({"postcode"})
    @Test(priority = 2)
    public void fillOrderFormAndSubmit(String postcode){

        samplesPage.fillOrderFormAndSubmit(postcode);
    }
}
