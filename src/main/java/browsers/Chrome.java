package browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class Chrome implements Browser {

    @Override
    public Name name() {
        return Name.Chrome;
    }

    @Override
    public Capabilities capabilities(Map<String, String> parameters) {
        final ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-urlfetcher-cert-requests");
        options.addArguments("--disable-popup-blocking");
//        options.setHeadless(true);

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setPlatform(Platform.fromString(parameters.get("platform")));
//        capabilities.setVersion("75");
        capabilities.setCapability("resolution", "1920x1080");
        capabilities.setCapability("name", parameters.get("name"));
        capabilities.setCapability("build", parameters.get("build"));

        capabilities.setCapability("browserstack.use_w3c", "true");
        capabilities.setCapability("browserstack.console", "verbose");
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.networkLogs", "true");

        return capabilities;
    }

}

