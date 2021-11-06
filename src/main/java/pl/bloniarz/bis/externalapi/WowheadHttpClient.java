package pl.bloniarz.bis.externalapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import pl.bloniarz.bis.externalapi.model.LootSource;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponse;
import pl.bloniarz.bis.externalapi.model.WowheadItemResponseStat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WowheadHttpClient {

    private final HttpClient httpClient;
    private final XmlMapper xmlMapper;
    private final String searchBaseUrl;
    private final String itemDataBaseUrl;
    private final ObjectMapper objectMapper;

    public WowheadHttpClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.searchBaseUrl = "https://www.wowhead.com/items/live-only:on/min-level:%s/max-level:%s/?filter=%s";
        this.itemDataBaseUrl = "https://www.wowhead.com/item=%s&xml";
    }

    public List<WowheadItemResponse> getItemsListForLootSource(LootSource lootSource) {
        List<String> allItems = null;
        try {
            allItems = getAllItemsIdsFromWowhead(lootSource);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return allItems.stream()
                .map(id -> {
                    try {
                        WowheadItemResponse item = findItemFromWowheadById(id);
                        item.setDropInstance(lootSource.getSourceName());
                        item = mapStatsFromJsonForItem(item);
                        return item;
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private WowheadItemResponse mapStatsFromJsonForItem(WowheadItemResponse wowheadItemResponse) throws JsonProcessingException {
        wowheadItemResponse.setStatJsonData("{\n" + wowheadItemResponse.getStatJsonData() + "\n}");
        WowheadItemResponseStat wowheadItemResponseStat = objectMapper.readValue(wowheadItemResponse.getStatJsonData(), WowheadItemResponseStat.class);
        wowheadItemResponseStat.prepareProcentSecondaryStats();
        wowheadItemResponse.setStats(wowheadItemResponseStat);
        return wowheadItemResponse;
    }
    private WowheadItemResponse findItemFromWowheadById(String id) throws IOException, InterruptedException {
        String requestUrl = String.format(itemDataBaseUrl, id);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestUrl))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        body = body.replace("<wowhead>", "").replace("</wowhead>", "");
        return xmlMapper.readValue(body, WowheadItemResponse.class);
    }
    private List<String> getAllItemsIdsFromWowhead(LootSource lootPlaces) throws IOException, InterruptedException {
        String url = String.format(searchBaseUrl, lootPlaces.getMinIlvl(), lootPlaces.getMaxIlvl(), lootPlaces.getSearchRequirements());
        URL htmlUrl = new URL(url);
        StringBuilder body = new StringBuilder();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(htmlUrl.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            body.append(inputLine);
        in.close();

        return extractIdsFromHtml(body.toString());
    }
    private List<String> extractIdsFromHtml(String htmlString) {
        String[] words = htmlString.split("addData");
        words = words[1].split("var tabsGroups");
        String s = words[0].substring(7, words[0].length() - 2);

        List<String> idList = new ArrayList<>();
        words = s.split("\\{\"name_enus");
        for (int i = 0; i < words.length - 1; i++) {
            String word = words[i].substring(words[i].length() - 8, words[i].length() - 2);
            if(!word.startsWith("13"))
                idList.add(word);
        }
        return idList;
    }

}
