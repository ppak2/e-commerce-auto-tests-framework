package browsers;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class SafariMobile implements Browser {

    @Override
    public Name name() {
        return Name.SafariMobile;
    }

    @Override
    public boolean isMobile() {
        return true;
    }

    @Override
    public Capabilities capabilities(Map<String, String> parameters) {

        final DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
        capabilities.setCapability("device", parameters.get("device"));
        capabilities.setCapability("real_mobile", "true");
        capabilities.setCapability("sendKeyStrategy", "grouped");
        capabilities.setCapability("browserName", "safari");
        capabilities.setCapability("nativeWebTap", "true");
//        capabilities.setCapability("unicodeKeyboard","true");
        capabilities.setCapability("name", parameters.get("name"));
//        capabilities.setCapability("browserstack.safari.driver", "2.48");
        capabilities.setCapability("build", parameters.get("build"));

        capabilities.setCapability("browserstack.appium_version","1.8.0");
        capabilities.setCapability("browserstack.console", "verbose");
        capabilities.setCapability("browserstack.appiumLogs", "true");
        capabilities.setCapability("browserstack.debug", "true");

        return capabilities;
    }
}
