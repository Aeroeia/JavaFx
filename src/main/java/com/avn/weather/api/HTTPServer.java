package com.avn.weather.api;

import com.alibaba.fastjson.JSON;
import com.avn.weather.model.weather.WeatherForecast;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * HTTP服务器类，用于调用天气预报API
 */
public class HTTPServer {
    private final String host = "nv3md8tnqq.re.qweatherapi.com";
    private final String baseUrl = "https://" + host;
    
    /**
     * 获取天气预报信息
     * 
     * @param days 预报天数，支持：3d, 7d, 10d, 15d, 30d
     * @param location 位置ID或经纬度坐标（格式：116.41,39.92）
     * @param lang 语言设置，可选
     * @param unit 单位设置，可选（m=公制，i=英制）
     * @return WeatherForecast 天气预报对象
     * @throws Exception 请求异常
     */
    public WeatherForecast getWeatherForecast(String days, String location, String lang, String unit) throws Exception {
        // 构建请求URL
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl).append("/v7/weather/").append(days);
        urlBuilder.append("?location=").append(URLEncoder.encode(location, StandardCharsets.UTF_8));
        
        if (lang != null && !lang.trim().isEmpty()) {
            urlBuilder.append("&lang=").append(URLEncoder.encode(lang, StandardCharsets.UTF_8));
        }
        
        if (unit != null && !unit.trim().isEmpty()) {
            urlBuilder.append("&unit=").append(URLEncoder.encode(unit, StandardCharsets.UTF_8));
        }
        
        String url = urlBuilder.toString();
        System.out.println("请求URL: " + url);
        
        // 获取JWT Token
        String token = TokenUtil.getToken();
        
        // 创建HTTP客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建GET请求
            HttpGet httpGet = new HttpGet(url);
            
            // 设置请求头
            httpGet.setHeader("Authorization", "Bearer " + token);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Accept-Encoding", "gzip");
            httpGet.setHeader("User-Agent", "WeatherApp/1.0");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getCode();
                System.out.println("响应状态码: " + statusCode);
                
                if (statusCode == 200) {
                    // 获取响应内容
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                        System.out.println("响应内容: " + responseBody);
                        
                        // 解析JSON响应为WeatherForecast对象
                        WeatherForecast forecast = JSON.parseObject(responseBody, WeatherForecast.class);
                        return forecast;
                    }
                } else {
                    // 处理错误响应
                    HttpEntity entity = response.getEntity();
                    String errorBody = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "无错误信息";
                    throw new RuntimeException("API请求失败，状态码: " + statusCode + ", 错误信息: " + errorBody);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("HTTP请求异常: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * 获取天气预报信息（简化版本，使用默认参数）
     * 
     * @param days 预报天数
     * @param location 位置
     * @return WeatherForecast 天气预报对象
     * @throws Exception 请求异常
     */
    public WeatherForecast getWeatherForecast(String days, String location) throws Exception {
        return getWeatherForecast(days, location, null, "m");
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        HTTPServer server = new HTTPServer();
        try {
            // 测试获取北京3天天气预报
            WeatherForecast forecast = server.getWeatherForecast("3d", "101010100");
            
            if (forecast != null) {
                System.out.println("天气预报获取成功:");
                System.out.println("状态码: " + forecast.getCode());
                System.out.println("更新时间: " + forecast.getUpdateTime());
                System.out.println("预报天数: " + (forecast.getDaily() != null ? forecast.getDaily().size() : 0));
                
                if (forecast.getDaily() != null && !forecast.getDaily().isEmpty()) {
                    System.out.println("第一天预报:");
                    System.out.println(forecast.getDaily().get(0));
                }
            } else {
                System.out.println("获取天气预报失败");
            }
            
        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
