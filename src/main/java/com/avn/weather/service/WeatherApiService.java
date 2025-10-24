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
    private static final String API_HOST = "nv3md8tnqq.re.qweatherapi.com";
    private static final String WEATHER_API_URL = "https://" + API_HOST + "/v7/weather/7d";
    
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
            // 获取token
            String token = TokenUtil.getToken();
            
            // 构建请求URL
            String url = WEATHER_API_URL + "?location=" + locationId + "&key=" + token;
            
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("User-Agent", "WeatherApp/1.0");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                // 添加调试信息
                System.out.println("API响应状态码: " + response.getCode());
                System.out.println("API响应内容: " + responseBody);
                
                // 检查响应是否为空
                if (responseBody == null || responseBody.trim().isEmpty()) {
                    System.err.println("API返回空响应");
                    return new ArrayList<>();
                }
                
                // 解析JSON响应
                WeatherResponse weatherResponse = null;
                try {
                    weatherResponse = JSON.parseObject(responseBody, WeatherResponse.class);
                } catch (Exception e) {
                    System.err.println("JSON解析失败: " + e.getMessage());
                    System.err.println("响应内容: " + responseBody);
                    return new ArrayList<>();
                }
                
                // 检查解析结果
                if (weatherResponse == null) {
                    System.err.println("JSON解析结果为null");
                    return new ArrayList<>();
                }
                
                if ("200".equals(weatherResponse.getCode()) && weatherResponse.getDaily() != null) {
                    return weatherResponse.getDaily();
                } else {
                    System.err.println("天气预报查询失败，状态码: " + weatherResponse.getCode());
                    return new ArrayList<>();
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                System.err.println("响应解析失败: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }
            
        } catch (IOException e) {
            System.err.println("天气预报查询请求失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            System.err.println("Token生成失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据地理位置ID获取3天天气预报
     * @param locationId 地理位置ID
     * @return 天气预报数据列表
     */
    public List<DailyWeather> getWeatherForecast3d(String locationId) {
        try {
            // 获取token
            String token = TokenUtil.getToken();
            
            // 构建请求URL
            String url = "https://" + API_HOST + "/v7/weather/3d?location=" + locationId + "&key=" + token;
            
            // 创建HTTP请求
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("User-Agent", "WeatherApp/1.0");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                // 添加调试信息
                System.out.println("3天预报API响应状态码: " + response.getCode());
                System.out.println("3天预报API响应内容: " + responseBody);
                
                // 检查响应是否为空
                if (responseBody == null || responseBody.trim().isEmpty()) {
                    System.err.println("3天预报API返回空响应");
                    return new ArrayList<>();
                }
                
                // 解析JSON响应
                WeatherResponse weatherResponse = null;
                try {
                    weatherResponse = JSON.parseObject(responseBody, WeatherResponse.class);
                } catch (Exception e) {
                    System.err.println("3天预报JSON解析失败: " + e.getMessage());
                    System.err.println("响应内容: " + responseBody);
                    return new ArrayList<>();
                }
                
                // 检查解析结果
                if (weatherResponse == null) {
                    System.err.println("3天预报JSON解析结果为null");
                    return new ArrayList<>();
                }
                
                if ("200".equals(weatherResponse.getCode()) && weatherResponse.getDaily() != null) {
                    return weatherResponse.getDaily();
                } else {
                    System.err.println("3天天气预报查询失败，状态码: " + weatherResponse.getCode());
                    return new ArrayList<>();
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                System.err.println("响应解析失败: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }

        } catch (IOException e) {
            System.err.println("3天天气预报查询请求失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            System.err.println("Token生成失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
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