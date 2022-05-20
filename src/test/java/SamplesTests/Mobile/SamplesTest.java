package SamplesTests.Mobile;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPageMobile;
import pages.SamplesPage;

public class SamplesTest extends BaseTest {

    private SamplesPage samplesPage;

    @Parameters({"url", "item"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void validateSamplesPopUp(String url, String item) {

        new IndexPageMobile(url).searchAndSelectSingleItem(item);
        samplesPage = new SamplesPage()
                .findAndClickSamplesLink()
                .validateSamplesPopUp();
    }

    @Test(priority = 1, alwaysRun = true)
    public void validateSamplePreview() {

        samplesPage.validateSamplePreviewMobile();
    }

    @Parameters({"postcode"})
    @Test(priority = 2, alwaysRun = true)
    public void fillOrderFormAndSubmit(String postcode){

        samplesPage.fillOrderFormAndSubmitMobile(postcode);
    }
}
