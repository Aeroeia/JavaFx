package com.avn.weather.service;

import com.avn.weather.model.AirQuality;
import com.avn.weather.model.CityDistrict;
import com.avn.weather.model.WeatherInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 天气数据服务类 - 提供伪数据用于测试
 */
public class WeatherDataService {
    
    private static final Random random = new Random();
    
    /**
     * 获取支持的城市列表
     */
    public List<CityDistrict> getSupportedCities() {
        List<CityDistrict> cities = new ArrayList<>();
        
        // 北京
        cities.add(new CityDistrict("北京", "BJ", Arrays.asList(
            new CityDistrict.District("东城区", "BJ_DC"),
            new CityDistrict.District("西城区", "BJ_XC"),
            new CityDistrict.District("朝阳区", "BJ_CY"),
            new CityDistrict.District("丰台区", "BJ_FT"),
            new CityDistrict.District("石景山区", "BJ_SJS"),
            new CityDistrict.District("海淀区", "BJ_HD"),
            new CityDistrict.District("通州区", "BJ_TZ"),
            new CityDistrict.District("昌平区", "BJ_CP")
        )));
        
        // 上海
        cities.add(new CityDistrict("上海", "SH", Arrays.asList(
            new CityDistrict.District("黄浦区", "SH_HP"),
            new CityDistrict.District("徐汇区", "SH_XH"),
            new CityDistrict.District("长宁区", "SH_CN"),
            new CityDistrict.District("静安区", "SH_JA"),
            new CityDistrict.District("普陀区", "SH_PT"),
            new CityDistrict.District("虹口区", "SH_HK"),
            new CityDistrict.District("杨浦区", "SH_YP"),
            new CityDistrict.District("浦东新区", "SH_PD")
        )));
        
        // 广州
        cities.add(new CityDistrict("广州", "GZ", Arrays.asList(
            new CityDistrict.District("荔湾区", "GZ_LW"),
            new CityDistrict.District("越秀区", "GZ_YX"),
            new CityDistrict.District("海珠区", "GZ_HZ"),
            new CityDistrict.District("天河区", "GZ_TH"),
            new CityDistrict.District("白云区", "GZ_BY"),
            new CityDistrict.District("黄埔区", "GZ_HP"),
            new CityDistrict.District("番禺区", "GZ_PY"),
            new CityDistrict.District("花都区", "GZ_HD")
        )));
        
        // 成都
        cities.add(new CityDistrict("成都", "CD", Arrays.asList(
            new CityDistrict.District("锦江区", "CD_JJ"),
            new CityDistrict.District("青羊区", "CD_QY"),
            new CityDistrict.District("金牛区", "CD_JN"),
            new CityDistrict.District("武侯区", "CD_WH"),
            new CityDistrict.District("成华区", "CD_CH"),
            new CityDistrict.District("龙泉驿区", "CD_LQ"),
            new CityDistrict.District("青白江区", "CD_QBJ"),
            new CityDistrict.District("新都区", "CD_XD")
        )));
        
        return cities;
    }
    
    /**
     * 获取指定区域的天气预报
     */
    public List<WeatherInfo> getWeatherForecast(String districtCode) {
        List<WeatherInfo> forecast = new ArrayList<>();
        String[] weatherConditions = {"晴", "多云", "阴", "小雨", "中雨", "大雨", "雷阵雨", "雪"};
        String[] dayNames = {"今天", "明天", "星期日", "星期一", "星期二", "星期三", "星期四"};
        String[] windDirections = {"北风", "南风", "东风", "西风", "东北风", "西北风", "东南风", "西南风"};
        String[] windLevels = {"1-2级", "2-3级", "3-4级", "4-5级"};
        String[] icons = {"☀️", "⛅", "☁️", "🌧️", "⛈️", "❄️"};
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            String condition = weatherConditions[random.nextInt(weatherConditions.length)];
            int high = 20 + random.nextInt(15);
            int low = high - 5 - random.nextInt(8);
            
            WeatherInfo weather = new WeatherInfo(
                date,
                i < dayNames.length ? dayNames[i] : date.format(DateTimeFormatter.ofPattern("MM-dd")),
                condition,
                high,
                low,
                windDirections[random.nextInt(windDirections.length)],
                windLevels[random.nextInt(windLevels.length)],
                icons[random.nextInt(icons.length)]
            );
            
            forecast.add(weather);
        }
        
        return forecast;
    }
    
    /**
     * 获取指定区域的空气质量信息
     */
    public AirQuality getAirQuality(String districtCode) {
        // 模拟从多个数据源获取数据，取最低值
        List<AirQuality> sources = new ArrayList<>();
        
        // 数据源1
        sources.add(createRandomAirQuality("环保部门", 50, 150));
        
        // 数据源2  
        sources.add(createRandomAirQuality("气象局", 60, 180));
        
        // 数据源3
        sources.add(createRandomAirQuality("第三方监测", 40, 160));
        
        // 取最低AQI值的数据
        return sources.stream()
                .min((a1, a2) -> Integer.compare(a1.getAqi(), a2.getAqi()))
                .orElse(sources.get(0));
    }
    
    private AirQuality createRandomAirQuality(String source, int minAqi, int maxAqi) {
        int aqi = minAqi + random.nextInt(maxAqi - minAqi);
        double pm25 = 20 + random.nextDouble() * 80;
        double pm10 = pm25 + random.nextDouble() * 30;
        
        String level;
        String description;
        String advice;
        
        if (aqi <= 50) {
            level = "优";
            description = "空气质量令人满意，基本无空气污染";
            advice = "各类人群可多参加户外活动，多呼吸一下清新的空气。";
        } else if (aqi <= 100) {
            level = "良";
            description = "空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响";
            advice = "极少数异常敏感人群应减少户外活动";
        } else if (aqi <= 150) {
            level = "轻度污染";
            description = "易感人群症状有轻度加剧，健康人群出现刺激症状";
            advice = "儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼";
        } else if (aqi <= 200) {
            level = "中度污染";
            description = "进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响";
            advice = "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外运动";
        } else {
            level = "重度污染";
            description = "心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状";
            advice = "儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动";
        }
        
        return new AirQuality(
            LocalDateTime.now(),
            aqi,
            pm25,
            pm10,
            random.nextDouble() * 50,  // SO2
            random.nextDouble() * 80,  // NO2
            random.nextDouble() * 2,   // CO
            random.nextDouble() * 160, // O3
            level,
            description,
            advice,
            source
        );
    }
}