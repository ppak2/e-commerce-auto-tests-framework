package assertions.assertpage;

import _pages.Page;
import _pages.enums.ChannelType;
import assertions.IAssertable;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.java.Log;
import org.assertj.core.api.AbstractAssert;
import org.testng.asserts.SoftAssert;

import java.util.function.Supplier;

import static repository.ElementSelectors.IndexPageSelectors.*;

@Log
public class ProductListPageAssert extends AbstractAssert<ProductListPageAssert, Page> implements IAssertable {

//    private ChannelType channelType;
    private boolean pageIsMobile;

    public ProductListPageAssert (Page page){
        super(page, ProductListPageAssert.class);
//        this.channelType = page.getChannelType();
        this.pageIsMobile = page.isMobile();
    }

    @Override
    public void assertPage(SoftAssert softly) {
//        softly.assertTrue(filterSectionIsPresent(), "Filter section is present");
        softly.assertTrue(productsAreNotEmpty(), "Products on PLP not empty");
        softly.assertTrue(productsImagesAreLoaded(), "Products on PLP loaded");
    }

    private boolean productsAreNotEmpty(){
        log.info("Products list size is "+imageContainersOnPLP.size());
        return imageContainersOnPLP.size() > 1;  // > 1
    }

    private boolean productsImagesAreLoaded(){
        return imagesAreLoaded.test(imageContainersOnPLP);
    }

//    private boolean filterSectionIsPresent(){
//        return getFilterSection.get().is(Condition.visible);
//    }

    private Supplier<SelenideElement> getFilterSection = () -> this.pageIsMobile ? filterSectionMobile : filterSection;
}
