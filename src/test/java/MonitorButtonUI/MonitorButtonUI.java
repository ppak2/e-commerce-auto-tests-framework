package MonitorButtonUI;

import base.BaseTest;
import listeners.RetryAnalyzer;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.IndexPage;
import pages.ProductPage;

public class MonitorButtonUI extends BaseTest {


    @Parameters({"url", "item"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItem(String url, String item) {

        new IndexPage(url).searchForMonitoringProduct(item);

        ProductPage productPage = new ProductPage();
        productPage.validateMonitorButtonSection();

    }

}
