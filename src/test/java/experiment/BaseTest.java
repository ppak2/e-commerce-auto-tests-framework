package experiment;

import browsers.*;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import lombok.extern.java.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.vavr.API.*;
import static repository.ElementSelectors.IndexPageSelectors.scriptABTestTag;

@Log
public abstract class BaseTest {

    private static final Map<String, Map<String, Integer>> testContainer = Collections.synchronizedMap(new HashMap<>());
    private String URL;
    private final String tempTestName = "Unknown Build";
    private final String tempBuildName = "Unknown Test";
    private static final String PATTERN = "dd-MMM-YY HH:mm";
    private static final String DATE = new SimpleDateFormat(PATTERN).format(Calendar.getInstance().getTime());
//    private static final String URL = "http://localhost:4444/wd/hub";



    @BeforeTest(alwaysRun = true)
    public void testStart(XmlTest xmlTest) {

        String currentTestName = xmlTest.getName();
        String currentBuildName = xmlTest.getSuite().getName()+" "+ DATE;

        String testName = Optional.of(currentTestName).orElseGet(()->tempTestName);
        String buildName = Optional.of(currentBuildName).orElseGet(()->tempBuildName);

        final Map<String, String> parameters = xmlTest.getAllParameters();
        final Browser browser = getBrowser(parameters.get("browser").toLowerCase());
        URL = parameters.get("hubUrl");

        parameters.put("name", testName);
        parameters.put("build", buildName);

        configureDriver(browser, parameters);

    }

    @AfterTest(alwaysRun = true)
    public void testFinish() {

        attachCollectDataToReport();
        WebDriverRunner.getWebDriver().close();
        WebDriverRunner.getWebDriver().quit();
    }

    private Browser getBrowser(String browserName) {
        return Match(browserName).of(
                Case($("chrome"), Chrome::new),
                Case($("safari"), Safari::new),
                Case($("internet explorer"), InternetExplorer::new),
                Case($("edge"), Edge::new),
                Case($("chromium"), Chromium::new),
                Case($("safari mobile"), SafariMobile::new),
                Case($("ff"), Firefox::new)
        );
    }

    private void configureDriver(final Browser browser, Map<String, String> params) {

        final WebDriver driver;

        if (!browser.isMobile()) {
            try {
                driver = new RemoteWebDriver(new URL(URL), browser.capabilities(params));
                driver.manage().window().maximize();
                log.info("-----REMOTE DRIVER IS CREATED-----");

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                driver = new AppiumDriver<>(new URL(URL), browser.capabilities(params));
                log.info("-----APPIUM DRIVER IS CREATED-----");

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        WebDriverRunner.setWebDriver(driver);
    }

    protected void collectABTestData() {

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
