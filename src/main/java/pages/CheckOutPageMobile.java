package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.HideKeyboardStrategy;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.touch.TouchActions;
import repository.ElementSelectors;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static pages.ChannelType.Country.*;
import static repository.ElementSelectors.CheckOutPageSelectors.*;
import static repository.ElementSelectors.CheckOutPageSelectors.ssnAddressButton;
import static repository.ElementSelectors.SampleSelectors.firstName;
import static repository.ElementSelectors.SampleSelectors.lastName;

public class CheckOutPageMobile extends CheckOutPage {

    private AppiumDriver apd;   //refactor
    private String platformName; //refactor

    public CheckOutPageMobile() {

        apd = (AppiumDriver) currentDriver;
        this.platformName = apd.getPlatformName();
        System.out.println("*-*-*-*-* " + "ChPageMobile constructor" + " *-*-*-*-*");
    }

    @Override
    public CheckOutPageMobile fillSsnAndSelectAddress(String ssn) {

//        addressLinkCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false);

        if (platformName.equalsIgnoreCase("android")) {
            ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();

            apd.hideKeyboard();

            ssnPopUpFrame.waitUntil(appear, waitTimeout());
            ssnAddressButton.waitUntil(appear, waitTimeout()).click();
        } else {
            ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false);
            ssnField.click();
            apd.getKeyboard().sendKeys(ssn);
            ssnSubmitButton.click();

            ssnPopUpFrame.waitUntil(appear, waitTimeout());
            ssnAddressButton.waitUntil(appear, waitTimeout()).click();
        }

        return this;
    }

    @Override
    public CheckOutPageMobile fillFormFieldPrivate(String value1, String value2) {

        System.out.println("====> POLYMORPH CALL");

        if (platformName.equalsIgnoreCase("android")) {
            addressFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(value1).pressTab();
            postcodeFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(value2).pressEnter();
            apd.hideKeyboard();
        } else {
            addressFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).sendKeys(value1);
            postcodeFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).sendKeys(value2);
            postcodeFieldPrivate.pressEnter();
            phoneFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
        }
        return this;
    }

    @Override
    public CheckOutPageMobile getPayMethodValues() {

        System.out.println("====>>> POLYMORPH CALL");

        final List<SelenideElement> temp = new ArrayList<>();
        payMethods.iterator().forEachRemaining(temp::add);
        payMethodsList = temp.stream().map(el -> el.getAttribute("value")).collect(Collectors.toList());
        payMethodsList.forEach(System.out::println);
        return this;
    }

    @Override
    public CheckOutPageMobile norwayFillSsn(String ssn) {

        if (platformName.equals("android")) {
            super.norwayFillSsn(ssn);
        } else {

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            addressLinkCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false);
            ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
            apd.getKeyboard().sendKeys(ssn);
            ssnSubmitButton.click();
        }
        return this;
    }

    @Override
    public CheckOutPageMobile fillAndValidateAddressFormPrivate(String address, String postcode, String phone) {

       if(platformName.equalsIgnoreCase("android")) super.fillAndValidateAddressFormPrivate(address, postcode, phone);

       else {
           goToKassan.waitUntil(appear, waitTimeout()).scrollIntoView(false);
           executeJavaScript("arguments[0].click()", goToKassan);
           if (country == SE) openAddressFormPrivate();

           firstName.click();
           apd.getKeyboard().sendKeys("Karl");
           lastName.click();
           apd.getKeyboard().sendKeys("Mobelkung");
           addressFieldPrivate.click();
           apd.getKeyboard().sendKeys(address);
           postcodeFieldPrivate.click();
           apd.getKeyboard().sendKeys(postcode);

//           if(country == FI || country == DK) {
//               cityElements.first().click();
//               apd.getKeyboard().sendKeysEnter("Helsinhagen");
//           }
           phoneFieldPrivate.click();
           apd.getKeyboard().sendKeys(phone);
           emailFieldPrivate.click();
           apd.getKeyboard().sendKeys(randomEmail());
           phoneFieldPrivate.click();

           stageCircle.shouldHave(text("1"));
           goToDelivery.waitUntil(appear, waitTimeout()).scrollIntoView(false);
           executeJavaScript("arguments[0].click()", goToDelivery);
       }

        return this;
    }

    @Override
    public CheckOutPageMobile fillAndValidateAddressFormCompany(String ssn, String phone){

        System.out.println("-*- " + "Address form company checkout" + " -*-");

        goToKassan.waitUntil(appear, waitTimeout());
        executeJavaScript("arguments[0].click()", goToKassan);
        selectCompanyTab();

        if(country == SE) fillAndValidateSsnSweden(ssn,phone);
        else if(country == NO) fillAndValidateSsnNorway(ssn,phone);
        else fillAndValidateSsnFinDen(ssn,phone);

        stageCircle.shouldHave(text("1"));
        goToDelivery.waitUntil(appear,waitTimeout());
        executeJavaScript("arguments[0].click()", goToDelivery);

        return this;
    }

    @Override
    protected void fillAndValidateSsnSweden(String ssn,String phone){

        if(platformName.equalsIgnoreCase("android")) super.fillAndValidateSsnSweden(ssn, phone);

        else{

            ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false);
            ssnField.click();
            apd.getKeyboard().sendKeys(ssn);
            ssnSubmitButton.click();
            ssnPopUpFrame.waitUntil(appear, waitTimeout());
            ssnAddressButton.waitUntil(appear, waitTimeout()).click();

            ElementSelectors.SampleSelectors.phone.waitUntil(appear, waitTimeout()).click();
            apd.getKeyboard().sendKeys(phone);

            ElementSelectors.SampleSelectors.email.waitUntil(appear, waitTimeout()).click();
            apd.getKeyboard().sendKeys(randomEmail());

            ElementSelectors.SampleSelectors.phone.click();

        }

    }

    @Override
    protected void fillAndValidateSsnNorway(String ssn, String phone){

        if(platformName.equalsIgnoreCase("android")) super.fillAndValidateSsnNorway(ssn, phone);

        else {

            ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false);
            ssnField.click();
            apd.getKeyboard().sendKeys(ssn);

            ssnSubmitButton.click();
            nonSeSsnControlField.waitUntil(appear, waitTimeout());
            ssnAddressFormLink.waitUntil(appear, waitTimeout());
            executeJavaScript("arguments[0].click()", ssnAddressFormLink);

            postcodeFieldCompany.click();
            apd.getKeyboard().sendKeys("0170");

            phoneElements.last().click();
            apd.getKeyboard().sendKeys(phone);

            emailElements.last().click();
            apd.getKeyboard().sendKeys(randomEmail());

            phoneElements.last().click();

        }

    }

    @Override
    protected void fillAndValidateSsnFinDen(String ssn, String phone){

        System.out.println("-*- " + "SsnFinDen CheckoutMobile" + " -*-");
        System.out.println("-*- " + platformName + " -*-");

        if(platformName.equalsIgnoreCase("android")) super.fillAndValidateSsnFinDen(ssn, phone);

        else {

            System.out.println("-*- " + "SsnFinDen Checkout IPhone" + " -*-");

            ElementsCollection ssnC = $$x("//input[@id='ssn']");

            if(ssnC.last().exists()) {

                ssnC.last().click();
                apd.getKeyboard().sendKeys("02012566");

            }

            companyNameField.click();
            apd.getKeyboard().sendKeys("Kompany");

            addressFieldCompany.click();
            apd.getKeyboard().sendKeys("Stad 1");

            postcodeFieldCompany.click();

//            if(country == FI) {
//
//                apd.getKeyboard().sendKeysEnter("00300");
//                cityElements.last().click();
//                apd.getKeyboard().sendKeysEnter("Helsinki");
//            }
//
//            else {
//
//                apd.getKeyboard().sendKeysEnter("1561");
//                cityElements.last().click();
//                apd.getKeyboard().sendKeysEnter("Copenhagen");
//            }

            phoneElements.last().click();
            apd.getKeyboard().sendKeys(phone);

            emailElements.last().click();
            apd.getKeyboard().sendKeys(randomEmail());

            phoneElements.last().click();

        }

    }
}
