package repository;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ElementSelectors {

    //COLORS
    public static final String WHITE = "#ffffff";
    //Trademax
    public static final String PINK = "#ff0060";
    public static final String STRONG_CYAN = "#02a9c4";
    //Kodin
    public static final String DARK_CYAN = "#48a6a5";
    public static final String MODERATE_BLUE = "#457ab3";
    public static final String YELLOW = "#f9ed38";
    public static final String BLACK = "#1d1d1b";
    //Chilli
    public static final String TOMATO_RED = "#cf461d";
    //Furniturebox
    public static final String STRONG_RED = "#cb0a0a";
    public static final String OLIVE_GREEN = "#91c52c";
    //Wegot
    public static final String SOFT_RED = "#df5145";
    public static final String DEEP_CYAN = "#1377a3";


    public static class IndexPageSelectors {

        public static final ElementsCollection popularProductsImgTCK = $$x("//div[@class='shortenedProductList--list']//img"), //TCK - Trademax, Chilli, Kodin + Mobile
                bestSellersProductsImgTCK = $$x("//div[@class='section--content']//img"),
                popularProductsImgFBWG = $$x("//div[@id='popularItems']//div[@class='productSection--content']//li//img"),  //FB WG
                bestSellersProductsImgFBWG = $$x("//div[@id='bestsellers']//div[@class='productSection--content']//article/a//img"),
                imageContainersOnPLP = $$x("//img[contains(@class, 'productItem')]"),
                productsOnPLP = $$x("//div[@class='productItem--top']//img");


        public static final SelenideElement searchField = $x("//div[@id='search']/descendant::input"),
                searchFieldIconMobile = $x("//div[@id='searchMobile']/descendant::button"),
                searchFieldMobile = $x("//div[@id='overlay']/descendant::input"),
                searchFieldSubmitIconMobile = $x("//div[@id='overlay']//button[@type='submit']"),
                cookieButton = $x("//div[@id='cookiesPolicy']/descendant::button"),
                iFrame = $x("//div[@id='SignupForm_0']"),
                iFrameCloseButton = $x("//div[@class='mc-closeModal']"),
                iFrameHotJar = $x("//*[@id='_hj-f5b2a1eb-9b07_feedback_minimized']/div[2]/span[1]"),
                iFrameHotJarCloseButton = iFrameHotJar.$x("//a[@id='_hj-f5b2a1eb-9b07_feedback_open_close_phone']/span"),
                categoriesTab = $x("//li[contains(text(), 'Kategor')]"),
                firstItemInCategoryTab = $x("//div[@class='tabs--contentItem tabs--contentItemIsActive']/descendant::a[1]"),
                firstProductInCategoryList = $x("//*[contains(@id,'productList')]/descendant::img[1]"),  ////<<<<<!!!!
                bannerFrame = $x("//div[@class='bannerContent']"),
                productListSection = $x("//div[@id='searchPageTabs']"),
                singleItemOnPLP = $x("//div[@id='searchPageTabs']//a[@data-ticket]"),
                bannerCloseButton = $x("//div[@class='bannerContent__closeButton']"),
                filterSection = $x("//div[@class='l-filterBlock']"),
                filterSectionMobile = $x("//button[@id='ShowFilterBlocks']"),
                scriptABTestTag = $x("//meta[@name='_token']/following::script[1]");
    }

    public static class ProductPageSelectors {
        public static final ElementsCollection breadCrumbs = $$x("//*[@class='breadcrumbs ']/a"), // --colors
                discountLabels = $$x("//*[starts-with(@class,'discount')]"), // --colors
                discountLabelsFB = $$x("//div[@class='productItem--discountBadge']"),
                discountLabelFB = $$x("//div[@class='productInfo--priceDiscount']"),
                pricesCurrent = $$x("//div[@class='productInfoPrice--current']"), // --colors
                pricesCurrentFB = $$x("//div[@class='productInfoPrice--currentValue']"),
                pricesSavings = $$x("//span[@class='productInfoPrice--savingsAmount']"), //--colors
                sameSeriePrices = $$x("//div[@class='productItem--priceCurrent']"), // --colors
                sameSeriesImagesFBWG = $$x("//section[@id='sameSeries']//img"),
                customerInterestWG = $$x("//section[@id='productInfoCustomersInterests']//img"),
                multiSection = $$x("//div[@id='multiSection']//img");


        public static final SelenideElement addToCartSection = $x("//div[@id='productBuySection']"),
                addToCart = $x("//div[@id='productBuySection']/descendant::button[1]"),
                addToCartMobile = $x("//div[@id='productBuySection']/descendant::button[4]"),
                goToCheckOut = $x("//div[@class='popup confirmPopup popup-isShown']//a"),
                siteNameSelector = $x("//head/meta[@property='og:site_name']"),
                variantsFrame = $x("//div[@id='variantPropertySelectors']"),
                firstItemAddToCart = $x("//*[@id='productListExtended']/descendant-or-self::article[1]//h3"),
                continueShopping = $x("//div[@class='popup confirmPopup popup-isShown']//span"),
                identifyPDP = $x("//div[@id='multiSection' or @class='productSection ']"),
                identifyPDPMobile = $x("//div[@id='productInfoTabs']"),
                sameSeriaDropDown = $x("//div[@id='productInfoTabs']/descendant::h2[4]"),
                mainImage = $x("//div[@id='productInfoImage']/descendant::img[1]"),
                productTitleTCK = $x("//div[@id='productTitle']"),
                productTitleFBWG = $x("//div[@id='productInfoTitle']"),
                productPriceTCK = $x("//div[@id='productPrice1']"),
                productPriceTCKMobile = $x("//div[@id='productPrice2']"),
                productPriceFBWG = $x("//div[@id='productPrice']"),
                trustFactor = $x("//div[@class='trustFactors']"),
                iFrameCloseButton = $x("//div[@id='ve-panel-iframe-close']");

    }

    public static class CheckOutPageSelectors {
        public static final ElementsCollection payMethods = $$x("//div[@class='paymentMethods']/child::div//input"),
                phoneElements = $$x("//input[@id='phone']"),
                emailElements = $$x("//input[@id='email']"),
                goToPayments = $$x("//a[@href='/checkout/payment']"),
                cityElements = $$x("//input[@id='city']"),
                deliveryList = $$x("//div[@class=' deliveryServices--list']/div");  //size should not be 0

        public static final SelenideElement companyTab = $x("//button[@tabindex='0'][2]"), //<---
                ssnField = $x("//input[@name='ssn']"), //<---
                emailFieldPrivate = $x("//input[@name='email']"), //<---
                emailFieldCompany = $x("//form[@id='sveaManuallyAddressFormCompany']//input[@id='email']"),
                addressLinkPrivate = $x("//button[@class='formSwitcher--switchBtn']"), //<---
                addressLinkCompany = $x("//div[@id='checkout']//form[@id='sveaSsnFormCompany']/a"),
                addressFieldPrivate = $x("//input[@name='address']"), //<---
                postcodeFieldPrivate = $x("//input[@name='postcode']"), //<---
                phoneFieldPrivate = $x("//input[@name='phone']"), //<---
                addressFieldCompany = $x("//input[@name='address']"),  //<---
                postcodeFieldCompany = $x("//form[@id='sveaManuallyAddressFormCompany']//input[@id='postcode']"),
                ssnPopUpFrame = $x("//div[contains(text(), 'Välj din leveransadress')]"),
                ssnPopUpCloseBtn = $x("//div[@class='noRenderPopupContent--close']"), //<---
                ssnAddressButton = $x("//ul[@class='addressList--list']//li[1]"), //<---
                companyNameField = $x("//input[@name='fullName']"), //<---
                ssnAddressButton2 = $x("//div[@class='sveaSsnAddresses--list']/div[2]//button"),
                ssnSubmitButton = $x("//button[@id='btnGetAddress-companyTabSSN']"),
                paymentAgreement = $x("//div[@class='payment--agreement']"), // bottom page element for scrolling
                goToKassan = $x("//button[@id='bottomBtnGoToCheckout']"),
                stageCircle = $x("//span[@class='wizardState--step wizardState--step-active']/span"), //get innerText
                deliveryServicesForm = $x("//h3[@class='deliveryServices--title deliveryServices--title-homeDelivery']"), //<---
                deliveryServiceItem = $x("//h3[@class='deliveryPrice--title']"), //<---
                goToDelivery = $x("//button[@class='addressForm--btnSubmitAddress']"), //<---
                nonSeSsnControlField = $x("//div[@class='sveaTab--ssnAddressTitle']"),
                ssnAddressFormLink = $x("//button[@class='formSwitcher--switchBtn']"), //<---
                goToPayment = goToPayments.first(),
                radioButton_SVEA_CARD = $x("//input[@id='SVEA_CARD']/following::label[1]"),
                goToSubmitOrder = $x("//a[@id='checkoutBtnSubmitOrder']"),
                cardNumberField = $x("//input[@id='CardNumber']"),
                expMonthField = $x("//input[@id='ExpirationMonth']"),
                expYearField = $x("//input[@id='ExpirationYear']"),
                cvvField = $x("//input[@id='CVV']"),
                submitCardInfo = $x("//button[@id='submit-button']"),
                errorPopUpNotification = $x("//div[@id='notification']/div[@class='notifications']"),
                thankYouSection = $x("//*[contains(@class,'thankYouOrderInfo')]");
    }

    public static class MiniCartSelectors {

        public static final SelenideElement miniCartIcon = $x("//div[@id='cartStatus']"),
                miniCartIconMobile = $x("//div[@id='cartStatusMobile']"),
                miniCartEmpty = $x("//div[@class='minicart--empty']"),
                itemDescription = $x("//div[@class='cartLineDescription--info']/a"),
                pricePerItem = $x("//div[@class='cartLineControls--price']"),
                imageSelector = $x("//div[@class='cartLine']//img"),
                itemsCount = $x("//*[@class='cartStatus--itemsCountTotal']"),
                productContainerDisabled = $x("//div[@class='cartLineControls--quantity cartLineControls--quantity-disabled']"),
                minusButton = $x("//div[@class='cartLine']//button[1]"),
                plusButton = $x("//div[@class='cartLine']//button[2]"),
                inputField = $x("//div[@class='cartLine']//input"),
                removeButton = $x("//div[@class='cartLineControls--remove']"),
                undoButton = $x("//div[@class='cartLineControls']//descendant::a[1]"),  //after remove is pressed
                goToProduct = $x("//div[@class='cartLineControls']//descendant::a[2]"),
                miniCartTotal = $x("//div[@class='minicart--totalPrice']");
    }

    public static class MonitorButtonSelectors {

        public static final SelenideElement monitorButtonOnPLP = $x("//div[contains(@class,'productItem--monitorBtn')]"),
                emailField = $x("//input[@id='email']"),
                checkBox = $x("//label[@for='monitorAvailabilityPrivacyPolicy']"),
                toolTip = $x("//div[@class='tooltip-container tooltip-container-is-active']/div"),
                reset = $x("//div[@class='monitorAvailability--afterSubscription']/span");

    }

    public static class SampleSelectors {

        public static final SelenideElement samplesLink = $x("//div[@id='productInfoSamples']/button"),
                samplePopUp = $x("//div[@class='productInfoSamples--content']"),
                orderSampleButton = $x("//div[@class='productInfoSamples--content']//button"), //((disabled @attribute) - (no @attribute))
                sampleItemImage = $x("//div[@class='productInfoSamplesListItem']//img"),
                emptyItemCheckbox = $x("//div[@class='productInfoSamplesListItem']//span"),
                checkBoxIsChecked = $x("//*[@class='iconChecked']"),
                samplePreview = $x("//div[@class='productInfoSamplesItemPreview']"),
                arrowPrev = $x("//div[@class='slick-arrow slick-prev']"),
                arrowNext = $x("//div[@class='slick-arrow slick-next']"),
                imageChanged = $x("//div[@class='slick-slide slick-active slick-current productInfoSamplesItemPreview--picWrap' and @data-index='1']"),
                goBackButton = $x("//div[@class='productInfoSamples--buttons']/button[1]"),
                submitButton = $x("//div[@class='productInfoSamples--buttons']/button[2]"),
                submitButtonIsChecked = $x("//div[@class='productInfoSamples--buttons']/button[2]/child::node()[1]"),
                orderForm = $x("//form[@id='productInfoSamplesOrderForm']"),
                firstName = $x("//input[@name='firstName']"), //<---
                lastName = $x("//input[@name='lastName']"), //<---
                address = $x("//input[@id='address']"),
                postcode = $x("//input[@id='postcode']"),
                city = $x("//input[@id='city']"),
                phone = $x("//input[@id='phone']"),
                email = $x("//input[@id='email']"),
                checkbox = $x("//label[@class='productInfoSamples--checkbox']/span"),
                submitOrderButton = $x("//div[@class='productInfoSamplesOrderForm--buttonWrapper']/button"),
                confirmPopUp = $x("//div[@class='productInfoSamplesOrderMessage']"),
                confirmPopUpButton = $x("//button[@class='btn btn--submitClose']");


    }

    public static class ThankYouPageSelectors {

        public static final ElementsCollection whatHappensNowList = $$x("//ul[@class='processStepsInfoBlock--list']/li"),
                expandDeliveryServices = $$x("//button[@class='showDeliveryServices--btn']"),  // <-- select first
                changeDeliveryOptionsButtons = $$x("//button[@id='ChangeDeliveryOptionsButton']"),
                timeTableDHL = $$x("//ul[@class='timeTableOptions']/li"), // <-- must be > 1
        //Delivery block
        deliveryServicesList = $$x("//ul[@class='deliveryServices']/li");  //  <-- must be >1

        public static final SelenideElement deliveryBlock = $x("//div[@class='deliveryBlock']"),

        //Delivery services
        firstPaidService = $x("//ul[@class='deliveryServices']/li[2]//div[@class='radio']"), //<-- click service
                secondPaidService = $x("//ul[@class='deliveryServices']/li[3]//div[@class='radio']"),
                timeDHLService = $x("//ul[@class='deliveryServices']/li[1]//div[@class='radio']"), // verify selection
                expandTimeTableDHL = $x("//ul[@class='deliveryServices']/li[1]//button"), //
                servicePriceFirst = $x("//ul[@class='deliveryServices']/li[2]//div[@class='serviceOption--price']"), // getText()goBackButton = $x("//div[@class='popupModern--footerButtons']/button[1]"),
                servicePriceSecond = $x("//ul[@class='deliveryServices']/li[3]//div[@class='serviceOption--price']"),
                selectButton = $x("//div[@class='popupModern--footerButtons']/button[2]"), // click()

        //DHL timetable
        popUpBlock = $x("//div[@class='popupModern--layout ']"),


        //CartServices
        cartBlock = $x("//div[@class='cartServices']"), // <-- appeared
                expandCartBlock = $x("//button[@id='ViewServicesDetails']"), // <-- click()
                cartBlockTotalPrice = $x("//div[@class='cartServices--totalPrice']"), // getText()
                cartBlockItem = $x("//div[@class='cartServicesItem']"), // isPresent()
                cartBlockItemPrice = $x("//div[@class='cartServicesItem']/descendant::div[4]"), // getText()
                forPaymentButton = $x("//button[@id='CartServicesConfirmBtn']"), // click()

        //Result block for second item
        resultBlock = $x("//ul[@class='updatedServices--list']"), // isPresent()
                resultBlockSkipButton = $x("//button[@id='UnchangedServicesSkipBtn']"),  // click()
                resultBlockContinueButton = $x("//button[@id='SubmitChangedServicesBtn']"),
                resultBlockTotalPrice = $x("//div[@class='updatedServices--totalPrice']"),  // getText()
                resultBlockOnDHL = $x("//span[@class='deliverySelectedService--price']"),  // getText()
                resultBlockSubmitButton = $x("//button[@id='SubmitChangedServicesBtn']"); // click()
    }
}

