package com.atronandbeyond;

import org.apache.commons.lang3.time.StopWatch;
import tjenkinson.caspar_serverconnection.commands.CaspCmd;
import tjenkinson.caspar_serverconnection.commands.CaspReturn;
import tjenkinson.caspar_serverconnection.commands.CaspSocket;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
    private CityFacts cityFacts;

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
//        geocache.getZipForCityState(city);
        cityFacts = new CityFacts(city);
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
            int factCounter = 0;
            int factNumber = 0;
            boolean cityNotification = false;
            boolean songNotification = false;
            boolean factNotication = false;
            boolean changeImage = false;

            String city = nextCity();
            Songs.Song song = songs.getNext();

            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND_MIXER_BLEND_OVERLAY, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.ABSTRACT_BACKGROUND_MIXER_OPACITY, null)));
            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.IMAGE_DESATURATE, null)));

            StopWatch cityStopWatch = new StopWatch();
            StopWatch songStopWatch = new StopWatch();
            StopWatch factStopWatch = new StopWatch();
            StopWatch imageStopWatch = new StopWatch();
            imageStopWatch.start();
            songStopWatch.start();
            cityStopWatch.start();
            factStopWatch.start();

            while (running) {

                try {

                    if (changeImage) {
                        String image = cityImages.getImages().get(imageCounter++ % cityImages.getImages().size()).toString();
                        logger.info("image: " + image);
                        String casparCommand = Command.getCommand(Command.Cmd.IMAGE, Arrays.asList(getImageName(Paths.get(image))));
                        caspReturn = socket.runCmd(new CaspCmd(casparCommand));
                        logger.info("caspar image command: " + casparCommand);
                        logger.info("caspar image return: " + caspReturn.getResponse());
                        changeImage = false;
                    }

                    if (factNotication) {
                        cityFacts = new CityFacts(city);
                        if (cityFacts.getNumFacts() != 0) {
                            factNumber = factCounter++ % cityFacts.getFacts().size();
                            String heading = "Random Fact #" + String.valueOf(factNumber + 1);
                            String fact = cityFacts.getFacts().get(factNumber).replace("\"","");
//                            logger.info(heading);
//                            logger.info(fact);
                            socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.FACT_TITLE,
                                    Arrays.asList(heading, fact))));
                        } else {
                            logger.info(city + " has no facts");
                        }
                        factNotication = false;
                    }

                    if(cityNotification){
                        socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.CITY_STATE_TITLE,
                                Arrays.asList(city.split(",")[0], city.split(",")[1]))));
                        city = nextCity();
                        cityNotification = false;
                    }

                    if(songNotification){
                        song = songs.getNext();
//                        logger.info("new song: " + song.getFilename());
                        caspReturn = socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.SONG, Arrays.asList(song.getFilename()))));
                        logger.info(caspReturn.getResponse());
                        socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.MUSIC_TITLE,
                                Arrays.asList(song.getArtist(), song.getSong(), song.getAlbum()))));
                        songNotification = false;
                    }

                    Thread.sleep(SLEEP_TIME);

                    socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.CITY_STATE_STOP, null)));
                    socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.MUSIC_TITLE_STOP, null)));
                    socket.runCmd(new CaspCmd(Command.getCommand(Command.Cmd.FACT_TITLE_STOP, null)));
                } catch (InterruptedException | ArithmeticException e) {
                    logger.severe(e.getMessage());
                }

                if (songStopWatch.getTime() > song.getDuration()) {
                    songNotification = true;
                    songStopWatch.reset();
                    songStopWatch.start();
                }

                if (cityStopWatch.getTime() > Long.valueOf(config.getCityStateTime())) {
                    logger.info("Current city: " + city);
                    cityNotification = true;
                    cityStopWatch.reset();
                    cityStopWatch.start();
                }

                if (factStopWatch.getTime() > Long.valueOf(config.getFactTime())) {
                    factNotication = true;
                    factStopWatch.reset();
                    factStopWatch.start();
                }

                if (imageStopWatch.getTime() > Long.valueOf(config.getImageTime())) {
                    changeImage = true;
                    imageStopWatch.reset();
                    imageStopWatch.start();
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
