package experiment;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.Test;

public class GridTest extends BaseTest  {

    @Test(parameters = "url")
    public void simpleTest(String url){
        Selenide.open(url);
        Selenide.sleep(5000);
    }
}
