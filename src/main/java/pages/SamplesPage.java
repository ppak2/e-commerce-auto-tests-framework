package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.SampleSelectors.*;

public class SamplesPage extends Page{


    public SamplesPage (){

    }

    public SamplesPage findAndClickSamplesLink(){

        samplesLink.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", samplesLink);
        samplePopUp.waitUntil(appear, waitTimeout());
        return this;
    }

    public SamplesPage validateSamplesPopUp(){

        sampleItemImage.shouldBe(visible);
        emptyItemCheckbox.shouldBe(visible);
        orderSampleButton.shouldHave(attribute("disabled"));

        return this;
    }

    public void validateSamplePreview(){

        sampleItemImage.click();
        samplePreview.shouldBe(visible);
        arrowNext.waitUntil(appear, waitTimeout());
        actions().clickAndHold(arrowNext).release().perform();
        imageChanged.should(exist);
        arrowPrev.waitUntil(appear, waitTimeout());
        actions().clickAndHold(arrowPrev).release().perform();
        imageChanged.should(not(exist));
        submitButton.click();
        submitButtonIsChecked.should(exist);
        goBackButton.click();
        checkBoxIsChecked.should(exist);
    }

    public void fillOrderFormAndSubmit(String postCode){

        orderSampleButton.waitUntil(appear, waitTimeout()).click();
        orderForm.waitUntil(appear, waitTimeout());
        $x("//div[@class='productInfoSamples--title']").click();
        Selenide.actions().sendKeys(Keys.TAB).perform();
        if(firstName.is(not(visible))) Selenide.actions().sendKeys(Keys.TAB).perform();
        firstName.setValue("Automatic").pressTab();
        lastName.setValue("Test").pressTab();
        address.setValue("Autotest Boulevard").pressTab();
        postcode.setValue(postCode).pressTab().pressTab();
        phone.setValue("1234567890").pressTab();
        email.setValue(randomEmail());
        submitOrderButton.shouldHave(attribute("disabled"));
        checkbox.click();
        submitOrderButton.click();
        confirmPopUp.waitUntil(appear, waitTimeout());
        confirmPopUpButton.click();
        confirmPopUp.shouldNotBe(visible);

    }

    public void validateSamplePreviewMobile(){

        sampleItemImage.click();
        samplePreview.shouldBe(visible);
        arrowNext.waitUntil(appear, waitTimeout()).click();
        sleep(3000);
        imageChanged.should(exist);
        arrowPrev.waitUntil(appear, waitTimeout()).click();
        imageChanged.should(not(exist));
        submitButton.click();
        submitButtonIsChecked.should(exist);
        goBackButton.click();
        checkBoxIsChecked.should(exist);
    }

    //works for Android
    public void fillOrderFormAndSubmitMobile(String postCode){

        orderSampleButton.waitUntil(appear, waitTimeout()).click();
        orderForm.waitUntil(appear, waitTimeout());
        $x("//div[@class='productInfoSamples--title']").click();
        Selenide.actions().sendKeys(Keys.TAB).perform();
        if(firstName.is(not(visible))) Selenide.actions().sendKeys(Keys.TAB).perform();
        firstName.setValue("Automatic").pressTab();
        lastName.setValue("Test").pressTab();
        address.setValue("Autotest Boulevard").pressTab();
        postcode.setValue(postCode).pressTab().pressTab();
        phone.setValue("1234567890").pressTab();
        email.setValue(randomEmail());
        submitOrderButton.shouldHave(attribute("disabled"));
        checkbox.click();
        submitOrderButton.click();
        confirmPopUp.waitUntil(appear, waitTimeout());
        confirmPopUpButton.click();
        confirmPopUp.shouldNotBe(visible);
    }
}
