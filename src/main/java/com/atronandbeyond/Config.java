package com.atronandbeyond;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

class Config {
    private String filename = "config.properties";
    private Properties properties = new Properties();
    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    Config() {
        try {
            properties.load(new FileReader(new File(getClass().getClassLoader().getResource(filename).getFile())));
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    String getMediaDirectory() {
        return properties.getProperty("media_directory");
    }

    String getSleeptime() {
        return properties.getProperty("sleep_time");
    }

    String getCx() {
        return System.getenv("social_weather_cx");
    }

    String getApi() {
        return System.getenv("social_weather_api");
    }

    String getMinImageSize() {
        return properties.getProperty("min_image_size");
    }

    String getCityStateTime(){
        return properties.getProperty("city_state_time");
    }

    String getCasparAddr(){
        return properties.getProperty("caspAddress");
    }

    String getCasparPort(){
        return properties.getProperty("caspPort");
    }
}
