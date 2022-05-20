package browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class Safari implements Browser {

    @Override
    public Name name() {
        return Name.Safari;
    }

    @Override
    public Capabilities capabilities(Map<String, String> parameters) {

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("safari");
        capabilities.setPlatform(Platform.MAC);
        capabilities.setVersion("11.1");
        capabilities.setCapability("resolution", "1920x1080");
        capabilities.setCapability("browserstack.safari.enablePopups", "true");
        capabilities.setCapability("browserstack.safari.driver", "2.48");
        capabilities.setCapability("name", parameters.get("name"));
        capabilities.setCapability("build", parameters.get("build"));

        capabilities.setCapability("browserstack.console", "verbose");
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.networkLogs", "true");

        return capabilities;
    }
}
