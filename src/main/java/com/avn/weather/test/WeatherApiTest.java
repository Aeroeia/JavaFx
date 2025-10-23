package com.avn.weather.test;

import com.avn.weather.model.geo.LocationInfo;
import com.avn.weather.model.weather.DailyWeather;
import com.avn.weather.service.GeoLocationService;
import com.avn.weather.service.WeatherApiService;

import java.util.List;

/**
 * 天气API测试类
 * 用于测试北京、上海、广州、成都等主要城市的天气API调用功能
 */
public class WeatherApiTest {
    
    private final GeoLocationService geoLocationService;
    private final WeatherApiService weatherApiService;
    
    public WeatherApiTest() {
        this.geoLocationService = new GeoLocationService();
        this.weatherApiService = new WeatherApiService();
    }
    
    /**
     * 测试主要城市的天气API调用
     */
    public void testMajorCities() {
        String[] cities = {"北京", "上海", "广州", "成都"};
        
        System.out.println("=== 开始测试主要城市天气API调用 ===\n");
        
        for (String cityName : cities) {
            System.out.println("正在测试城市: " + cityName);
            testCityWeather(cityName);
            System.out.println("----------------------------------------\n");
        }
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 测试单个城市的天气API调用
     */
    private void testCityWeather(String cityName) {
        try {
            // 1. 测试地理位置查询
            System.out.println("1. 查询地理位置信息...");
            List<LocationInfo> locations = geoLocationService.searchLocation(cityName);
            
            if (locations == null || locations.isEmpty()) {
                System.out.println("❌ 地理位置查询失败: 未找到城市 " + cityName);
                return;
            }
            
            LocationInfo location = locations.get(0);
            System.out.println("✅ 地理位置查询成功:");
            System.out.println("   城市名称: " + location.getName());
            System.out.println("   城市ID: " + location.getId());
            System.out.println("   经纬度: " + location.getLon() + ", " + location.getLat());
            System.out.println("   行政区划: " + location.getAdm1() + " " + location.getAdm2());
            
            // 2. 测试天气预报查询
            System.out.println("\n2. 查询天气预报信息...");
            List<DailyWeather> weatherList = weatherApiService.getWeatherForecast(location.getId());
            
            if (weatherList == null || weatherList.isEmpty()) {
                System.out.println("❌ 天气预报查询失败: 未获取到天气数据");
                return;
            }
            
            System.out.println("✅ 天气预报查询成功:");
            System.out.println("   获取到 " + weatherList.size() + " 天的天气预报");
            
            // 显示前3天的天气信息
            int displayCount = Math.min(3, weatherList.size());
            for (int i = 0; i < displayCount; i++) {
                DailyWeather weather = weatherList.get(i);
                System.out.println("   第" + (i + 1) + "天: " + weather.getFxDate() + 
                                 " | " + weather.getTextDay() + 
                                 " | " + weather.getTempMin() + "°C ~ " + weather.getTempMax() + "°C" +
                                 " | 风力: " + weather.getWindScaleDay());
            }
            
        } catch (Exception e) {
            System.out.println("❌ 测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试Token生成功能
     */
    public void testTokenGeneration() {
        System.out.println("=== 测试Token生成功能 ===");
        try {
            String token = com.avn.weather.api.TokenUtil.getToken();
            if (token != null && !token.trim().isEmpty()) {
                System.out.println("✅ Token生成成功");
                System.out.println("Token长度: " + token.length());
                System.out.println("Token前50字符: " + token.substring(0, Math.min(50, token.length())) + "...");
            } else {
                System.out.println("❌ Token生成失败: Token为空");
            }
        } catch (Exception e) {
            System.out.println("❌ Token生成异常: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * 主测试方法
     */
    public static void main(String[] args) {
        WeatherApiTest test = new WeatherApiTest();
        
        // 首先测试Token生成
        test.testTokenGeneration();
        
        // 然后测试主要城市的天气API调用
        test.testMajorCities();
        
        // 关闭资源
        test.cleanup();
    }
    
    /**
     * 清理资源
     */
    private void cleanup() {
        try {
            if (geoLocationService != null) {
                geoLocationService.close();
            }
            if (weatherApiService != null) {
                weatherApiService.close();
            }
        } catch (Exception e) {
            System.err.println("清理资源时发生异常: " + e.getMessage());
        }
    }
}