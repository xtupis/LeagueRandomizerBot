package org.xtupis.randomizer.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomRunes {

    private static final String RUNES_URL = "https://ddragon.leagueoflegends.com/cdn/12.10.1/data/ru_RU/runesReforged.json";
    private final Random random = new Random();

    public String getRandomRunes() {
        try {
            return fetchRandomRunes();
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при получении рун";
        }
    }

    private String fetchRandomRunes() throws IOException {
        List<String> smallRunes = new ArrayList<>();
        List<String> secondaryRunes = new ArrayList<>();
        String mainRune = "";
        String secondaryRune1 = "";
        String secondaryRune2 = "";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(RUNES_URL);
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode runeArray = objectMapper.readTree(responseBody);

            JsonNode mainRuneTree = runeArray.get(random.nextInt(runeArray.size()));

            for (JsonNode slot : mainRuneTree.path("slots")) {
                List<String> tempRunes = new ArrayList<>();
                for (JsonNode rune : slot.path("runes")) {
                    tempRunes.add(rune.path("name").asText());
                }
                if (!tempRunes.isEmpty()) {
                    smallRunes.add(tempRunes.get(random.nextInt(tempRunes.size())));
                }
            }

            if (smallRunes.size() >= 4) {
                mainRune = smallRunes.remove(0);
            }

            JsonNode secondaryRuneTree;
            do {
                secondaryRuneTree = runeArray.get(random.nextInt(runeArray.size()));
            } while (secondaryRuneTree.path("name").asText().equals(mainRuneTree.path("name").asText()));

            for (JsonNode slot : secondaryRuneTree.path("slots")) {
                for (JsonNode rune : slot.path("runes")) {
                    secondaryRunes.add(rune.path("name").asText());
                }
            }

            if (secondaryRunes.size() >= 2) {
                secondaryRune1 = secondaryRunes.get(random.nextInt(secondaryRunes.size()));
                secondaryRune2 = secondaryRunes.get(random.nextInt(secondaryRunes.size()));

                while (secondaryRune1.equals(secondaryRune2)) {
                    secondaryRune2 = secondaryRunes.get(random.nextInt(secondaryRunes.size()));
                }
            }
        }

        return String.format(
                "**%s**\n" +
                        "- %s\n" +
                        "- %s\n" +
                        "- %s\n" +
                        "---------------------------\n" +
                        "- %s\n" +
                        "- %s",
                mainRune, smallRunes.get(0), smallRunes.get(1), smallRunes.get(2), secondaryRune1, secondaryRune2
        );
    }
}

