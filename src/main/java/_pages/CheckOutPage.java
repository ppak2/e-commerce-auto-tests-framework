package _pages;

import _pages.Actions.MobileActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static _pages.enums.ChannelType.Country.*;
import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.CheckOutPageSelectors.*;
import static repository.ElementSelectors.SampleSelectors.firstName;
import static repository.ElementSelectors.SampleSelectors.lastName;

@Log
public class CheckOutPage extends Page {


    public List<String> payMethodsList = new ArrayList<>();

    @Getter
    private List<String> privatePayMethods = new ArrayList<>();
    @Getter
    private List<String> companyPayMethods = new ArrayList<>();
    @Getter
    private String checkOutPageUrl;
    @Getter
    private String addressStageUrl;

    private String deliveryStageUrl;
    @Getter
    private String paymentStageUrl;
    @Getter
    private String address;
    @Getter
    private String postcode;
    @Getter
    private String phone;
    @Getter
    private String ssn;

    public CheckOutPage() {
    }

    public CheckOutPage(Page page) {

        super(page);
        checkOutPageUrl = page.getIndexPageUrl() + "/checkout/cart";
        addressStageUrl = page.getIndexPageUrl() + "/checkout/address";
        deliveryStageUrl = page.getIndexPageUrl() + "/checkout/delivery";
        paymentStageUrl = page.getIndexPageUrl() + "/checkout/payment";
        addLogs("<CheckOut page>: ");
        address = inputParameters.get(this.country).getProperty("address");
        postcode = inputParameters.get(this.country).getProperty("postcode");
        phone = inputParameters.get(this.country).getProperty("phone");
        ssn = inputParameters.get(this.country).getProperty("ssn");
    }

    @Override
    protected boolean isAt() {

        return checkOutPageUrl.equals(getMetaUrl.get());
    }

    @SuppressWarnings("unused")
    public CheckOutPage getPayMethodValues() {

        log.info("Entering CheckOut page Get PayMethod values");
        waitForStageToLoad(paymentStageUrl);
        payMethods.shouldHave(sizeGreaterThan(0));

        if(privatePayMethods.size() == 0){
            privatePayMethods = payMethods
                .stream()
                .map(el -> el.getAttribute("value")).collect(Collectors.toList());
            System.out.println("***************************");
            privatePayMethods.forEach(System.out::println);
            System.out.println("***************************");
        }
        else{
            companyPayMethods = payMethods
                    .stream()
                    .map(el -> el.getAttribute("value")).collect(Collectors.toList());
            System.out.println("***************************");
            companyPayMethods.forEach(System.out::println);
            System.out.println("***************************");
        }
        addLogs("<CheckOut Payment stage>: ");
        return this;
    }

    public CheckOutPage completePrivateForm() {
        action.click(goToKassan);
        if (country == SE) openAddressFormPrivate();

        if (country == FI || country == NO) {
            action.hoverAndClick(ssnField);
            action.sendKeys(ssnField, "15055514");
        }
        action.hoverAndClick(firstName); //put cursor into the field

        action.sendKeysTab(firstName, "Karl");

        action.sendKeysTab(lastName, "Mobelkung");

        action.sendKeysTab(addressFieldPrivate, address);

        action.sendKeysTab(postcodeFieldPrivate, postcode);

        sleep(3000);

        action.hoverAndClick(phoneFieldPrivate); //put cursor into the field

        action.sendKeysTab(phoneFieldPrivate, phone);

        if(platformType == iOs) MobileActions.iOsSingleScroll();

        if(platformType == iOs){ action.sendKeys(emailFieldPrivate, randomEmail()); }
        else { action.sendKeysTab(emailFieldPrivate, randomEmail()); }

        if(platformType == iOs) MobileActions.iOsSingleScroll();

        action.hoverAndClick(phoneFieldPrivate);
        phoneFieldPrivate.pressEnter();

//        if (isAtStage.test(addressStageUrl)) {
//            if(goToDelivery.isDisplayed()) action.click(goToDelivery);
//        }

//        waitForStageToLoad(addressStageUrl);
//        action.click(goToDelivery);
//        sleep(2000);

        validateDeliveryServices();
        return this;
    }

    public CheckOutPage completeCompanyForm() {
        action.click(goToKassan);
        selectCompanyTab();
        if (country == SE) swedenFillSsn(ssn, phone);
        else if (country == NO) norwayFillSsn(ssn, phone);
        else finDenFillSsn(ssn, phone);

        sleep(3000);

        waitForStageToLoad(addressStageUrl);
        action.click(goToDelivery);
        sleep(2000);

        validateDeliveryServices();
        return this;
    }

    @SuppressWarnings("unused")
    public CheckOutPage submitOrder() {

        String visa = "4916-4232-3977-8102";
        String mastercard = "5392-1273-3201-0533";

        int i = new Random().nextInt(2);

        String cardValue = i == 0 ? visa : mastercard;

//        waitForStageToLoad(paymentStageUrl);
        payMethods.shouldHave(sizeGreaterThan(0));

        action.click(radioButton_SVEA_CARD);
        action.click(goToSubmitOrder);

        action.sendKeysTab(cardNumberField, cardValue);
        action.sendKeysTab(expMonthField, "10");
        action.sendKeysTab(expYearField, "22");
        action.sendKeysTab(cvvField, "987");

        action.click(submitCardInfo);
//        sleep(10000);
//        Selenide.refresh();

        thankYouSection.shouldBe(Condition.visible);
        sleep(5000);
//        Selenide.clearBrowserCookies();
//        WebDriverRunner.clearBrowserCache();

        return this;
    }

    private void validateDeliveryServices() {
        if (country == NO || country == SE) {
//            waitForStageToLoad(deliveryStageUrl);
            sleep(3000);

            action.click(goToPayment);
        }
    }

    public CheckOutPage returnToCartStage(){

        open(checkOutPageUrl);
        return this;
    }

    void openAddressFormPrivate() {
        action.click(addressLinkPrivate);
    }

    private void selectCompanyTab() {
        action.click(companyTab);
    }

    private void norwayFillSsn(String ssn, String phone) {

        if(platformType == iOs) {
            AppiumDriver appiumDriver = (AppiumDriver) this.getCurrentDriver();
            action.iOsSsnFieldType(appiumDriver, ssn);
        }
        else {
            action.hoverAndClick(ssnField);
            action.sendKeysEnter(ssnField, ssn);
        }
        sleep(3000);
        if (ssnSubmitButton.exists()) ssnSubmitButton.click();

        action.click(ssnAddressFormLink);

        action.hoverAndClick(phoneFieldPrivate);
        action.sendKeysTab(phoneFieldPrivate, phone);
        sleep(2000);

        MobileActions.iOsSingleScroll();

        action.sendKeysTab(emailFieldPrivate, randomEmail());

        if (platformType == iOs) phoneFieldPrivate.click();
    }

    private void swedenFillSsn(String ssn, String phone) {

        if(platformType == iOs) {
            AppiumDriver appiumDriver = (AppiumDriver) this.getCurrentDriver();
            action.iOsSsnFieldType(appiumDriver, ssn);
        }
        else {
            action.hoverAndClick(ssnField);
            action.sendKeysEnter(ssnField, ssn);   //ENTER
        }

        sleep(3000);

        if (ssnSubmitButton.exists()) action.click(ssnSubmitButton); //SSN SUBMIT ?

        action.click(ssnAddressButton);

        action.hoverAndClick(phoneFieldPrivate);
        action.sendKeysTab(phoneFieldPrivate, phone);
        sleep(2000);

        MobileActions.iOsSingleScroll();

        action.sendKeysTab(emailFieldPrivate, randomEmail());

        if (ssnPopUpCloseBtn.isDisplayed()) action.click(ssnPopUpCloseBtn);
        if (platformType == iOs) phoneFieldPrivate.click();
    }

    private void finDenFillSsn(String ssn, String phone) {

        action.hoverAndClick(companyNameField);
        action.sendKeysTab(companyNameField, "Kompany");
        action.sendKeysTab(addressFieldCompany, "Stad 1");

        if (country == FI) {
            action.sendKeysTab(postcodeFieldPrivate, "00300");
        } else {
            action.sendKeysTab(postcodeFieldPrivate, "1561");
        }
        action.hoverAndClick(phoneFieldPrivate);
        action.sendKeysTab(phoneFieldPrivate, phone);
        action.sendKeysTab(emailFieldPrivate, randomEmail());
        if (platformType == iOs) phoneFieldPrivate.click();
    }

    private void waitForStageToLoad(String stageUrl) {

        try {
            if (!isAtStage.test(stageUrl)) {
                log.info("Waiting for " + stageUrl + " to load");
                int i = 0;
                while (!isAtStage.test(stageUrl) && ++i < PAGE_LOAD_COUNTER) {
                    sleep(1000);
                    if (i == 10) throw new RuntimeException("Unable to load stage " + stageUrl);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to load stage " + stageUrl);
        }
    }

    Predicate<String> isAtStage = stageUrl -> stageUrl.equals(currentDriver.getCurrentUrl());

}
