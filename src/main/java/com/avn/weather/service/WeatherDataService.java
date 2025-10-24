package com.avn.weather.service;

import com.avn.weather.model.quality.AirQuality;
import com.avn.weather.model.district.CityDistrict;
import com.avn.weather.model.weather.WeatherInfo;
import com.avn.weather.model.weather.DailyWeather;
import com.avn.weather.model.geo.LocationInfo;
import com.avn.weather.util.WeatherDataConverter;

import java.time.LocalDate;
import java.util.*;

/**
 * 天气数据服务类 - 使用真实API获取天气数据
 */
public class WeatherDataService {
    
    private final GeoLocationService geoLocationService;
    private final WeatherApiService weatherApiService;
    private final AirQualityApiService airQualityApiService;
    
    // 缓存城市ID映射，避免重复查询
    private final Map<String, String> cityIdCache = new HashMap<>();
    
    public WeatherDataService() {
        this.geoLocationService = new GeoLocationService();
        this.weatherApiService = new WeatherApiService();
        this.airQualityApiService = new AirQualityApiService();
        
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
                // 查询城市的区域信息
                List<LocationInfo> locations = geoLocationService.searchLocation(cityName);
                if (!locations.isEmpty()) {
                    // 获取主城区
                    LocationInfo mainCity = locations.get(0);
                    
                    // 查询该城市的所有区域
                    List<LocationInfo> districts = geoLocationService.searchLocation(cityName, cityName);
                    
                    List<CityDistrict.District> districtList = new ArrayList<>();
                    for (LocationInfo district : districts) {
                        districtList.add(new CityDistrict.District(district.getName(), district.getId()));
                        // 缓存区域ID
                        cityIdCache.put(district.getName(), district.getId());
                    }
                    
                    cities.add(new CityDistrict(cityName, mainCity.getId(), districtList));
                    // 缓存主城市ID
                    cityIdCache.put(cityName, mainCity.getId());
                }
            } catch (Exception e) {
                System.err.println("查询城市 " + cityName + " 失败: " + e.getMessage());
                // 如果API查询失败，跳过该城市
            }
        }
        
        return cities;
    }
    
    /**
     * 创建备用城市数据（当API不可用时使用）
     */

    

    
    /**
     * 获取天气预报数据
     * 使用真实API获取天气预报
     */
    public List<WeatherInfo> getWeatherForecast(String cityCode) {
        try {
            // 获取城市ID
            String locationId = getCityLocationId(cityCode);
            if (locationId == null) {
                System.err.println("无法获取城市ID: " + cityCode);
                return new ArrayList<>();
            }
            
            // 调用天气API获取7天预报
            List<DailyWeather> dailyWeatherList = weatherApiService.getWeatherForecast(locationId);
            
            if (dailyWeatherList != null && !dailyWeatherList.isEmpty()) {
                // 转换为UI使用的WeatherInfo格式
                return WeatherDataConverter.convertToWeatherInfoList(dailyWeatherList);
            } else {
                System.err.println("API返回空数据");
                return new ArrayList<>();
            }
            
        } catch (Exception e) {
            System.err.println("获取天气预报失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取城市的LocationID
     */
    private String getCityLocationId(String cityCode) {
        // 首先从缓存中查找
        for (Map.Entry<String, String> entry : cityIdCache.entrySet()) {
            if (entry.getKey().equals(cityCode) || entry.getValue().equals(cityCode)) {
                return entry.getValue();
            }
        }
        
        // 如果缓存中没有，尝试通过API查询
        try {
            List<LocationInfo> locations = geoLocationService.searchLocation(cityCode);
            if (!locations.isEmpty()) {
                String locationId = locations.get(0).getId();
                cityIdCache.put(cityCode, locationId);
                return locationId;
            }
        } catch (Exception e) {
            System.err.println("查询城市ID失败: " + e.getMessage());
        }
        
        // 如果都失败了，返回默认的北京ID
        return "101010100";
    }
    
    /**
     * 获取天气图标路径
     */
    private String getWeatherIcon(String weatherType) {
        switch (weatherType) {
            case "晴":
                return "/icons/sunny.png";
            case "多云":
                return "/icons/cloudy.png";
            case "阴":
                return "/icons/overcast.png";
            case "小雨":
                return "/icons/light_rain.png";
            case "中雨":
                return "/icons/moderate_rain.png";
            case "大雨":
                return "/icons/heavy_rain.png";
            case "雷阵雨":
                return "/icons/thunderstorm.png";
            case "雪":
                return "/icons/snow.png";
            default:
                return "/icons/default.png";
        }
    }
    
    /**
     * 获取备用天气预报数据（当API不可用时使用）
     */

    
    /**
     * 获取空气质量数据
     * 使用真实API获取空气质量信息
     */
    public AirQuality getAirQuality(String cityCode) {
        try {
            // 获取城市ID
            String locationId = getCityLocationId(cityCode);
            if (locationId == null) {
                System.err.println("无法获取城市ID: " + cityCode);
                return null;
            }
            
            // 调用空气质量API
            AirQuality airQuality = airQualityApiService.getAirQuality(locationId);
            
            if (airQuality != null) {
                return airQuality;
            } else {
                System.err.println("API返回空数据");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("获取空气质量失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    

    

}