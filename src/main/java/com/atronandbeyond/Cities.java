package com.atronandbeyond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Cities {

    private int current = 0;
    private List<String> cities;
    private List<String> images;

    Cities(){
        images = new ArrayList<>();
        cities = new ArrayList<>(Arrays.asList("Columbia, Missouri",
                "St. Louis, Missouri",
                "Perry, Missouri",
                "City of Excelsior Springs, Missouri",
                "Topeka, Kansas",
                "Ames, Iowa",
                "Springfield, Missouri",
                "Bentonville, Arkansas",
                "Kansas City, Missouri",
                "Jefferson City, Missouri",
                "Kirksville, Missouri",
                "Bloomington, Illinois",
                "Manhattan, Kansas",
                "Cape Girardeau, Missouri",
                "Dallas, Texas",
                "Chicago, Illinois",
                "Hannibal, Missouri",
                "Joplin, Missouri",
                "Lee's Summit, Missouri",
                "Memphis, Tennessee"
        ));
    }

    public List<String> getImages() {
        return images;
    }

    String getNext() {
        return cities.get(++current % cities.size());
    }

}
