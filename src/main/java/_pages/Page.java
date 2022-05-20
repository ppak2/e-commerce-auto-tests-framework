package _pages;

import _pages.Actions.Actions;
import _pages.enums.ChannelType;
import _pages.enums.PlatformType;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import utils.PropertiesLoader;

import java.util.EnumMap;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static _pages.enums.PlatformType.MacOS;
import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.Selenide.*;

@Log
public abstract class Page {

    @Getter
    @Setter
    WebDriver currentDriver;

    @Getter
    @Setter
    ChannelType channelType;

    @Getter
    @Setter
    ChannelType.Country country;

    @Getter
    @Setter
    PlatformType platformType;

    @Getter
    @Setter
    String indexPageUrl;

    @Getter
    @Setter
    ChannelType.Environment environment;

    @Getter
    @Setter
    Actions action;

    EnumMap<ChannelType.Country, Properties> inputParameters;

    static final int PAGE_LOAD_COUNTER = 11;

    private static ThreadLocal<StringJoiner> _CONSOLE_LOGS = new ThreadLocal<>();



    protected Page() {}

    Page(String url, String platformName) {

        this.currentDriver = WebDriverRunner.getWebDriver();
        this.channelType = ChannelType.defineChannelType(url);
        this.country = ChannelType.Country.defineCountry(url);
        this.platformType = PlatformType.definePlatformType();
        this.indexPageUrl = processUrl(url);
        this.environment = ChannelType.Environment.defineEnvironment(url);
        this.action = Actions.initActionsObject(platformType);
        this.inputParameters = PropertiesLoader.getChannelsProperties();
        _CONSOLE_LOGS.set(new StringJoiner(""));
    }

    Page(Page page) {

        this.currentDriver = page.getCurrentDriver();
        this.channelType = page.getChannelType();
        this.country = page.getCountry();
        this.platformType = page.getPlatformType();
        this.indexPageUrl = page.getIndexPageUrl();
        this.environment = page.getEnvironment();
        this.action = page.action;
        this.inputParameters = page.inputParameters;
    }

    /**
     * Method for sub-class impl
     * to verify Web Driver is called
     * on the correct Page type
     */
    protected abstract boolean isAt();

    public boolean isMobile() {
        return currentDriver instanceof AppiumDriver;
    }

    void waitForPageToLoad() {

        try {
            if (!this.isAt()) {
                int i = 0;
                while (!this.isAt() && ++i < PAGE_LOAD_COUNTER) {
                    sleep(1000);
                    if (i == 10) throw new RuntimeException("Unable to load Page " + this.getMetaUrl);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to load Page " + this.getMetaUrl);
        }
    }

    Supplier<String> getMetaUrl = () -> $x("html/head/meta[@property='og:url']").getAttribute("content");

    String randomEmail() {
        return RandomStringUtils.randomAlphabetic(9) + "@" + RandomStringUtils.randomAlphabetic(4) + ".com";
    }

    @Attachment(value = "_CONSOLE logs")
    @SuppressWarnings("unused")
    public static String getLogs(){
        return _CONSOLE_LOGS.get().toString();
    }

    void addLogs(String pageLiteral){

        if (platformType.equals(MacOS) || platformType.equals(iOs)){
            _CONSOLE_LOGS.get().add("Logs are not available\n");
        }
        else {
            try {
                _CONSOLE_LOGS.get().add(getPageConsoleLogs(pageLiteral));
            }
            catch (Exception e){e.getMessage();}
        }
    }

    private String getPageConsoleLogs(String pageLiteral) {

        try {
            String logs = currentDriver.manage().logs().get(LogType.BROWSER)
                    .getAll().stream()
                    .filter(logEntry -> logEntry.getLevel().equals(Level.SEVERE))
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n", pageLiteral, "\n"));

            return logs.contentEquals(pageLiteral) ? "No SEVERE logs" : logs;

        }catch (NullPointerException|IllegalArgumentException e){e.printStackTrace();}

        return  "[]";
    }

    private String processUrl(String indexPageUrl){

        String result;

        if(!Character.isLetter(indexPageUrl.charAt(indexPageUrl.length()-1))) {
            result = indexPageUrl.substring(0, indexPageUrl.length()-1);
        }
        else {
            result = indexPageUrl;
        }
        return result;
    }


}
