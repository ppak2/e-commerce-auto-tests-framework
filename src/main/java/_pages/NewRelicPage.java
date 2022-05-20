package _pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.actions;
import static repository.ElementSelectors.IndexPageSelectors.searchField;

public class NewRelicPage extends Page {

    private static final SelenideElement levelCat1 = $("a[href='/uteplats");
    private static final SelenideElement levelCat2 = $("a[href='/mobler/utemobler']");


    public NewRelicPage(String url, String platform){
        super(url,platform);
        open(url);
    }

    public void verifyCategoryLink(){

        action.checkForCookieButton();

        actions().moveToElement(levelCat1).perform();
        Selenide.sleep(2000);

        actions().moveToElement(levelCat2).click().perform();
        Selenide.sleep(2000);

        currentDriver.navigate().to("http://httpbin.org/user-agent");
        String body = $("pre").getText();
        System.out.println("=========================");
        System.out.println(body);
        System.out.println("=========================");

    }

    @Override
    protected boolean isAt() {
        return false;
    }
}
