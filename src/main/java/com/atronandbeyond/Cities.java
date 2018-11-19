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
        cities = new ArrayList<>(Arrays.asList("Columbia, Missouri", "Springfield, Missouri", "Kansas City, Missouri",
                "Saint Louis, Missouri", "Jefferson City, Missouri"));
    }

    public List<String> getImages() {
        return images;
    }

    String getNext() {
        return cities.get(++current % cities.size());
    }

}
