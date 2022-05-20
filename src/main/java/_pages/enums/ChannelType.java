package _pages.enums;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.vavr.API.*;


public enum ChannelType {

    TRADEMAX,
    FURNITUREBOX,
    CHILLI,
    KODIN1,
    WEGOT;

    public static ChannelType defineChannelType(String url) {

        try {
            String host = new URL(url).getHost();

            //If url ends with '/'
            if(!Character.isLetter(host.charAt(host.length()-1))) {
                host = host.substring(0, host.length()-1);
            }
            String hostM;

            if (host.startsWith("www") || !host.matches(".*trademax-test.com")) {
                hostM = host.replace("www.", "");

            } else {
                hostM = host;
            }

            return Match(hostM).of(Case($(isTrademax), TRADEMAX),
                    Case($(isFurniturebox), FURNITUREBOX),
                    Case($(isChilli), CHILLI),
                    Case($(isWegot), WEGOT),
                    Case($(), KODIN1));

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Country {

        SE,
        NO,
        FI,
        DK;

        public static ChannelType.Country defineCountry(String url) {

            return Match(url).of(Case($(isSweden), SE),
                    Case($(isNorway), NO),
                    Case($(isFinland), FI),
                    Case($(), DK)
            );
        }
    }

    public enum Environment {

        PROD,
        TEST;

        public static ChannelType.Environment defineEnvironment(String url) {
            return isTestEnv.test(url) ? TEST : PROD;
        }
    }

    private static Predicate<String> isTrademax = val->val.startsWith("trademax");
    private static Predicate<String> isFurniturebox = val->val.startsWith("furniturebox");
    private static Predicate<String> isChilli = val->val.startsWith("chilli");
    private static Predicate<String> isWegot = val->val.startsWith("wegot");

    private static Predicate<String> isSweden = url->url.contains("-se-") || url.contains(".se");
    private static Predicate<String> isNorway = url->url.contains("-no-") || url.contains(".no");
    private static Predicate<String> isFinland = url->url.contains("-fi-") || url.contains(".fi") || url.contains("kodin1");

    private static Predicate<String> isTestEnv = url->{
        String pattern = "(-)(\\d+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);
        return m.find();
    };
}
