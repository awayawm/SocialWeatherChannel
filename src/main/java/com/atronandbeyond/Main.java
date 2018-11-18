package com.atronandbeyond;

import org.apache.commons.lang3.time.StopWatch;
import tjenkinson.caspar_serverconnection.commands.CaspCmd;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private Logger logger;
    private int SLEEP_TIME;
    private Cities cities;
    private Config config;
    private CityImages cityImages;
    private Downloader downloader;

    private Main() {
        cities = new Cities();
        config = new Config();
        SLEEP_TIME = Integer.valueOf(config.getSleeptime());
        logger = Logger.getLogger(getClass().getSimpleName());
        downloader = new Downloader();
    }

    private String nextCity() {
        String city = cities.getNext();
        downloader.getImagesForCityState(city);
        cityImages = new CityImages(city);
        return city;
    }

    private void startLoop() {
        CaspSocket socket = null;
        try {
            socket = new CaspSocket(config.getCasparAddr(), Integer.valueOf(config.getCasparPort()));
            boolean running = true;
            int imageCounter = 0;
            String city = nextCity();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            while (running) {

                if (stopWatch.getTime() > Long.valueOf(config.getCityStateTime())) {
                    city = nextCity();
                    logger.info("Current city: " + city);
                    stopWatch.reset();
                    stopWatch.start();
                }

                try {
                    logger.info(cityImages.getImages().get(imageCounter%cityImages.getImages().size()).toString());
                    imageCounter++;
                    socket.runCmd(new CaspCmd("info"));
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    logger.severe(e.getMessage());
                }
            }

            socket.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.startLoop();
    }
}
