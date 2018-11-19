package com.atronandbeyond;

import org.apache.commons.lang3.time.StopWatch;
import tjenkinson.caspar_serverconnection.commands.CaspCmd;
import tjenkinson.caspar_serverconnection.commands.CaspReturn;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
        CaspReturn caspReturn = null;
        try {
            socket = new CaspSocket(config.getCasparAddr(), Integer.valueOf(config.getCasparPort()));
            boolean running = true;
            int imageCounter = 0;

            String city = nextCity();
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.CITY_STATE_TITLE,
                    Arrays.asList(city.split(",")[0], city.split(",")[1]))));
            Songs.Song song = songs.getNext();

            caspReturn = socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.SONG, Arrays.asList(song.getFilename()))));
            logger.info(caspReturn.getResponse());

            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND_MIXER_BLEND_OVERLAY, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND_MIXER_HALF_OPACITY, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.IMAGE_DESATURATE, null)));

            StopWatch cityStopWatch = new StopWatch();
            StopWatch songStopWatch = new StopWatch();
            songStopWatch.start();
            cityStopWatch.start();

            while (running) {

                if (songStopWatch.getTime() > song.getDuration()) {
                    song = songs.getNext();
                    logger.info("new song: " + song.getFilename());
                    caspReturn = socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.SONG, Arrays.asList(song.getFilename()))));
                    logger.info(caspReturn.getResponse());
                    songStopWatch.reset();
                    songStopWatch.start();
                }

                if (cityStopWatch.getTime() > Long.valueOf(config.getCityStateTime())) {
                    city = nextCity();
                    logger.info("Current city: " + city);
                    socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.CITY_STATE_TITLE,
                            Arrays.asList(city.split(",")[0], city.split(",")[1]))));
                    cityStopWatch.reset();
                    cityStopWatch.start();
                }

                try {
                    String image = cityImages.getImages().get(imageCounter % cityImages.getImages().size()).toString();
                    logger.info(image);
                    imageCounter++;
                    caspReturn = socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.IMAGE, Arrays.asList(getImageName(Paths.get(image))))));
                    logger.info(caspReturn.getResponse());

                    Thread.sleep(SLEEP_TIME);
                    socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.CITY_STATE_STOP, null)));
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
