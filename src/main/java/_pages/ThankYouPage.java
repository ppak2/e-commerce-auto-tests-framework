package _pages;

import _pages.Actions.MobileActions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.sun.istack.NotNull;
import lombok.Getter;

import java.util.StringJoiner;

import static _pages.enums.ChannelType.Country.*;
import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.CheckOutPageSelectors.*;
import static repository.ElementSelectors.CheckOutPageSelectors.phoneFieldPrivate;
import static repository.ElementSelectors.SampleSelectors.firstName;
import static repository.ElementSelectors.SampleSelectors.lastName;
import static repository.ElementSelectors.ThankYouPageSelectors.*;

public class ThankYouPage extends CheckOutPage {

    private String paidDeliveryOptionsPrice;

    @Getter
    private String cartBlockTotalSum;

    @Getter
    private String cartBlockItemValue;

    @Getter
    private String resultBlockTotalSum;

    @Getter
    private double paidItemPrice;

    @Getter
    private double dhlItemPrice;

    @Getter
    private double cartTotalSum1;

    @Getter
    private double cartTotalSum2;

    @Getter
    private double cartTotalSum3;

    @Getter
    private String freeValue1;

    @Getter
    private String freeValue2;

    public ThankYouPage(Page page) {
        super(page);
    }

    @Override
    protected boolean isAt() {
        return false;
    }

    //DRAFT
    public void completePaidDelivery(String zipCode) {

        completePrivate(zipCode);
        completeOrder();
        goToPaidDeliveryOptions();
        selectPaidOption(firstPaidService);
        validateCartBlock();
        proceedToResultBlock();
        validateResultBlock(resultBlockTotalPrice);
    }

    //DRAFT
    public void completeDHL(String zipCode) {
        completePrivate(zipCode);
        completeOrder();
        goToDHLDeliveryOptions();
        selectDHLTimeOption();
        validateCartBlock();
        proceedToResultBlock();
        validateResultBlock(resultBlockOnDHL);
    }

    //DRAFT
    public void completeCombinationServices(String zipCode) {

        //1
        completePrivate(zipCode);
        completeOrder();
        goToPaidDeliveryOptions();
        paidItemPrice = getPriceValue(servicePriceSecond);
        selectPaidOption(secondPaidService);
        System.out.println(paidItemPrice);
        openCartBlock();
        cartTotalSum1 = getPriceValue(cartBlockTotalPrice);
        System.out.println(cartTotalSum1);
        System.out.println(" ****** 1 ****** ");
        System.out.println(paidItemPrice == cartTotalSum1);
        System.out.println(" ****** /1 ****** ");
        //2
        goToDHLDeliveryOptions();
        freeValue1 = servicePriceSecond.getText();
        System.out.println(" ****** 2 ****** ");
        System.out.println(freeValue1.equals("Free!"));
        dhlItemPrice = getPriceValue(servicePriceFirst);
        selectPaidOption(firstPaidService);
        System.out.println(dhlItemPrice);
        openCartBlock();
        cartTotalSum2 = getPriceValue(cartBlockTotalPrice);
        System.out.println(cartTotalSum2);
        System.out.println(cartTotalSum2 == paidItemPrice + dhlItemPrice);
        System.out.println(" ****** /2 ****** ");
        //3
        action.click(expandCartBlock);
        action.click(changeDeliveryOptionsButtons.first());
        freeValue2 = servicePriceFirst.getText();
        System.out.println(" ****** 3 ****** ");
        System.out.println(freeValue2.equals("Free!"));
        selectPaidOption(firstPaidService);
        openCartBlock();
        cartTotalSum3 = getPriceValue(cartBlockTotalPrice);
        System.out.println(cartTotalSum3);
        System.out.println(cartTotalSum3 == dhlItemPrice);
        System.out.println(" ****** /3 ****** ");
        proceedToResultBlock();
        validateResultBlock(resultBlockTotalPrice);
    }

    private void completePrivate(String zipCode) {
        action.click(goToKassan);
        if (country == SE) openAddressFormPrivate();
        if (country == FI || country == NO) {
            action.hoverAndClick(ssnField);
            action.sendKeys(ssnField, "15055514");
        }
        action.hoverAndClick(firstName); //put cursor into the field
        action.sendKeysTab(firstName, "Karl");
        action.sendKeysTab(lastName, "Mobelkung");
        action.sendKeysTab(addressFieldPrivate, getAddress());
        action.sendKeysTab(postcodeFieldPrivate, zipCode);
        sleep(3000);
        action.hoverAndClick(phoneFieldPrivate); //put cursor into the field
        action.sendKeysTab(phoneFieldPrivate, getPhone());
        if (platformType == iOs) MobileActions.iOsSingleScroll();
        if (platformType == iOs) {
            action.sendKeys(emailFieldPrivate, randomEmail());
        } else {
            action.sendKeysTab(emailFieldPrivate, randomEmail());
        }
        if (platformType == iOs) MobileActions.iOsSingleScroll();
        action.hoverAndClick(phoneFieldPrivate);
        phoneFieldPrivate.pressEnter();
        /////////////////////////////////////
        sleep(10000);
        /////////////////////////////////////
        if (isAtStage.test(getAddressStageUrl())) action.click(goToDelivery);
    }

    private void completeOrder() {
        action.click(radioButton_SVEA_CARD);
        action.click(goToSubmitOrder);
        action.sendKeysTab(cardNumberField, "4916-4232-3977-8102");
        action.sendKeysTab(expMonthField, "10");
        action.sendKeysTab(expYearField, "22");
        action.sendKeysTab(cvvField, "987");
        action.click(submitCardInfo);
//        sleep(10000);
//        refresh();
        deliveryBlock.waitUntil(Condition.appear, 45000).scrollIntoView(false);
        System.out.println("Start sleeping");
        sleep(3000);
        System.out.println("Finished sleeping");
    }

    //Select first option from the list
    private void goToPaidDeliveryOptions() {
        action.click(expandDeliveryServices.first());
        action.click(changeDeliveryOptionsButtons.first());
        sleep(3000);
    }

    //Select second option from the list
    private void goToDHLDeliveryOptions() {
        action.click(expandDeliveryServices.get(1));
        action.click(changeDeliveryOptionsButtons.get(1));
//        sleep(3000);
    }

    private void selectPaidOption(SelenideElement option) {
        action.click(option);
        action.click(selectButton);
        sleep(3000);
    }

    private void selectDHLTimeOption() {
        action.click(timeDHLService);
        action.click(expandTimeTableDHL);
//        sleep(3000);
        action.click(timeTableDHL.get(5));
        action.click(selectButton);
    }

    private void validateCartBlock() {
        cartBlock.scrollIntoView(false);
        action.click(expandCartBlock);
        cartBlockTotalSum = cartBlockTotalPrice.getText();
        cartBlockItem.shouldBe(Condition.visible);
        cartBlockItemValue = cartBlockItemPrice.getText();
        System.out.println(cartBlockTotalSum + "   " + cartBlockItemValue);
        sleep(3000);
    }

    private void openCartBlock() {
        if (cartBlock.exists()) {
            cartBlock.scrollIntoView(false);
            action.click(expandCartBlock);
        }
    }

    private void proceedToResultBlock() {
        action.click(forPaymentButton);
    }

    private void validateResultBlock(SelenideElement resultValue) {
        resultBlock.shouldBe(Condition.visible);
        if (resultBlockSkipButton.isDisplayed()) action.click(resultBlockSkipButton);
        if(resultBlockContinueButton.isDisplayed()) action.click(resultBlockContinueButton);
        resultBlockTotalSum = resultValue.getText();
        action.click(resultBlockSubmitButton);
        sleep(5000);
    }

    public double getPriceValue(SelenideElement priceBlock) {

        String[] array = priceBlock.getText().split("\\s");
        StringJoiner sj = new StringJoiner("");
        for (String s : array) {
            if (s.matches("\\d+") || s.matches("\\d+,\\d+")) sj.add(s);
            else break;
        }
        double result = Double.parseDouble(sj.toString().replace(',', '.'));
        result*=100;
        result = Math.round(result);
        result/=100;

        return result;
    }

}
