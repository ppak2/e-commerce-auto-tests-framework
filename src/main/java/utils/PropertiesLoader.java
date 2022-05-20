package utils;

import _pages.enums.ChannelType;
import lombok.SneakyThrows;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Properties;

import static _pages.enums.ChannelType.Country.*;


public final class PropertiesLoader {


    private static EnumMap<ChannelType.Country, Properties> channelsProperties;

    public static EnumMap<ChannelType.Country, Properties> getChannelsProperties(){

        loadProperties();
        return channelsProperties;
    }

    @SneakyThrows(IOException.class)
    private static void loadProperties(){

        Properties seProps = new Properties();
        seProps.load(new FileInputStream("src/main/resources/se_channels.properties"));

        Properties noProps = new Properties();
        noProps.load(new FileInputStream("src/main/resources/no_channels.properties"));


        Properties fiProps = new Properties();
        fiProps.load(new FileInputStream("src/main/resources/fi_channels.properties"));


        Properties dkProps = new Properties();
        dkProps.load(new FileInputStream("src/main/resources/dk_channel.properties"));

        channelsProperties = new EnumMap<>(ChannelType.Country.class);
        channelsProperties.put(SE, seProps);
        channelsProperties.put(NO, noProps);
        channelsProperties.put(FI, fiProps);
        channelsProperties.put(DK, dkProps);
    }
}
