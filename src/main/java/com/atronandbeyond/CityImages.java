package com.atronandbeyond;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CityImages {
    List<Path> images;
    private Logger logger;
    String city;
    private Config config;

    CityImages(String city) {
        this.city = city;
        config = new Config();
        logger = Logger.getLogger(getClass().getSimpleName());
        images = new ArrayList<>();
        init();
    }
    private void init() {
        String cityStateCleaned = city.replace(" ", "+");
        String imageDir = config.getMediaDirectory() + File.separator + cityStateCleaned;
        if (Files.exists(Paths.get(imageDir))) {

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(imageDir))) {
                for (Path path : stream) {
                    images.add(path);
                }
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }

        } else {
            logger.severe(imageDir + " does not exist.");
        }
    }

    public List<Path> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "CityImages{" +
                "images=" + images +
                ", city='" + city + '\'' +
                ", config=" + config +
                '}';
    }
}
