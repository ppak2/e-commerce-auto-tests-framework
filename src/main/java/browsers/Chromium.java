package browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class Chromium implements Browser {


    @Override
    public Name name() { return Name.Chromium;}

    @Override
    public boolean isMobile(){
        return true;
    }

    @Override
    public Capabilities capabilities(Map<String, String> parameters) {

        final DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("device", parameters.get("device"));
        capabilities.setCapability("real_mobile", "true");
        capabilities.setCapability("browser","chrome");
        capabilities.setCapability("gpsEnabled","false");
        capabilities.setCapability("name", parameters.get("name"));   //
        capabilities.setCapability("build", parameters.get("build"));

        capabilities.setCapability("browserstack.appium_version", "1.9.1");
        capabilities.setCapability("browserstack.console","verbose");
        capabilities.setCapability("browserstack.appiumLogs","true");
        capabilities.setCapability("browserstack.debug","true");

        return capabilities;
    }
}
