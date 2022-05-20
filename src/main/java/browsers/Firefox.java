package browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;


public class Firefox implements Browser {
    @Override
    public Name name() { return Name.FireFox; }

    @Override
    public Capabilities capabilities(Map<String, String> parameters) {

        final FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.merge(options);
//        capabilities.setBrowserName("firefox");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);

        capabilities.setCapability("resolution", "1440x900");
        capabilities.setCapability("name", parameters.get("name"));
        capabilities.setCapability("build", parameters.get("build"));
        return capabilities;
    }
}
