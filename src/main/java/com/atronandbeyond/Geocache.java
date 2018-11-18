package com.atronandbeyond;

import com.atronandbeyond.domains.GeocodeResponse;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class Geocache{
    Logger logger;
    OkHttpClient okHttpClient;
    Config config;
    Gson gson;
    public Geocache(){
        logger = Logger.getLogger(getClass().getSimpleName());
        okHttpClient = new OkHttpClient();
        config = new Config();
        gson = new Gson();
    }
    public String getZipForCityState(String cityState) {
        String cityStateCleaned = cityState.replace(" ", "+");
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/geocode/json?address=")
                .append(cityStateCleaned)
                .append("&key=")
                .append(config.getApi());
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        logger.info(sb.toString());
        try (Response response = okHttpClient.newCall(request).execute()) {
            GeocodeResponse geocodeResponse = gson.fromJson(response.body().string(), GeocodeResponse.class);
            logger.info(geocodeResponse.toString());
        } catch(IOException ex){
            logger.severe(ex.getMessage());
        }
        return "";
    }
}