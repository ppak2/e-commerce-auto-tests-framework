package _pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.java.Log;

import static _pages.enums.PlatformType.iOs;
import static com.codeborne.selenide.Selenide.*;
import static repository.ElementSelectors.IndexPageSelectors.*;
import static repository.ElementSelectors.MiniCartSelectors.miniCartIcon;
import static repository.ElementSelectors.MiniCartSelectors.miniCartIconMobile;

@Log
public class IndexPage extends Page {

    private String indexPageUrl;
    private String itemId;
    //Constructor for url and platformName?

    public IndexPage(String url, String platformName){

        super(url, platformName);
//        refresh();
        indexPageUrl = getIndexPageUrl();
        clearBrowserCookies();
        open(url);
        waitForIndexPageToLoad();
        clearBrowserLocalStorage();
        addLogs("<Index page>: ");
        this.itemId = inputParameters.get(this.country).getProperty("item");

    }

    @Override
    protected boolean isAt() {

        return indexPageUrl.equals(getMetaUrl.get());
    }

    public ProductListPage searchItem(){

//        action.checkForIFrame();
        action.checkForCookieButton();

        if(!this.isMobile()) searchItemDesktop();
        else searchItemMobile();

        return new ProductListPage(this);
    }

    public ProductListPage searchItem(String item){

        action.checkForCookieButton();

        if(!this.isMobile()) searchItemDesktop(item);
        else searchItemMobile(item);
        return new ProductListPage(this);
    }

    private void searchItemDesktop(){
        action.click(searchField);
        action.sendKeysEnter(searchField, this.itemId);
    }

    private void searchItemMobile(){
        action.handleTranslatePopUp();
        action.click(searchFieldIconMobile);
        action.sendKeysEnter(searchFieldMobile, this.itemId);
    }

    private void searchItemDesktop(String item){
        action.click(searchField);
        action.sendKeysEnter(searchField, item);
    }

    private void searchItemMobile(String item){
        action.handleTranslatePopUp();
        action.click(searchFieldIconMobile);
        action.sendKeysEnter(searchFieldMobile, item);
    }

    private void waitForIndexPageToLoad(){
        if (this.isMobile()){
            log.info("Waiting for IndexPage Mobile to load");
            searchFieldIconMobile.shouldBe(Condition.visible);
            miniCartIconMobile.shouldBe(Condition.visible);
        }
        else {
            log.info("Waiting for IndexPage Desktop to load");
            searchField.shouldBe(Condition.visible);
            miniCartIcon.shouldBe(Condition.visible);
        }
    }

}
