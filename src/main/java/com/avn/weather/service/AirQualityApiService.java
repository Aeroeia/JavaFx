package com.avn.weather.service;

import com.alibaba.fastjson.JSON;
import com.avn.weather.model.quality.AirQuality;
import com.avn.weather.model.quality.AirQualityResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 空气质量API服务
 * 调用和风天气空气质量API获取空气质量数据
 */
public class AirQualityApiService {
    
    // API配置 - 实际使用时需要替换为真实的API Host和Token
    private static final String API_HOST = "your_api_host";
    private static final String API_TOKEN = "your_token";
    private static final String AIR_QUALITY_API_URL = "https://" + API_HOST + "/air/now";
    
    private final CloseableHttpClient httpClient;
    
    public AirQualityApiService() {
        this.httpClient = HttpClients.createDefault();
    }
    
    /**
     * 根据地理位置ID获取当前空气质量数据
     * @param locationId 地理位置ID
     * @return 空气质量数据，如果获取失败返回null
     */
    public AirQuality getAirQuality(String locationId) throws ParseException {
        try {
            // 构建请求URL
            String url = AIR_QUALITY_API_URL + "?location=" + locationId;
            
            // 创建HTTP请求
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Bearer " + API_TOKEN);
            httpGet.setHeader("Accept-Encoding", "gzip");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                // 解析JSON响应
                AirQualityResponse airResponse = JSON.parseObject(responseBody, AirQualityResponse.class);
                
                if ("200".equals(airResponse.getCode()) && airResponse.getNow() != null) {
                    return convertToAirQuality(airResponse);
                } else {
                    System.err.println("空气质量查询失败，状态码: " + airResponse.getCode());
                    return null;
                }
            }
            
        } catch (IOException e) {
            System.err.println("空气质量查询请求失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 将API响应转换为应用内部的AirQuality模型
     */
    private AirQuality convertToAirQuality(AirQualityResponse response) {
        AirQualityResponse.AirQualityNow now = response.getNow();
        
        // 解析数值
        int aqi = parseIntSafely(now.getAqi());
        double pm25 = parseDoubleSafely(now.getPm2p5());
        double pm10 = parseDoubleSafely(now.getPm10());
        double so2 = parseDoubleSafely(now.getSo2());
        double no2 = parseDoubleSafely(now.getNo2());
        double co = parseDoubleSafely(now.getCo());
        double o3 = parseDoubleSafely(now.getO3());
        
        // 获取空气质量等级和描述
        String level = now.getCategory() != null ? now.getCategory() : "未知";
        String description = getAirQualityDescription(aqi);
        String advice = getHealthAdvice(aqi);
        
        // 获取数据来源
        String source = "和风天气";
        if (response.getRefer() != null && response.getRefer().getSources() != null && !response.getRefer().getSources().isEmpty()) {
            source = response.getRefer().getSources().get(0);
        }
        
        return new AirQuality(
            LocalDateTime.now(),
            aqi,
            pm25,
            pm10,
            so2,
            no2,
            co,
            o3,
            level,
            description,
            advice,
            source
        );
    }
    
    /**
     * 安全解析整数
     */
    private int parseIntSafely(String value) {
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 安全解析双精度浮点数
     */
    private double parseDoubleSafely(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * 根据AQI值获取空气质量描述
     */
    private String getAirQualityDescription(int aqi) {
        if (aqi <= 50) {
            return "空气质量令人满意，基本无空气污染";
        } else if (aqi <= 100) {
            return "空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响";
        } else if (aqi <= 150) {
            return "易感人群症状有轻度加剧，健康人群出现刺激症状";
        } else if (aqi <= 200) {
            return "进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响";
        } else if (aqi <= 300) {
            return "心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状";
        } else {
            return "健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病";
        }
    }
    
    /**
     * 根据AQI值获取健康建议
     */
    private String getHealthAdvice(int aqi) {
        if (aqi <= 50) {
            return "各类人群可多参加户外活动，多呼吸一下清新的空气。";
        } else if (aqi <= 100) {
            return "极少数异常敏感人群应减少户外活动";
        } else if (aqi <= 150) {
            return "儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼";
        } else if (aqi <= 200) {
            return "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外运动";
        } else if (aqi <= 300) {
            return "儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动";
        } else {
            return "儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动";
        }
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
            System.err.println("关闭HTTP客户端失败: " + e.getMessage());
        }
    }
}