package org.xtupis.randomizer.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class RandomBuild {

    private static final String API_KEY = "RGAPI-caa53341-1f4e-4216-a621-95911bbb1f67";
    private static final String BASE_URL = "https://ddragon.leagueoflegends.com/cdn/12.10.1/data/ru_RU/item.json";

    private List<String> fetchLegendaryItems() throws IOException {
        List<String> legendaryItems = new ArrayList<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL);
            request.addHeader("X-Riot-Token", API_KEY);

            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");

            dataNode.fieldNames().forEachRemaining(itemId -> {
                JsonNode itemNode = dataNode.path(itemId);
                String itemName = itemNode.path("name").asText();
                String itemDescription = itemNode.path("description").asText();

                if (itemDescription.contains("rarityLegendary")) {
                    legendaryItems.add(itemName);
                }
            });
        }

        return legendaryItems;
    }

    public String getRandomBuild() {
        List<String> legendaryItems;
        try {
            legendaryItems = fetchLegendaryItems();

            if (legendaryItems.size() < 6) {
                return "Ошибка: недостаточно легендарных предметов в базе данных";
            }

            Collections.shuffle(legendaryItems);
            List<String> selectedItems = legendaryItems.subList(0, 6);

            StringBuilder build = new StringBuilder("\uD83C\uDFAEАбсолютно Случайная Сборка\uD83C\uDFAE\n");
            for (int i = 0; i < selectedItems.size(); i++) {
                build.append(i + 1).append(". ").append(selectedItems.get(i)).append("\n");
            }

            return build.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при получении сборки";
        }
    }
}
