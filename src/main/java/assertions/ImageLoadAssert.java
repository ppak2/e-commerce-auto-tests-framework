package assertions;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.AbstractAssert;
import pages.ChannelType;
import pages.Page;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static pages.ChannelType.FURNITUREBOX;
import static pages.ChannelType.WEGOT;
import static repository.ElementSelectors.IndexPageSelectors.*;


public class ImageLoadAssert extends AbstractAssert<ImageLoadAssert, Page> {

    private ChannelType channelType;

    private ImageLoadAssert(Page page){

        super(page, ImageLoadAssert.class);
        this.channelType = page.getChannelType();
    }

    @SuppressWarnings("unused")
    public static ImageLoadAssert assertThat(Page page){
        return new ImageLoadAssert(page);
    }

    @SuppressWarnings("unused")
    public static ImageLoadAssert assertThatImages(Page page) { return new ImageLoadAssert(page); }

    public ImageLoadAssert popularProductsAreNotEmpty(){

        isNotNull();

        if(getPopularProducts.get().size() < 1){
            failWithMessage("Popular products actual size is <%s>, must not be empty", getPopularProducts.get().size());
        }

        return this;
    }

    public ImageLoadAssert bestSellersProductsAreNotEmpty(){

        isNotNull();

        if(getBestSellersProducts.get().size() < 1){
            failWithMessage("Best sellers products actual size is <%s>, must not be empty", getBestSellersProducts.get());
        }

        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoadAssert popularProductsImagesAreLoaded(){

        isNotNull();

        if(!validateImages(getPopularProducts.get())){
            failWithMessage("Popular products images are not loaded properly");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoadAssert bestSellersProductsImagesAreLoaded(){

        isNotNull();

        if(!validateImages(getBestSellersProducts.get())){
            failWithMessage("Best sellers products images are not loaded properly");
        }
        return this;
    }

    public ImageLoadAssert productsOnPLPareNotEmpty(){

        isNotNull();

        if(productsOnPLP.size() < 1){
            failWithMessage("Best sellers products actual size is <%s>, must not be empty", productsOnPLP.size());
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ImageLoadAssert productsImagesOnPLPAreLoaded(){

        isNotNull();

        boolean result = validateImages(productsOnPLP);
        if(!result){
            failWithMessage("Product list items images are not loaded properly");
        }
        return this;
    }


    private boolean validateImages(ElementsCollection images) {

        System.out.println(images.size());
        return images.stream().allMatch(image -> imageIsLoaded.test(image));
    }

    private Predicate<SelenideElement> imageIsLoaded = img ->

            Selenide.executeJavaScript(
                    "return arguments[0].complete && " +
                            "typeof arguments[0].naturalWidth != \"undefined\" && " +
                            "arguments[0].naturalWidth > 0", img);

    private Supplier<ElementsCollection> getPopularProducts = () -> Match(channelType).of(

            Case($(isFBWG), popularProductsImgFBWG),
            Case($(), popularProductsImgTCK)
    );

    private Supplier<ElementsCollection> getBestSellersProducts = () -> Match(channelType).of(

            Case($(isFBWG), bestSellersProductsImgFBWG),
            Case($(), bestSellersProductsImgFBWG)
    );

    private static Predicate<Enum> isFBWG = channel -> channel.equals(FURNITUREBOX)||channel.equals(WEGOT);
}
