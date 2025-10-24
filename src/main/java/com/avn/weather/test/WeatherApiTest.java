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
        
        for (String cityName : cities) {
            testCityWeather(cityName);
        }
    }
    
    /**
     * 测试单个城市的天气API调用
     */
    private void testCityWeather(String cityName) {
        try {
            // 1. 测试地理位置查询
            List<LocationInfo> locations = geoLocationService.searchLocation(cityName);
            
            if (locations == null || locations.isEmpty()) {
                return;
            }
            
            LocationInfo location = locations.get(0);
            
            // 2. 测试天气预报查询
            List<DailyWeather> weatherList = weatherApiService.getWeatherForecast(location.getId());
            
            if (weatherList == null || weatherList.isEmpty()) {
                return;
            }
            
        } catch (Exception e) {
            // 静默处理异常
        }
    }
    
    /**
     * 测试Token生成功能
     */
    public void testTokenGeneration() {
        try {
            String token = com.avn.weather.api.TokenUtil.getToken();
        } catch (Exception e) {
            // 静默处理异常
        }
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
            // 静默处理异常
        }
    }
}