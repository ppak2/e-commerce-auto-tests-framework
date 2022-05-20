package experiment;

import _pages.CheckOutPage;
import _pages.IndexPage;
import _pages.ProductDetailsPage;
import _pages.ProductListPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import listeners.RetryAnalyzer;
import lombok.extern.java.Log;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;
import static repository.ElementSelectors.IndexPageSelectors.scriptABTestTag;

@Log
public class GridTestSingle {

    private int testRepeat;
    private int counter;
    private Map<String, Map<String, Integer>> testContainer;

    @BeforeClass
    @Parameters({"testRepeat"})
    public void setUp(String testRepeat){
        this.testRepeat = Integer.valueOf(testRepeat);
        counter = this.testRepeat+1;
        testContainer = new HashMap<>();

        Configuration.browser = "chrome";
//        Configuration.headless = true;
        Configuration.startMaximized = true;
        Configuration.remote = "http://104.155.58.144:80/wd/hub";
    }

    @AfterClass(alwaysRun = true)
    public void getABReport(){
        attachCollectDataToReport();
    }


    @Severity(SeverityLevel.NORMAL)
    @Parameters({"url"})
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void searchForItemAndAddToCart(String url){

        while (testRepeat > 0) {

            log.info(">>> CURRENT ITERATION № " + (counter - testRepeat));
            IndexPage indexPage = new IndexPage(url, "mac");

            ProductListPage productListPage = indexPage.searchItem();

            ProductDetailsPage productDetailsPage = productListPage.proceedToProductDetailsPage();

//            productDetailsPage.addItemToCartAndGoToCheckOut()
//                    .completePrivateForm()
////                    .submitOrder();
//
//            collectABTestData();

            testRepeat--;
        }
    }

    private void collectABTestData() {

        Optional<String> jsBodyValue = Optional.ofNullable(scriptABTestTag.getAttribute("innerHTML"));
        String dataLayer = jsBodyValue.orElseGet(() -> "");

        if (dataLayer.isEmpty()) {

            throw new RuntimeException("DataLayer script body is empty");

        } else {

            String experimentIdPattern = "(experimentId)\":\"([^\"]*)";
            String experimentVariantPattern = "(experimentVariant)\":(\\d)";

            Pattern pattern1 = Pattern.compile(experimentIdPattern);
            Pattern pattern2 = Pattern.compile(experimentVariantPattern);

            Matcher matcher1 = pattern1.matcher(dataLayer);
            Matcher matcher2 = pattern2.matcher(dataLayer);

            while (matcher1.find() && matcher2.find()) {

                String experimentId = matcher1.group(2);

                if (!testContainer.containsKey(experimentId)) {

                    Map<String, Integer> experimentVariantData = new HashMap<>();
                    experimentVariantData.put("experimentVariant:0", 0);
                    experimentVariantData.put("experimentVariant:1", 0);

                    if (matcher2.group(2).matches("0")){
                        experimentVariantData.put("experimentVariant:0",1);
                    }
                    else {
                        experimentVariantData.put("experimentVariant:1",1);
                    }

                    testContainer.put(experimentId, experimentVariantData);

                } else {

                    Integer counterVar;

                    if (matcher2.group(2).matches("0")) {

                        counterVar = testContainer.get(experimentId).get("experimentVariant:0");
                        testContainer.get(experimentId).put("experimentVariant:0", ++counterVar);
                    } else {

                        counterVar = testContainer.get(experimentId).get("experimentVariant:1");
                        testContainer.get(experimentId).put("experimentVariant:1", ++counterVar);
                    }
                }
            }
        }
    }

    @Attachment(value = "A/B Test data")
    private String attachCollectDataToReport() {

        StringJoiner sj = new StringJoiner("\n");
        sj.add("+----------------- Result -----------------+");
        testContainer.forEach((stringKey, map)->{
            sj.add("ExperimentId: "+stringKey);
            sj.add("experimentVariant_0: "+map.get("experimentVariant:0"));
            sj.add("experimentVariant_1: "+map.get("experimentVariant:1"));
        });
        sj.add("+------------------------------------------+");
        return sj.toString();
    }

}
