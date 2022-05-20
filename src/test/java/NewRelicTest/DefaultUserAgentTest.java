package NewRelicTest;

import _pages.NewRelicPage;
import base.BaseTest;
import org.testng.annotations.*;


public class DefaultUserAgentTest extends BaseTest {


    @Parameters({"url", "platform"})
    @Test
    public void defaultUserAgent(String url, String platform){

        new NewRelicPage(url, platform).verifyCategoryLink();

    }
}
