package NewRelicTest;

import _pages.NewRelicPage;
import base.BaseTest;
import org.testng.annotations.*;


public class GoogleBotUserAgentTest extends BaseTest {


    @Parameters({"url", "platform"})
    @Test
    public void googleBotUserAgent(String url, String platform){

        new NewRelicPage(url, platform).verifyCategoryLink();

    }
}
