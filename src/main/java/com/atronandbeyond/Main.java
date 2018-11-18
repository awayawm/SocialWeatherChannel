package com.atronandbeyond;

import org.apache.commons.lang3.time.StopWatch;
import tjenkinson.caspar_serverconnection.commands.CaspCmd;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {
    private Logger logger;
    private int SLEEP_TIME;
    private Cities cities;
    private Config config;
    private CityImages cityImages;
    private Downloader downloader;
    private Songs songs;
    private Geocache geocache;

    private Main() {
        cities = new Cities();
        config = new Config();
        songs = new Songs();
        SLEEP_TIME = Integer.valueOf(config.getSleeptime());
        logger = Logger.getLogger(getClass().getSimpleName());
        downloader = new Downloader();
        geocache = new Geocache();
    }

    private String nextCity() {
        String city = cities.getNext();
        downloader.getImagesForCityState(city);
        cityImages = new CityImages(city);
        geocache.getZipForCityState(city);
        return city;
    }

    private String getImageName(Path path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path.getName(path.getNameCount()-2));
        sb.append("/");
        sb.append(path.getName(path.getNameCount()-1));
        logger.info(sb.toString());
        return sb.toString();
    }

    private void startLoop() {
        CaspSocket socket = null;
        try {
            socket = new CaspSocket(config.getCasparAddr(), Integer.valueOf(config.getCasparPort()));
            boolean running = true;
            int imageCounter = 0;
            String city = nextCity();
            Songs.Song song = songs.getNext();
            socket.runCmd(new CaspCmd("play 1-1 " + song.getFilename()));
            StopWatch cityStopWatch = new StopWatch();
            StopWatch songStopWatch = new StopWatch();
            songStopWatch.start();
            cityStopWatch.start();

            while (running) {

                if (songStopWatch.getTime() > song.getDuration()) {
                    song = songs.getNext();
                    logger.info("new song: " + song.getFilename());
                    socket.runCmd(new CaspCmd("play 1-1 " + song.getFilename()));
                    songStopWatch.reset();
                    songStopWatch.start();
                }

                if (cityStopWatch.getTime() > Long.valueOf(config.getCityStateTime())) {
                    city = nextCity();
                    logger.info("Current city: " + city);
                    cityStopWatch.reset();
                    cityStopWatch.start();
                }

                try {
                    String image = cityImages.getImages().get(imageCounter % cityImages.getImages().size()).toString();
                    logger.info(image);
                    imageCounter++;
                    socket.runCmd(new CaspCmd("play 1-2 " + getImageName(Paths.get(image)) + " mix 100 easeinsine"));
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
