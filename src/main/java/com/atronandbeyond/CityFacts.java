package com.atronandbeyond;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class CityFacts {
    private List<String> facts;
    private String cityState;
    private OkHttpClient okHttpClient;
    private Logger logger;
    private Gson gson;

    CityFacts(String cityState){
        this.cityState = cityState;
        logger = Logger.getLogger(getClass().getSimpleName());
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        facts = new ArrayList<>();
        collectFacts();
    }

    private void collectFacts() {
//        logger.info("Collecting facts for " + cityState);
        String cityStateCleaned = cityState.replace(" ", "+");
        StringBuilder sb = new StringBuilder()
                .append("https://en.wikipedia.org/w/api.php?")
                .append("action=opensearch&")
                .append("search=")
                .append(cityStateCleaned)
                .append("&limit=100&namespace=0&format=json");
        logger.info(sb.toString());
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseString = response.body().string();
            JsonElement jsonElement = new JsonParser().parse(responseString);
            JsonElement factsElement = jsonElement.getAsJsonArray().get(2);
            Iterator<JsonElement> factsIterator = factsElement.getAsJsonArray().iterator();
            while (factsIterator.hasNext()) {
                String fact = factsIterator.next().toString();
                if (!fact.contains(":") && !fact.equals("\"\"")) {
                    facts.add(fact);
                }
            }
//            logger.info("fact list: " + facts.toString());
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
        logger.info(cityStateCleaned + " has " + facts.size() + " facts.");
    }

    public int getNumFacts() {
        return facts.size();
    }

    public List<String> getFacts() {
        return facts;
    }
}
