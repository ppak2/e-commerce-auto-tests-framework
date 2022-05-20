package base;

import _pages.Page;
import browsers.*;
import browserstack.TestRun;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.vavr.Tuple2;
import lombok.extern.java.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Optional;

import static config.BrowserStackConfig.CONFIG;
import static io.vavr.API.*;

@Log
public abstract class BaseTest {

    private String testName;
    private String buildName;
    private final String tempTestName = "Unknown Build";
    private final String tempBuildName = "Unknown Test";
    private static final String PATTERN = "dd-MMM-YY HH:mm";
    private static final String DATE = new SimpleDateFormat(PATTERN).format(Calendar.getInstance().getTime());
    private static final String URL = "https://" + CONFIG.userName() + ":" + CONFIG.key() + "@hub-cloud.browserstack.com/wd/hub";


    @BeforeTest(alwaysRun = true)
    public void testStart(XmlTest xmlTest) {

        String currentTestName = xmlTest.getName();
        String currentBuildName = xmlTest.getSuite().getName()+" "+ DATE;

        testName = Optional.of(currentTestName).orElseGet(()->tempTestName);
        buildName = Optional.of(currentBuildName).orElseGet(()->tempBuildName);

        final Map<String, String> parameters = xmlTest.getAllParameters();
        final Browser browser = getBrowser(parameters.get("browser").toLowerCase());

        parameters.put("name", testName);
        parameters.put("build", buildName);

        configureDriver(browser, parameters);

        TestRun.updateTestRunStatus(buildName);
        Tuple2<String, String> t =  TestRun.getTestSessionsUrls(buildName, testName);
        TestRun.getTestSessionUrls(t);
    }

    @AfterTest(alwaysRun = true)
    public void testFinish() {

        Page.getLogs();
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
                Case($("safari mobile"), SafariMobile::new)
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
}

