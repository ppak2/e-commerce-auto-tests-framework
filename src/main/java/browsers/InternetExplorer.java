package browsers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class InternetExplorer implements Browser {

    @Override
    public Name name()  {
        return Name.InternetExplorer;
    }


    @Override
    public Capabilities capabilities(Map<String, String> parameters) {

//        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//        caps.setCapability("platform", "Windows 10");
//        caps.setCapability("version", "11.285");
//        caps.setCapability("screenResolution", "1920x1080");

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("internet explorer");
        capabilities.setPlatform(Platform.WINDOWS);
        capabilities.setVersion("11");
        capabilities.setCapability("resolution", "1920x1080");
//        capabilities.setCapability("browserstack.ie.arch", "x64");
//        capabilities.setCapability("browserstack.ie.compatibility", "11001");
        capabilities.setCapability("browserstack.ie.enablePopups", true);
        capabilities.setCapability("name", parameters.get("name"));
        capabilities.setCapability("build", parameters.get("build"));

        capabilities.setCapability("browserstack.console", "verbose");
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.networkLogs", "true");

        return capabilities;



//        return caps;
    }
}
