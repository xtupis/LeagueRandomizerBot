package org.xtupis.randomizer.ai;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomChampion {

    private static final String RIOT_API_URL = "https://ddragon.leagueoflegends.com/cdn/11.20.1/data/ru_RU/champion.json";

    public String getRandomChampion() {
        List<String> champions = getChampionList();
        if (champions.isEmpty()) {
            return "Не удалось получить список чемпионов";
        }

        String randomChampion = champions.get(new Random().nextInt(champions.size()));

        String championImage = getChampionImage(randomChampion);

        return randomChampion + " - " + championImage;
    }

    private List<String> getChampionList() {
        List<String> champions = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RIOT_API_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return champions;
            }

            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject data = jsonObject.getJSONObject("data");

            data.keys().forEachRemaining(key -> {
                JSONObject champData = data.getJSONObject(key);
                champions.add(champData.getString("name"));
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return champions;
    }

    private String getChampionImage(String championName) {
        String championKey = getChampionKeyByName(championName);
        if (championKey == null) {
            return "Изображение не найдено";
        }
        return "https://ddragon.leagueoflegends.com/cdn/11.20.1/img/champion/" + championKey + ".png";
    }

    private String getChampionKeyByName(String championName) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RIOT_API_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return null;
            }

            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject data = jsonObject.getJSONObject("data");

            for (String key : data.keySet()) {
                JSONObject champData = data.getJSONObject(key);
                if (champData.getString("name").equals(championName)) {
                    return key;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

