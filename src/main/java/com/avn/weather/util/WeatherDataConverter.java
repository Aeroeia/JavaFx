package com.avn.weather.util;

import com.avn.weather.model.weather.DailyWeather;
import com.avn.weather.model.weather.WeatherInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 天气数据转换工具类
 * 将API返回的数据转换为UI使用的模型
 */
public class WeatherDataConverter {
    
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String[] DAY_NAMES = {"今天", "明天", "后天"};
    
    /**
     * 将API返回的DailyWeather列表转换为UI使用的WeatherInfo列表
     * @param dailyWeatherList API返回的天气数据
     * @return UI使用的天气信息列表
     */
    public static List<WeatherInfo> convertToWeatherInfoList(List<DailyWeather> dailyWeatherList) {
        List<WeatherInfo> weatherInfoList = new ArrayList<>();
        
        if (dailyWeatherList == null || dailyWeatherList.isEmpty()) {
            return weatherInfoList;
        }
        
        for (int i = 0; i < dailyWeatherList.size(); i++) {
            DailyWeather daily = dailyWeatherList.get(i);
            WeatherInfo weatherInfo = convertToWeatherInfo(daily, i);
            if (weatherInfo != null) {
                weatherInfoList.add(weatherInfo);
            }
        }
        
        return weatherInfoList;
    }
    
    /**
     * 将单个DailyWeather转换为WeatherInfo
     * @param daily API返回的天气数据
     * @param dayIndex 天数索引（0表示今天，1表示明天，以此类推）
     * @return UI使用的天气信息
     */
    private static WeatherInfo convertToWeatherInfo(DailyWeather daily, int dayIndex) {
        try {
            // 解析日期
            LocalDate date = LocalDate.parse(daily.getFxDate(), API_DATE_FORMAT);
            
            // 获取日期显示名称
            String dayName = getDayName(dayIndex, date);
            
            // 获取天气状况（优先使用白天天气）
            String condition = daily.getTextDay() != null ? daily.getTextDay() : daily.getTextNight();
            if (condition == null) {
                condition = "未知";
            }
            
            // 解析温度
            int highTemp = parseTemperature(daily.getTempMax());
            int lowTemp = parseTemperature(daily.getTempMin());
            
            // 获取风向和风力（优先使用白天数据）
            String windDirection = daily.getWindDirDay() != null ? daily.getWindDirDay() : daily.getWindDirNight();
            String windScale = daily.getWindScaleDay() != null ? daily.getWindScaleDay() : daily.getWindScaleNight();
            
            if (windDirection == null) windDirection = "无风";
            if (windScale == null) windScale = "0级";
            
            String windInfo = windDirection + " " + windScale;
            
            // 获取天气图标（简化处理）
            String icon = getWeatherIcon(condition);
            
            return new WeatherInfo(date, dayName, condition, highTemp, lowTemp, windInfo, windScale, icon);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取日期显示名称
     */
    private static String getDayName(int dayIndex, LocalDate date) {
        if (dayIndex < DAY_NAMES.length) {
            return DAY_NAMES[dayIndex];
        } else {
            // 超过预定义名称的使用星期几
            return getWeekdayName(date);
        }
    }
    
    /**
     * 获取星期几的中文名称
     */
    private static String getWeekdayName(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY: return "星期一";
            case TUESDAY: return "星期二";
            case WEDNESDAY: return "星期三";
            case THURSDAY: return "星期四";
            case FRIDAY: return "星期五";
            case SATURDAY: return "星期六";
            case SUNDAY: return "星期日";
            default: return date.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }
    
    /**
     * 安全解析温度字符串
     */
    private static int parseTemperature(String tempStr) {
        try {
            return tempStr != null ? Integer.parseInt(tempStr) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * 根据天气状况获取对应的图标
     */
    private static String getWeatherIcon(String condition) {
        if (condition == null) return "❓";
        
        condition = condition.toLowerCase();
        
        if (condition.contains("晴")) {
            return "☀️";
        } else if (condition.contains("多云")) {
            return "⛅";
        } else if (condition.contains("阴")) {
            return "☁️";
        } else if (condition.contains("雨")) {
            if (condition.contains("雷")) {
                return "⛈️";
            } else {
                return "🌧️";
            }
        } else if (condition.contains("雪")) {
            return "❄️";
        } else if (condition.contains("雾") || condition.contains("霾")) {
            return "🌫️";
        } else if (condition.contains("风")) {
            return "💨";
        } else {
            return "🌤️";
        }
    }
}