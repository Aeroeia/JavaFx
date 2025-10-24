package com.avn.weather.service;

import com.alibaba.fastjson.JSON;
import com.avn.weather.api.TokenUtil;
import com.avn.weather.model.geo.LocationInfo;
import com.avn.weather.model.geo.LocationResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * 地理位置查询服务
 * 调用和风天气地理位置查询API
 */
public class GeoLocationService {
    
    // API配置
    private static final String GEO_API_URL = "https://nv3md8tnqq.re.qweatherapi.com/geo/v2/city/lookup";
    
    private final CloseableHttpClient httpClient;
    
    public GeoLocationService() {
        this.httpClient = HttpClients.createDefault();
    }
    
    /**
     * 根据城市名称查询地理位置信息
     * @param location 城市名称，支持模糊搜索
     * @param adm 上级行政区划，可选
     * @param range 搜索范围，如"cn"表示中国
     * @param number 返回结果数量，默认10
     * @return 地理位置信息列表
     */
    public List<LocationInfo> searchLocation(String location, String adm, String range, Integer number) {
        try {
            // 获取token
            String token = TokenUtil.getToken();
            
            // 构建请求URL
            StringBuilder urlBuilder = new StringBuilder(GEO_API_URL);
            urlBuilder.append("?location=").append(URLEncoder.encode(location, StandardCharsets.UTF_8));
            
            if (adm != null && !adm.trim().isEmpty()) {
                urlBuilder.append("&adm=").append(URLEncoder.encode(adm, StandardCharsets.UTF_8));
            }
            
            if (range != null && !range.trim().isEmpty()) {
                urlBuilder.append("&range=").append(range);
            }
            
            if (number != null && number > 0) {
                urlBuilder.append("&number=").append(number);
            }
            
            // 创建HTTP请求
            HttpGet httpGet = new HttpGet(urlBuilder.toString());
            httpGet.setHeader("Authorization", "Bearer " + token);
            httpGet.setHeader("Accept-Encoding", "gzip");
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                // 解析JSON响应
                LocationResponse locationResponse = JSON.parseObject(responseBody, LocationResponse.class);
                
                if ("200".equals(locationResponse.getCode()) && locationResponse.getLocation() != null) {
                    return locationResponse.getLocation();
                } else {
                    System.err.println("地理位置查询失败，状态码: " + locationResponse.getCode());
                    return new ArrayList<>();
                }
            } catch (org.apache.hc.core5.http.ParseException e) {
                System.err.println("响应解析失败: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }
            
        } catch (IOException e) {
            System.err.println("地理位置查询请求失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            System.err.println("Token生成失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据城市名称查询地理位置信息（简化版本）
     * @param location 城市名称
     * @return 地理位置信息列表
     */
    public List<LocationInfo> searchLocation(String location) {
        return searchLocation(location, null, "cn", 10);
    }
    
    /**
     * 根据城市名称和上级行政区划查询地理位置信息
     * @param location 城市名称
     * @param adm 上级行政区划
     * @return 地理位置信息列表
     */
    public List<LocationInfo> searchLocation(String location, String adm) {
        return searchLocation(location, adm, "cn", 20);
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
    
    /**
     * 获取默认的中国主要城市列表
     * 这个方法提供一些常用城市的快速查询
     */
    public List<LocationInfo> getDefaultCities() {
        List<LocationInfo> cities = new ArrayList<>();
        
        // 只保留北京、上海、广州、成都四个主要城市
        String[] cityNames = {"北京", "上海", "广州", "成都"};
        
        for (String cityName : cityNames) {
            List<LocationInfo> results = searchLocation(cityName);
            if (!results.isEmpty()) {
                cities.add(results.get(0)); // 取第一个结果
            }
        }
        
        return cities;
    }
}