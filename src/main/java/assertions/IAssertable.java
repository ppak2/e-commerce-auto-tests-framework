package assertions;

import _pages.Page;
import _pages.enums.ChannelType;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.testng.asserts.SoftAssert;

import java.util.function.Predicate;

public interface IAssertable<T> {

    void assertPage(SoftAssert softly);

    Predicate<SelenideElement> imageIsLoaded = img ->
            Selenide.executeJavaScript(
                    "return arguments[0].complete && " +
                            "typeof arguments[0].naturalWidth != \"undefined\" && " +
                            "arguments[0].naturalWidth > 0", img);

    Predicate<ElementsCollection> imagesAreLoaded = collection ->
            collection.stream().allMatch(imageIsLoaded);

    Predicate<ChannelType> isFB = channel -> channel == ChannelType.FURNITUREBOX;

    Predicate<ChannelType> isWG = channel -> channel == ChannelType.WEGOT;

    Predicate<ChannelType> isFBWG = isFB.or(isWG);
}
