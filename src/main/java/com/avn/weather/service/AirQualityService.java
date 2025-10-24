package com.avn.weather.service;

import com.avn.weather.model.airquality.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 空气质量API服务类
 */
public class AirQualityService {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiHost;
    private final String jwtToken;

    public AirQualityService(String apiHost, String jwtToken) {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.apiHost = apiHost;
        this.jwtToken = jwtToken;
    }

    /**
     * 获取指定位置的实时空气质量数据
     * @param latitude 纬度
     * @param longitude 经度
     * @return 空气质量数据的CompletableFuture
     */
    public CompletableFuture<AirQuality> getCurrentAirQuality(double latitude, double longitude) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("=== 空气质量API请求 ===");
                System.out.println("请求坐标: " + latitude + ", " + longitude);
                
                String url = String.format("https://%s/airquality/v1/current/%.2f/%.2f", 
                                         apiHost, latitude, longitude);
                System.out.println("请求URL: " + url);
                
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Authorization", "Bearer " + jwtToken);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Accept-Encoding", "gzip");
                httpGet.setHeader("User-Agent", "WeatherApp/1.0");
                
                System.out.println("空气质量API请求头设置完成，开始发送请求...");
                
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int statusCode = response.getCode();
                    System.out.println("空气质量API响应状态码: " + statusCode);
                    
                    if (statusCode == 200) {
                        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                        System.out.println("空气质量API响应内容长度: " + (responseBody != null ? responseBody.length() : 0));
                        System.out.println("空气质量API响应内容: " + (responseBody != null && responseBody.length() > 300 ? 
                            responseBody.substring(0, 300) + "..." : responseBody));
                        
                        AirQuality result = parseAirQualityResponse(responseBody);
                        System.out.println("空气质量数据解析" + (result != null ? "成功" : "失败"));
                        return result;
                    } else {
                        System.err.println("空气质量API请求失败，状态码: " + statusCode);
                        return null;
                    }
                }
            } catch (IOException | ParseException e) {
                System.err.println("空气质量API请求异常: " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * 解析空气质量API响应
     */
    private AirQuality parseAirQualityResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        
        AirQuality airQuality = new AirQuality();
        
        // 解析metadata
        if (root.has("metadata")) {
            JsonNode metadataNode = root.get("metadata");
            AirQuality.Metadata metadata = new AirQuality.Metadata();
            if (metadataNode.has("tag")) {
                metadata.setTag(metadataNode.get("tag").asText());
            }
            airQuality.setMetadata(metadata);
        }
        
        // 解析indexes
        if (root.has("indexes")) {
            List<AQIIndex> indexes = new ArrayList<>();
            for (JsonNode indexNode : root.get("indexes")) {
                AQIIndex index = parseAQIIndex(indexNode);
                indexes.add(index);
            }
            airQuality.setIndexes(indexes);
        }
        
        // 解析pollutants
        if (root.has("pollutants")) {
            List<Pollutant> pollutants = new ArrayList<>();
            for (JsonNode pollutantNode : root.get("pollutants")) {
                Pollutant pollutant = parsePollutant(pollutantNode);
                pollutants.add(pollutant);
            }
            airQuality.setPollutants(pollutants);
        }
        
        // 解析stations
        if (root.has("stations")) {
            List<Station> stations = new ArrayList<>();
            for (JsonNode stationNode : root.get("stations")) {
                Station station = parseStation(stationNode);
                stations.add(station);
            }
            airQuality.setStations(stations);
        }
        
        return airQuality;
    }

    /**
     * 解析AQI指数
     */
    private AQIIndex parseAQIIndex(JsonNode indexNode) {
        AQIIndex index = new AQIIndex();
        
        if (indexNode.has("code")) index.setCode(indexNode.get("code").asText());
        if (indexNode.has("name")) index.setName(indexNode.get("name").asText());
        if (indexNode.has("aqi")) index.setAqi(indexNode.get("aqi").asDouble());
        if (indexNode.has("aqiDisplay")) index.setAqiDisplay(indexNode.get("aqiDisplay").asText());
        if (indexNode.has("level")) index.setLevel(indexNode.get("level").asText());
        if (indexNode.has("category")) index.setCategory(indexNode.get("category").asText());
        
        // 解析颜色
        if (indexNode.has("color")) {
            JsonNode colorNode = indexNode.get("color");
            AQIIndex.AQIColor color = new AQIIndex.AQIColor();
            if (colorNode.has("red")) color.setRed(colorNode.get("red").asInt());
            if (colorNode.has("green")) color.setGreen(colorNode.get("green").asInt());
            if (colorNode.has("blue")) color.setBlue(colorNode.get("blue").asInt());
            if (colorNode.has("alpha")) color.setAlpha(colorNode.get("alpha").asDouble());
            index.setColor(color);
        }
        
        // 解析首要污染物
        if (indexNode.has("primaryPollutant")) {
            JsonNode pollutantNode = indexNode.get("primaryPollutant");
            AQIIndex.PrimaryPollutant primaryPollutant = new AQIIndex.PrimaryPollutant();
            if (pollutantNode.has("code")) primaryPollutant.setCode(pollutantNode.get("code").asText());
            if (pollutantNode.has("name")) primaryPollutant.setName(pollutantNode.get("name").asText());
            if (pollutantNode.has("fullName")) primaryPollutant.setFullName(pollutantNode.get("fullName").asText());
            index.setPrimaryPollutant(primaryPollutant);
        }
        
        // 解析健康建议
        if (indexNode.has("health")) {
            JsonNode healthNode = indexNode.get("health");
            AQIIndex.HealthAdvice health = new AQIIndex.HealthAdvice();
            if (healthNode.has("effect")) health.setEffect(healthNode.get("effect").asText());
            
            if (healthNode.has("advice")) {
                JsonNode adviceNode = healthNode.get("advice");
                AQIIndex.HealthAdvice.Advice advice = new AQIIndex.HealthAdvice.Advice();
                if (adviceNode.has("generalPopulation")) {
                    advice.setGeneralPopulation(adviceNode.get("generalPopulation").asText());
                }
                if (adviceNode.has("sensitivePopulation")) {
                    advice.setSensitivePopulation(adviceNode.get("sensitivePopulation").asText());
                }
                health.setAdvice(advice);
            }
            index.setHealth(health);
        }
        
        return index;
    }

    /**
     * 解析污染物
     */
    private Pollutant parsePollutant(JsonNode pollutantNode) {
        Pollutant pollutant = new Pollutant();
        
        if (pollutantNode.has("code")) pollutant.setCode(pollutantNode.get("code").asText());
        if (pollutantNode.has("name")) pollutant.setName(pollutantNode.get("name").asText());
        if (pollutantNode.has("fullName")) pollutant.setFullName(pollutantNode.get("fullName").asText());
        
        // 解析浓度
        if (pollutantNode.has("concentration")) {
            JsonNode concentrationNode = pollutantNode.get("concentration");
            Pollutant.Concentration concentration = new Pollutant.Concentration();
            if (concentrationNode.has("value")) concentration.setValue(concentrationNode.get("value").asDouble());
            if (concentrationNode.has("unit")) concentration.setUnit(concentrationNode.get("unit").asText());
            pollutant.setConcentration(concentration);
        }
        
        // 解析分指数
        if (pollutantNode.has("subIndexes")) {
            List<Pollutant.SubIndex> subIndexes = new ArrayList<>();
            for (JsonNode subIndexNode : pollutantNode.get("subIndexes")) {
                Pollutant.SubIndex subIndex = new Pollutant.SubIndex();
                if (subIndexNode.has("code")) subIndex.setCode(subIndexNode.get("code").asText());
                if (subIndexNode.has("aqi")) subIndex.setAqi(subIndexNode.get("aqi").asDouble());
                if (subIndexNode.has("aqiDisplay")) subIndex.setAqiDisplay(subIndexNode.get("aqiDisplay").asText());
                subIndexes.add(subIndex);
            }
            pollutant.setSubIndexes(subIndexes);
        }
        
        return pollutant;
    }

    /**
     * 解析监测站
     */
    private Station parseStation(JsonNode stationNode) {
        Station station = new Station();
        
        if (stationNode.has("id")) station.setId(stationNode.get("id").asText());
        if (stationNode.has("name")) station.setName(stationNode.get("name").asText());
        
        return station;
    }

    /**
     * 关闭HTTP客户端
     */
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            // 静默处理关闭异常
        }
    }
}