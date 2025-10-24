package com.avn.weather.service;

import com.alibaba.fastjson.JSON;
import com.avn.weather.api.TokenUtil;
import com.avn.weather.model.weather.DailyWeather;
import com.avn.weather.model.weather.WeatherResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * 天气API服务
 * 调用和风天气API获取天气预报数据
 */
public class WeatherApiService {
    
    // API配置
    private static final String WEATHER_API_URL = "https://nv3md8tnqq.re.qweatherapi.com/v7/weather/7d";
    
    private final CloseableHttpClient httpClient;
    
    public WeatherApiService() {
        this.httpClient = HttpClients.createDefault();
    }
    
    /**
     * 获取7天天气预报
     * @param locationId 地理位置ID
     * @return 天气预报列表
     */
    public List<DailyWeather> getWeatherForecast(String locationId) {
        try {
            System.out.println("=== 天气API请求 ===");
            System.out.println("请求位置ID: " + locationId);
            
            // 获取token
            String token = TokenUtil.getToken();
            
            // 构建请求URL
            String url = WEATHER_API_URL + "?location=" + locationId;
            System.out.println("请求URL: " + url);
            
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Bearer " + token);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Accept-Encoding", "gzip");
            httpGet.setHeader("User-Agent", "WeatherApp/1.0");
            
            System.out.println("请求头设置完成，开始发送请求...");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                System.out.println("API响应状态码: " + statusCode);
                System.out.println("API响应内容长度: " + (responseBody != null ? responseBody.length() : 0));
                System.out.println("API响应内容: " + (responseBody != null && responseBody.length() > 200 ? 
                    responseBody.substring(0, 200) + "..." : responseBody));
                
                // 检查响应是否为空
                if (responseBody == null || responseBody.trim().isEmpty()) {
                    System.err.println("天气API返回空响应");
                    return new ArrayList<>();
                }
                
                // 解析JSON响应
                WeatherResponse weatherResponse = null;
                try {
                    weatherResponse = JSON.parseObject(responseBody, WeatherResponse.class);
                    System.out.println("JSON解析成功");
                } catch (Exception e) {
                    System.err.println("天气JSON解析失败: " + e.getMessage());
                    System.err.println("响应内容: " + responseBody);
                    return new ArrayList<>();
                }
                
                // 检查解析结果
                if (weatherResponse == null) {
                    System.err.println("天气响应解析结果为null");
                    return new ArrayList<>();
                }
                
                System.out.println("API响应码: " + weatherResponse.getCode());
                System.out.println("天气数据条数: " + (weatherResponse.getDaily() != null ? weatherResponse.getDaily().size() : 0));
                
                if ("200".equals(weatherResponse.getCode()) && weatherResponse.getDaily() != null) {
                    System.out.println("天气数据获取成功，返回 " + weatherResponse.getDaily().size() + " 天预报");
                    return weatherResponse.getDaily();
                } else {
                    System.err.println("天气API请求失败，响应码: " + weatherResponse.getCode());
                    return new ArrayList<>();
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                System.err.println("HTTP响应解析异常: " + e.getMessage());
                return new ArrayList<>();
            }
            
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            System.err.println("天气API请求异常: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 获取API Host
     */
    public String getApiHost() {
        return "nv3md8tnqq.re.qweatherapi.com";
    }
    
    /**
     * 获取JWT Token
     */
    public String getJwtToken() {
        try {
            return TokenUtil.getToken();
        } catch (Exception e) {
            return null;
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
            // 静默处理关闭异常
        }
    }
}