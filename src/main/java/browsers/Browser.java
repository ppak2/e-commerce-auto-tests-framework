package browsers;

import org.openqa.selenium.Capabilities;

import java.util.Map;

public interface Browser {

    enum Name{
        Chrome("chrome"),
        Chromium("chromium"),
        Safari("safari"),
        SafariMobile("safari_mobile"),
        InternetExplorer("internet explorer"),
        FireFox("ff"),
        Edge("edge");

        private String browser;

        Name(String browserName) {
            browser = browserName;
        }

        public String getBrowser(){
            return browser;
        }
    }

    Name name();

    Capabilities capabilities(final Map<String, String> parameters);

    default boolean isMobile(){
        return false;
    }
}
