package pages;

import com.codeborne.selenide.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.Color;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;
import static repository.ElementSelectors.IndexPageSelectors.cookieButton;
import static repository.ElementSelectors.IndexPageSelectors.iFrameCloseButton;

public abstract class Page {

    final String SCREENSHOT_FILEPATH = "target/pdf_screenshots";
    String currentUrl;
    ChannelType channelType;
    ChannelType.Country country;
    WebDriver currentDriver;

    Page() {

        System.out.println("*-*- " + "Page constructor" + " -*-*");
        channelType = ChannelType.defineChannelType(WebDriverRunner.getWebDriver().getCurrentUrl());
        country = ChannelType.Country.defineCountry(WebDriverRunner.getWebDriver().getCurrentUrl());
        currentDriver = WebDriverRunner.getWebDriver();
    }

    Page(String url) {

        currentUrl = url;
        channelType = ChannelType.defineChannelType(url);
        country = ChannelType.Country.defineCountry(url);
    }

    int waitTimeout() {
        return 25000;
    }

    public String url() {
        return currentUrl;
    }

    public ChannelType getChannelType() {

        return this.channelType;
    }

    public ChannelType.Country getCountry() {

        return this.country;
    }

    protected boolean validateElementsAttributes(ElementsCollection elements, String attributeName, String attributeValue) {

        boolean correct;

        List<SelenideElement> extractedElements = elements.stream()
                .filter(SelenideElement::exists)
                .filter(el -> !el.getCssValue("visibility").equals("hidden")).collect(Collectors.toList());

        correct = extractedElements.stream().map(el -> el.getCssValue(attributeName))
                .map(col -> Color.fromString(col).asHex())
                .allMatch(attr -> attr.equals(attributeValue));

        return correct;
    }

    public void scrollToFooter(){

        $x("//footer").scrollIntoView(false);
    }

    void checkForIFrame(SelenideElement iFrame, SelenideElement closeButton) {

        if (iFrameCloseButton.isDisplayed()) iFrameCloseButton.click();

        else {

            Selenide.sleep(2000);
            if (iFrameCloseButton.isDisplayed()) iFrameCloseButton.click();
        }
    }

    String randomEmail() {

        return RandomStringUtils.randomAlphabetic(9) + "@" + RandomStringUtils.randomAlphabetic(4) + ".com";
    }
}
