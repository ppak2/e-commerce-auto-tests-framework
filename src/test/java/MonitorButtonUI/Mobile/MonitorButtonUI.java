package MonitorButtonUI.Mobile;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPageMobile;
import pages.ProductPageMobile;

public class MonitorButtonUI extends BaseTest {

    @Parameters({"url", "item", "platform"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItemAndValidateMonitorButton(String url, String item, String platform) {

        new IndexPageMobile(url).searchForMonitoringProduct(item, platform);
        new ProductPageMobile().validateMonitorButtonSection();
    }
}
