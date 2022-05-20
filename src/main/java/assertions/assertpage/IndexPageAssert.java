package assertions.assertpage;

import _pages.Page;
import _pages.enums.ChannelType;
import assertions.IAssertable;
import com.codeborne.selenide.ElementsCollection;
import lombok.NonNull;
import org.assertj.core.api.AbstractAssert;
import org.testng.asserts.SoftAssert;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static _pages.enums.ChannelType.FURNITUREBOX;
import static _pages.enums.ChannelType.WEGOT;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static repository.ElementSelectors.IndexPageSelectors.*;


public class IndexPageAssert extends AbstractAssert<IndexPageAssert, Page> implements IAssertable {

    private ChannelType channelType;
    private boolean pageIsMobile;

    public IndexPageAssert(Page page) {
        super(page, IndexPageAssert.class);
        this.channelType = page.getChannelType();
        this.pageIsMobile = page.isMobile();

    }

    @Override
    public void assertPage(SoftAssert softly) {
        softly.assertTrue(popularProductsAreNotEmpty(), "Popular products on Index not empty");
        softly.assertTrue(bestSellersProductsAreNotEmpty(), "Best sellers products on Index not empty");
        softly.assertTrue(bestSellersProductsImagesAreLoaded(), "Best sellers products images on Index loaded");
        softly.assertTrue(popularProductsImagesAreLoaded(), "Popular products Images on Index loaded");
    }

    private boolean popularProductsAreNotEmpty() {
        if (pageIsMobile) {
            return true;
        } else {
            return getPopularProducts.get().size() > 1;
        }
    }

    private boolean bestSellersProductsAreNotEmpty() {
        if (pageIsMobile && !isFBWG.test(channelType)) {
            return true;
        } else {
            return getBestSellersProducts.get().size() > 1;
        }
    }

    private boolean popularProductsImagesAreLoaded() {
        if (pageIsMobile && !isFBWG.test(channelType)) {
            return true;
        } else {
        return imagesAreLoaded.test(getPopularProducts.get());
        }
    }

    private boolean bestSellersProductsImagesAreLoaded() {
        if (pageIsMobile && !isFBWG.test(channelType)) {
            return true;
        } else {
        return imagesAreLoaded.test(getBestSellersProducts.get());
        }
    }

    private Supplier<@NonNull ElementsCollection> getPopularProducts = () -> Match(channelType).of(
            Case($(isFBWG), popularProductsImgFBWG),
            Case($(), popularProductsImgTCK)
    );

    private Supplier<@NonNull ElementsCollection> getBestSellersProducts = () -> Match(channelType).of(
            Case($(isFBWG), bestSellersProductsImgFBWG),
            Case($(), bestSellersProductsImgTCK)
    );

//    private static Predicate<Enum> isFBWG = channelType -> channelType.equals(FURNITUREBOX) || channelType.equals(WEGOT);
}
