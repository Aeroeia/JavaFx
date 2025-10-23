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
                // 如果API查询失败，使用备用数据
                cities.add(createFallbackCityData(cityName));
            }
        }
        
        // 如果没有获取到任何城市数据，返回备用数据
        if (cities.isEmpty()) {
            return getFallbackCities();
        }
        
        return cities;
    }
    
    /**
     * 创建备用城市数据（当API不可用时使用）
     */
    private CityDistrict createFallbackCityData(String cityName) {
        List<CityDistrict.District> districts = new ArrayList<>();
        String cityId = cityIdCache.getOrDefault(cityName, "101010100");
        
        switch (cityName) {
            case "北京":
                districts.addAll(Arrays.asList(
                    new CityDistrict.District("东城区", "101010200"),
                    new CityDistrict.District("西城区", "101010300"),
                    new CityDistrict.District("朝阳区", "101010400"),
                    new CityDistrict.District("海淀区", "101010500")
                ));
                break;
            case "上海":
                districts.addAll(Arrays.asList(
                    new CityDistrict.District("黄浦区", "101020200"),
                    new CityDistrict.District("徐汇区", "101020300"),
                    new CityDistrict.District("长宁区", "101020400"),
                    new CityDistrict.District("静安区", "101020500")
                ));
                break;
            case "广州":
                districts.addAll(Arrays.asList(
                    new CityDistrict.District("越秀区", "101280102"),
                    new CityDistrict.District("海珠区", "101280103"),
                    new CityDistrict.District("荔湾区", "101280104"),
                    new CityDistrict.District("天河区", "101280105")
                ));
                break;
            case "成都":
                districts.addAll(Arrays.asList(
                    new CityDistrict.District("锦江区", "101270102"),
                    new CityDistrict.District("青羊区", "101270103"),
                    new CityDistrict.District("金牛区", "101270104"),
                    new CityDistrict.District("武侯区", "101270105")
                ));
                break;
            default:
                districts.add(new CityDistrict.District(cityName + "市区", cityId));
                break;
        }
        
        return new CityDistrict(cityName, cityId, districts);
    }
    
    /**
     * 获取备用城市列表（当API完全不可用时使用）
     */
    private List<CityDistrict> getFallbackCities() {
        List<CityDistrict> cities = new ArrayList<>();
        
        // 北京
        cities.add(new CityDistrict("北京", "101010100", Arrays.asList(
            new CityDistrict.District("东城区", "101010200"),
            new CityDistrict.District("西城区", "101010300"),
            new CityDistrict.District("朝阳区", "101010400"),
            new CityDistrict.District("海淀区", "101010500")
        )));
        
        // 上海
        cities.add(new CityDistrict("上海", "101020100", Arrays.asList(
            new CityDistrict.District("黄浦区", "101020200"),
            new CityDistrict.District("徐汇区", "101020300"),
            new CityDistrict.District("长宁区", "101020400"),
            new CityDistrict.District("静安区", "101020500")
        )));
        
        // 广州
        cities.add(new CityDistrict("广州", "101280101", Arrays.asList(
            new CityDistrict.District("越秀区", "101280102"),
            new CityDistrict.District("海珠区", "101280103"),
            new CityDistrict.District("荔湾区", "101280104"),
            new CityDistrict.District("天河区", "101280105")
        )));
        
        // 成都
        cities.add(new CityDistrict("成都", "101270101", Arrays.asList(
            new CityDistrict.District("锦江区", "101270102"),
            new CityDistrict.District("青羊区", "101270103"),
            new CityDistrict.District("金牛区", "101270104"),
            new CityDistrict.District("武侯区", "101270105")
        )));
        
        return cities;
    }
    
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
                return getFallbackWeatherForecast();
            }
            
            // 调用天气API获取7天预报
            List<DailyWeather> dailyWeatherList = weatherApiService.getWeatherForecast(locationId);
            
            if (dailyWeatherList != null && !dailyWeatherList.isEmpty()) {
                // 转换为UI使用的WeatherInfo格式
                return WeatherDataConverter.convertToWeatherInfoList(dailyWeatherList);
            } else {
                System.err.println("API返回空数据，使用备用数据");
                return getFallbackWeatherForecast();
            }
            
        } catch (Exception e) {
            System.err.println("获取天气预报失败: " + e.getMessage());
            e.printStackTrace();
            return getFallbackWeatherForecast();
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
    private List<WeatherInfo> getFallbackWeatherForecast() {
        List<WeatherInfo> forecast = new ArrayList<>();
        
        // 模拟7天天气预报数据
        String[] weatherTypes = {"晴", "多云", "阴", "小雨", "中雨", "大雨", "雷阵雨", "雪"};
        String[] windDirections = {"北风", "南风", "东风", "西风", "东北风", "西北风", "东南风", "西南风"};
        String[] windLevels = {"1-2级", "3-4级", "5-6级", "7-8级"};
        
        java.util.Random random = new java.util.Random();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        
        for (int i = 0; i < 7; i++) {
            String dateName;
            if (i == 0) {
                dateName = "今天";
            } else if (i == 1) {
                dateName = "明天";
            } else if (i == 2) {
                dateName = "后天";
            } else {
                String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
                int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + i - 1) % 7;
                dateName = weekDays[dayOfWeek];
            }
            
            String weatherType = weatherTypes[random.nextInt(weatherTypes.length)];
            int highTemp = 15 + random.nextInt(20); // 15-35度
            int lowTemp = highTemp - 5 - random.nextInt(10); // 比最高温度低5-15度
            String windDirection = windDirections[random.nextInt(windDirections.length)];
            String windLevel = windLevels[random.nextInt(windLevels.length)];
            
            // 根据天气类型选择图标
            String iconPath = getWeatherIcon(weatherType);
            
            // 计算日期
            LocalDate currentDate = LocalDate.now().plusDays(i);
            
            WeatherInfo weatherInfo = new WeatherInfo(
                currentDate,
                dateName,
                weatherType,
                highTemp,
                lowTemp,
                windDirection,
                windLevel,
                iconPath
            );
            
            forecast.add(weatherInfo);
        }
        
        return forecast;
    }
    
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
                return getFallbackAirQuality();
            }
            
            // 调用空气质量API
            AirQuality airQuality = airQualityApiService.getAirQuality(locationId);
            
            if (airQuality != null) {
                return airQuality;
            } else {
                System.err.println("API返回空数据，使用备用数据");
                return getFallbackAirQuality();
            }
            
        } catch (Exception e) {
            System.err.println("获取空气质量失败: " + e.getMessage());
            e.printStackTrace();
            return getFallbackAirQuality();
        }
    }
    
    /**
     * 获取备用空气质量数据（当API不可用时使用）
     */
    private AirQuality getFallbackAirQuality() {
        // 模拟从多个数据源获取空气质量信息
        java.util.Random random = new java.util.Random();
        
        // 模拟AQI值 (0-500)
        int aqi = random.nextInt(300);
        
        // 根据AQI值确定空气质量等级
        String level;
        String description;
        String suggestion;
        
        if (aqi <= 50) {
            level = "优";
            description = "空气质量令人满意，基本无空气污染";
            suggestion = "各类人群可正常活动";
        } else if (aqi <= 100) {
            level = "良";
            description = "空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响";
            suggestion = "极少数异常敏感人群应减少户外活动";
        } else if (aqi <= 150) {
            level = "轻度污染";
            description = "易感人群症状有轻度加剧，健康人群出现刺激症状";
            suggestion = "儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼";
        } else if (aqi <= 200) {
            level = "中度污染";
            description = "进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响";
            suggestion = "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外运动";
        } else if (aqi <= 300) {
            level = "重度污染";
            description = "心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状";
            suggestion = "儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动";
        } else {
            level = "严重污染";
            description = "健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病";
            suggestion = "儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动";
        }
        
        // 模拟各项污染物数据
        double pm25 = aqi * 0.7 + random.nextDouble() * 20;
        double pm10 = pm25 * 1.5 + random.nextDouble() * 30;
        double so2 = random.nextDouble() * 100;
        double no2 = random.nextDouble() * 80;
        double co = random.nextDouble() * 5;
        double o3 = random.nextDouble() * 200;
        
        return new AirQuality(
            java.time.LocalDateTime.now(),
            aqi,
            pm25,
            pm10,
            so2,
            no2,
            co,
            o3,
            level,
            description,
            suggestion,
            "备用数据"
        );
    }
    

}