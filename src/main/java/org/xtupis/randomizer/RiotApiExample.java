package org.xtupis.randomizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RiotApiExample {
    public static void main(String[] args) {
        String apiKey = "RGAPI-caa53341-1f4e-4216-a621-95911bbb1f67";
        String urlString = "https://ddragon.leagueoflegends.com/cdn/14.5.1/data/en_US/champion.json";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Riot-Token", apiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}