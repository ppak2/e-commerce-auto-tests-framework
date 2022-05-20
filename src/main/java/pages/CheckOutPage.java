package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import repository.ElementSelectors;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static pages.ChannelType.Country.*;
import static repository.ElementSelectors.CheckOutPageSelectors.*;
import static repository.ElementSelectors.SampleSelectors.firstName;
import static repository.ElementSelectors.SampleSelectors.lastName;

@SuppressWarnings("never used")
public class CheckOutPage extends Page {

    public List<String> payMethodsList;
    public String zipCodeFieldLength;

    public CheckOutPage() {

        System.out.println("*-*-*- " + "CheckoutPage constructor" + " -*-*-*");
        System.out.println("-*- " + country + " -*-");
    }


    public CheckOutPage getPayMethodValues() {

        final List<SelenideElement> temp = new ArrayList<>();
        payMethods.iterator().forEachRemaining(temp::add);
        payMethodsList = temp.stream().filter(el->el.getAttribute("type").equals("radio"))
                .map(el -> el.getAttribute("value")).collect(Collectors.toList());
        payMethodsList.forEach(System.out::println);
        listOfPaymentMethods(payMethodsList);
        makeScreenshot2();
        return this;
    }

    public CheckOutPage openAddressFormPrivate() {
        addressLinkPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
        emailFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        return this;
    }

    public CheckOutPage selectCompanyTab() {
        companyTab.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
        return this;
    }

    public CheckOutPage fillFormFieldCompany(String value1, String value2) {
        addressFieldCompany.waitUntil(appear, waitTimeout()).setValue(value1).pressTab();
        postcodeFieldCompany.waitUntil(appear, waitTimeout()).setValue(value2).pressTab();
        sleep(2000);
        return this;
    }

    public CheckOutPage fillFormFieldPrivate(String value1, String value2) {
        addressFieldPrivate.waitUntil(appear, waitTimeout()).setValue(value1).pressTab();
        postcodeFieldPrivate.waitUntil(appear, waitTimeout()).setValue(value2).pressTab();
        sleep(2000);
        return this;
    }

    public CheckOutPage scrollToAddressFormPrivate() {
        emailFieldPrivate.waitUntil(appear, waitTimeout()).scrollIntoView(true);
        return this;
    }

    public CheckOutPage scrollToAddressFormCompany() {
        emailFieldCompany.waitUntil(appear, waitTimeout()).scrollIntoView(true);
        return this;
    }

    public CheckOutPage openAddressFormCompany() {
        addressLinkCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false).click();
        emailFieldCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        sleep(2000);
        return this;
    }

    public CheckOutPage fillSsnAndSelectAddress(String ssn) {
        addressLinkCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();

//        if(WebDriverRunner.getWebDriver() instanceof AppiumDriver){
//            AppiumDriver apd = (AppiumDriver)WebDriverRunner.getWebDriver();
//            apd.hideKeyboard();
//        }
        ssnPopUpFrame.waitUntil(appear, waitTimeout());
        ssnAddressButton.waitUntil(appear, waitTimeout()).click();
        return this;
    }

    public CheckOutPage kodin1FillSsn(String ssn) {
        final SelenideElement kodin1SsnField = $x("//input[@id='ssn']").append("[1]");
        kodin1SsnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();
        return this;
    }

    public CheckOutPage norwayFillSsn(String ssn) {
        addressLinkCompany.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();
        return this;
    }

    public CheckOutPage getZipCodeValue() {
        zipCodeFieldLength = postcodeFieldCompany.getAttribute("maxLength");
        return this;
    }

    public CheckOutPage fillAndValidateAddressFormPrivate(String address, String postcode, String phone){

        System.out.println(" fillAndValidateAddressFormPrivate is called");
        goToKassan.waitUntil(appear, waitTimeout()).scrollIntoView(false);
        executeJavaScript("arguments[0].click()", goToKassan);
        if(country == SE) openAddressFormPrivate();
        firstName.waitUntil(appear, waitTimeout()).setValue("Karl").pressTab();
        lastName.waitUntil(appear, waitTimeout()).setValue("Mobelkung").pressTab();
        addressFieldPrivate.waitUntil(appear, waitTimeout()).setValue(address).pressTab();
        postcodeFieldPrivate.waitUntil(appear, waitTimeout()).setValue(postcode).pressTab().pressTab();
//        if(country == FI || country == DK) {
//            cityElements.first().setValue("Helsinhagen").pressTab();
//        }
        phoneFieldPrivate.waitUntil(appear, waitTimeout()).setValue(phone).pressTab();
        emailFieldPrivate.waitUntil(appear, waitTimeout()).setValue(randomEmail()).pressTab();

        stageCircle.shouldHave(text("1"));
        goToDelivery.waitUntil(appear,waitTimeout());
        executeJavaScript("arguments[0].click()", goToDelivery);

        return this;
    }

    public CheckOutPage fillAndValidateAddressFormCompany(String ssn, String phone){

        System.out.println("-*- " + "Address form company call" + " -*-");

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

    protected void fillAndValidateSsnNorway(String ssn, String phone){

        ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();
        nonSeSsnControlField.waitUntil(appear, waitTimeout());
        ssnAddressFormLink.waitUntil(appear, waitTimeout()).click();

        postcodeFieldCompany.waitUntil(appear, waitTimeout()).clear();
        postcodeFieldCompany.waitUntil(appear, waitTimeout()).setValue("0170").pressTab().pressTab();
        phoneElements.last().waitUntil(appear, waitTimeout()).setValue(phone).pressTab();
        emailElements.last().waitUntil(appear, waitTimeout()).setValue(randomEmail()).pressTab();
    }

    protected void fillAndValidateSsnSweden(String ssn, String phone){

        ssnField.waitUntil(appear, waitTimeout()).scrollIntoView(false).setValue(ssn).pressEnter();
        ssnPopUpFrame.waitUntil(appear, waitTimeout());
        ssnAddressButton.waitUntil(appear, waitTimeout()).click();
        ElementSelectors.SampleSelectors.phone.waitUntil(appear, waitTimeout()).setValue(phone).pressTab();
        ElementSelectors.SampleSelectors.email.waitUntil(appear, waitTimeout()).setValue(randomEmail()).pressTab();


    }

    protected void fillAndValidateSsnFinDen(String ssn, String phone){

        System.out.println("-*- " + "SsnFinDen Checkout" + " -*-");

        ElementsCollection ssnC = $$x("//input[@id='ssn']");

        if(ssnC.last().exists()) ssnC.last().setValue("02012566").pressTab();
        companyNameField.waitUntil(appear, waitTimeout()).setValue("Kompany").pressTab();
        addressFieldCompany.waitUntil(appear,waitTimeout()).setValue("Stad 1").pressTab();
        postcodeFieldCompany.waitUntil(appear,waitTimeout());

        if(country == FI) {

            postcodeFieldCompany.setValue("00300").pressTab();
//            cityElements.last().waitUntil(appear,waitTimeout()).setValue("Helsinki").pressTab();
            phoneElements.last().waitUntil(appear, waitTimeout()).setValue(phone).pressTab();
            emailElements.last().waitUntil(appear, waitTimeout()).setValue(randomEmail()).pressTab();
        }

        else {

            postcodeFieldCompany.setValue("1561").pressTab();
//            cityElements.last().waitUntil(appear,waitTimeout()).setValue("Copenhagen").pressTab();
            phoneElements.last().waitUntil(appear, waitTimeout()).setValue(phone).pressTab();
            emailElements.last().waitUntil(appear, waitTimeout()).setValue(randomEmail()).pressTab();
        }

    }

    public CheckOutPage validateDeliveryServices(){

        System.out.println(" validateDeliveryServices is called");

        if(country == SE || country == NO ){

            goToPayment.waitUntil(exist, waitTimeout());
            deliveryServicesForm.waitUntil(exist,waitTimeout());
            stageCircle.shouldHave(text("2"));
            deliveryServiceItem.should(be(visible));

            goToPayment.waitUntil(appear, waitTimeout());
            executeJavaScript("arguments[0].click()", goToPayment);
        }

        return this;
    }

    @Attachment
    private String listOfPaymentMethods(List<String> list) {
        return list.stream().collect(Collectors.joining("\n" + "-", "The list of paymethods is:" + "\n" + "-", ""));
    }

    private void makeScreenshot(String outputFolder) {
        DateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
        Date date = new Date();
        File srcFile = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(outputFolder + "-" + dateFormat.format(date) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeScreenshot2() {
        DateFormat dateFormat = new SimpleDateFormat("hh-mm-ss");
        Date date = new Date();

        BufferedImage image = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(WebDriverRunner.getWebDriver()).getImage();
        try {
            ImageIO.write(image, "png", new File(SCREENSHOT_FILEPATH + "-" + dateFormat.format(date) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
