package com.avn.weather.service;

import com.avn.weather.model.district.CityDistrict;
import com.avn.weather.model.weather.WeatherInfo;
import com.avn.weather.model.weather.DailyWeather;
import com.avn.weather.model.geo.LocationInfo;
import com.avn.weather.util.WeatherDataConverter;

import java.util.*;

/**
 * 天气数据服务类 - 使用真实API获取天气数据
 */
public class WeatherDataService {
    
    private final GeoLocationService geoLocationService;
    private final WeatherApiService weatherApiService;
    
    // 缓存城市ID映射，避免重复查询
    private final Map<String, String> cityIdCache = new HashMap<>();
    
    public WeatherDataService() {
        this.geoLocationService = new GeoLocationService();
        this.weatherApiService = new WeatherApiService();
        
        // 初始化城市ID缓存
        initializeCityCache();
    }
    
    /**
     * 初始化城市ID缓存
     */
    private void initializeCityCache() {
        // 只保留四个主要城市的ID
        cityIdCache.put("北京", "101010100");
        cityIdCache.put("上海", "101020100");
        cityIdCache.put("广州", "101280101");
        cityIdCache.put("成都", "101270101");
    }
    
    /**
     * 获取支持的城市列表
     * 使用真实API查询城市数据
     */
    public List<CityDistrict> getSupportedCities() {
        List<CityDistrict> cities = new ArrayList<>();
        
        // 主要城市列表 - 只保留四个主要城市
        String[] majorCities = {"北京", "上海", "广州", "成都"};
        
        for (String cityName : majorCities) {
            try {
                // 直接查询该城市的所有区域
                List<LocationInfo> districts = geoLocationService.searchLocation(cityName, cityName);
                if (!districts.isEmpty()) {
                    List<CityDistrict.District> districtList = new ArrayList<>();
                    String cityId = null;
                    for (LocationInfo district : districts) {
                        if(cityId!=null){
                            districtList.add(new CityDistrict.District(
                                district.getName(), 
                                district.getId(),
                                district.getLatitude(),
                                district.getLongitude()
                            ));
                            // 缓存区域ID
                            cityIdCache.put(district.getName(), district.getId());
                        }
                        // 使用第一个区域的ID作为城市ID（实际上不会用到，但保持数据结构完整）
                        else {
                            cityId = district.getId();
                        }
                    }
                    
                    cities.add(new CityDistrict(cityName, cityId, districtList));
                }
            } catch (Exception e) {
                // 如果API查询失败，跳过该城市
            }
        }
        
        return cities;
    }
    
    
    /**
     * 获取天气预报数据
     * 使用真实API获取天气预报
     */
    public List<WeatherInfo> getWeatherForecast(String locationId) {
        try {
            // 直接使用传入的LocationID（从UI层传入的已经是有效的地区ID）
            if (locationId == null || locationId.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            // 调用天气API获取7天预报
            List<DailyWeather> dailyWeatherList = weatherApiService.getWeatherForecast(locationId);
            
            if (dailyWeatherList != null && !dailyWeatherList.isEmpty()) {
                // 转换为UI使用的WeatherInfo格式
                return WeatherDataConverter.convertToWeatherInfoList(dailyWeatherList);
            } else {
                return new ArrayList<>();
            }
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取API Host配置
     */
    public String getApiHost() {
        return weatherApiService.getApiHost();
    }
    
    /**
     * 获取JWT Token配置
     */
    public String getJwtToken() {
        return weatherApiService.getJwtToken();
    }
}