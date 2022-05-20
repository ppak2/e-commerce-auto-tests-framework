package _pages;


import static com.codeborne.selenide.Selenide.sleep;
import static repository.ElementSelectors.IndexPageSelectors.*;


public class ProductListPage extends Page {

    //firstItemInCategoryTab <--

    private String productListPageUrl;
    private String itemID;

    ProductListPage(Page page){

        super(page);
        productListPageUrl = page.getIndexPageUrl()+"/search";
        action.checkForCookieButton();
        action.checkForIFrame();
        addLogs("<ProductList page>: ");

    }

    @Override
    protected boolean isAt() {

        return productListPageUrl.equals(getMetaUrl.get());
    }

    public ProductListPage proceedToFilterProductList(){

        this.waitForPageToLoad();
        action.click(categoriesTab);
        action.click(firstItemInCategoryTab);

        return this;
    }

    public ProductDetailsPage selectSingleItem(){

//        this.waitForPageToLoad();
        action.click(singleItemOnPLP);
        return new ProductDetailsPage(this);
    }

    public ProductDetailsPage proceedToProductDetailsPage(){

        this.proceedToFilterProductList();
        action.click(firstProductInCategoryList);
        sleep(4000); //<----- remove

        return new ProductDetailsPage(this);
    }


}
