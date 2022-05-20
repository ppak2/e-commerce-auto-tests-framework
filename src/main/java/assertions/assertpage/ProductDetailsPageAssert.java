package assertions.assertpage;

import _pages.Page;
import _pages.enums.ChannelType;
import assertions.IAssertable;
import com.codeborne.selenide.Condition;
import org.assertj.core.api.AbstractAssert;
import org.testng.asserts.SoftAssert;

import java.util.stream.Stream;

import static repository.ElementSelectors.ProductPageSelectors.*;

public class ProductDetailsPageAssert extends AbstractAssert<ProductDetailsPageAssert, Page> implements IAssertable {

    private ChannelType channelType;
    private boolean pageIsMobile;

    public ProductDetailsPageAssert(Page page){
        super(page, ProductDetailsPageAssert.class);
        this.channelType = page.getChannelType();
        this.pageIsMobile = page.isMobile();
    }

    @Override
    public void assertPage(SoftAssert softly) {
        softly.assertTrue(mainImageIsLoaded(), "Main image on "+channelType+" PDP is NOT loaded");
        softly.assertTrue(productTitleIsPresent(), "Product title on "+channelType+" PDP is NOT displayed");
        softly.assertTrue(productPriceIsLoaded(), "Product price on "+channelType+" PDP is NOT displayed");
        softly.assertTrue(trustFactorIsPresent(), "Trust factor on "+channelType+" PDP is NOT displayed");
        softly.assertTrue(multiSectionImagesAreLoaded(), "Multi section images on "+channelType+" PDP are NOT loaded");
    }

    private boolean mainImageIsLoaded(){
        return imageIsLoaded.test(mainImage);
    }

    private boolean productTitleIsPresent(){
        return isFBWG.test(this.channelType) ? productTitleFBWG.is(Condition.visible) : productTitleTCK.is(Condition.visible);
    }

    private boolean productPriceIsLoaded(){
        if (!isFBWG.test(channelType) && pageIsMobile){
            return productPriceTCKMobile.isDisplayed();
        }
        else if (!isFBWG.test(channelType)){
            return productPriceTCK.isDisplayed();
        }
        else {
            return productPriceFBWG.isDisplayed();
        }
    }

    private boolean trustFactorIsPresent(){
        return trustFactor.exists();

    }

    private boolean multiSectionImagesAreLoaded(){
        if (isWG.test(channelType)){
            return Stream.concat(customerInterestWG.stream(), sameSeriesImagesFBWG.stream()).allMatch(imageIsLoaded);
        }
        else {
            return imagesAreLoaded.test(multiSection);
        }
    }
}
