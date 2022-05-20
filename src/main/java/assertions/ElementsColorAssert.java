package assertions;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.support.Color;
import pages.ChannelType;
import pages.Page;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.exist;
import static pages.ChannelType.*;
import static repository.ElementSelectors.*;
import static io.vavr.API.*;
import static repository.ElementSelectors.ProductPageSelectors.*;


public class ElementsColorAssert extends AbstractAssert<ElementsColorAssert, Page> {

    private ChannelType channelType;

    private ElementsColorAssert(Page page) {

        super(page, ElementsColorAssert.class);
        this.channelType = page.getChannelType();
    }

    @SuppressWarnings("unused")
    public static ElementsColorAssert assertThat(Page page) { return new ElementsColorAssert(page); }

    @SuppressWarnings("unused")
    public static ElementsColorAssert assertThatColors(Page page) { return new ElementsColorAssert(page); }

    public ElementsColorAssert ofBreadCrumbsAreConsistent(){

        isNotNull();

        if(!validateBreadCrumbs()){
            failWithMessage("BreadCrumbs colors on <%s> channel are wrong", channelType);
        }
        return this;
    }

    public ElementsColorAssert ofDiscountLabelsAreConsistent(){

        isNotNull();

        if(!validateDiscountLabels()){
            failWithMessage("Discount labels colors on <%s> channel are wrong", channelType);
        }

        return this;
    }

    public ElementsColorAssert ofCurrentPricesAreConsistent(){

        isNotNull();

        if(!validatePricesCurrent()){
            failWithMessage("Current prices colors on <%s> channel are wrong", channelType);
        }
        return this;
    }

    public ElementsColorAssert ofSavingsPricesAreConsistent(){

        isNotNull();

        if(!validatePricesSavings()){
            failWithMessage("Savings prices colors on <%s> channel are wrong", channelType);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ElementsColorAssert ofSameSeriePricesAreConsistent(){

        isNotNull();

        if(!validateSameSeriePrices()){
            failWithMessage("Same serie prices colors on <%s> channel are wrong", channelType);
        }
        return this;
    }

    //main validating method
    private boolean validateElementsAttributes(ElementsCollection elements, String attributeName, Supplier<String> attributeValue){

        String attributeV = attributeValue.get();

        return validateElementsAttributes(elements, attributeName, attributeV);
    }


    private boolean validateElementsAttributes(ElementsCollection elements, String attributeName, String attributeValue){

        identifyPDP.waitUntil(exist,25000);  //<--

        return elements.stream()
                .filter(SelenideElement::exists)
                .filter(isAccessible)
                .map(el -> el.getCssValue(attributeName))
                .map(col -> Color.fromString(col).asHex())
                .allMatch(attr -> attr.equals(attributeValue));
    }

    private boolean validateBreadCrumbs() {

        if (channelType == WEGOT){
            return validateWGBreadCrumbs(getBreadCrumbsColor, "#bdbdbd");
        }
        else {
            return validateElementsAttributes(breadCrumbs, "color", getBreadCrumbsColor);
        }
    }

    private boolean validateDiscountLabels() {

        boolean result;

        int i = getDiscountLabelsColor.get().lastIndexOf("#");
        String color = getDiscountLabelsColor.get().substring(i);
        String background = getDiscountLabelsColor.get().substring(0,i);

        System.out.println("|||" + channelType + "|||");
        if (channelType == FURNITUREBOX || channelType == WEGOT) {
            result = (validateElementsAttributes(discountLabelFB, "color", color) && validateElementsAttributes(discountLabelsFB, "background-color", background));

        } else {
            result = (validateElementsAttributes(discountLabels, "color", color) && validateElementsAttributes(discountLabels, "background-color", background));
        }

        return result;
    }

    private boolean validateSameSeriePrices() {

        boolean result;

        System.out.println("|||" + channelType + "|||");
        if (channelType == FURNITUREBOX || channelType == WEGOT) {

            int i = getDiscountLabelsColor.get().lastIndexOf("#");
            String color = getDiscountLabelsColor.get().substring(i);
            String background = getDiscountLabelsColor.get().substring(0,i);

            result = (validateElementsAttributes(sameSeriePrices, "color", color) && validateElementsAttributes(sameSeriePrices, "background-color", background));

        } else {
            result = validateElementsAttributes(sameSeriePrices, "color", getSameSerieColor);
        }

        return result;
    }

    private boolean validatePricesCurrent() {

        ElementsCollection elementsCollection = pricesCurrent;

        if (channelType == FURNITUREBOX || channelType == WEGOT) {
            elementsCollection = pricesCurrentFB;
        }
        return validateElementsAttributes(elementsCollection, "color", getCurrentAndSavingsColor);
    }

    private boolean validatePricesSavings() {

        return validateElementsAttributes(pricesSavings, "color", getCurrentAndSavingsColor);
    }

    private boolean validateWGBreadCrumbs(Supplier<String> color2, String color3) {

        boolean result;
        SelenideElement lastBC = breadCrumbs.last();
        List<SelenideElement> collection = breadCrumbs.stream().limit((long) breadCrumbs.size() - 1).collect(Collectors.toList());
        String expectedColor3 = Color.fromString(lastBC.getCssValue("color")).asHex();

        result = (expectedColor3.equals(color3) && collection
                .stream().map(el -> el.getCssValue("color"))
                .map(col -> Color.fromString(col).asHex())
                .allMatch(attr -> attr.equals(color2.get())));

        return result;
    }

    private Supplier<String> getBreadCrumbsColor = () -> Match(channelType).of(

            Case($(TRADEMAX), STRONG_CYAN),
            Case($(CHILLI), TOMATO_RED),
            Case($(KODIN1), MODERATE_BLUE),
            Case($(FURNITUREBOX), OLIVE_GREEN),
            Case($(WEGOT), DEEP_CYAN)
    );

    //concatenation background + number colors
    private Supplier<String> getDiscountLabelsColor = () -> Match(channelType).of(

            Case($(TRADEMAX), PINK+WHITE),
            Case($(CHILLI), TOMATO_RED+WHITE),
            Case($(KODIN1), YELLOW+BLACK),
            Case($(FURNITUREBOX), STRONG_RED+WHITE),
            Case($(WEGOT), SOFT_RED+WHITE)
    );

    private Supplier<String> getCurrentAndSavingsColor = () -> Match(channelType).of(

            Case($(TRADEMAX), PINK),
            Case($(CHILLI), TOMATO_RED),
            Case($(KODIN1), DARK_CYAN),
            Case($(FURNITUREBOX), STRONG_RED),
            Case($(WEGOT), SOFT_RED)
    );

    private Supplier<String> getSameSerieColor = () -> Match(channelType).of(

            Case($(TRADEMAX), PINK),
            Case($(CHILLI), TOMATO_RED),
            Case($(KODIN1), DARK_CYAN),
            Case($(FURNITUREBOX), STRONG_RED+WHITE),
            Case($(WEGOT), SOFT_RED+WHITE)
    );

    private Predicate<SelenideElement> isVisible = s->s.getCssValue("visibility").equals("hidden");

    private Predicate<SelenideElement> isClickable = s->s.getCssValue("opacity").equals("0");

    private Predicate<SelenideElement> isAccessible = isVisible.and(isClickable).negate();

}
