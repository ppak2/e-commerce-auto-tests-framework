package pages;

import java.net.MalformedURLException;
import java.net.URL;

public enum ChannelType {

    TRADEMAX,
    FURNITUREBOX,
    CHILLI,
    KODIN1,
    WEGOT;

    public static ChannelType defineChannelType(String url) {

        try {
            String host = new URL(url).getHost();
            String hostM;

            if (host.startsWith("www") || !host.matches(".*trademax-test.com")) {
                hostM = host.replace("www.", "");

            } else {
                hostM = host;
            }

            if (hostM.startsWith("trademax")) return TRADEMAX;
            else if (hostM.startsWith("furniturebox")) return FURNITUREBOX;
            else if (hostM.startsWith("chilli")) return CHILLI;
            else if (hostM.startsWith("kodin1")) return KODIN1;
            else return WEGOT;


        } catch (MalformedURLException e) {

            throw new RuntimeException(e);
        }
    }

    public enum Country {

        SE,
        NO,
        FI,
        DK;

        public static Country defineCountry(String url) {

            if (url.contains("-se-") || url.contains(".se")) return SE;
            else if (url.contains("-no-") || url.contains(".no")) return NO;
            else if (url.contains("-fi-") || url.contains(".fi") || url.contains("kodin1")) return FI;
            else return DK;
        }
    }
}
