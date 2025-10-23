package com.avn.weather.model.weather;

import java.time.LocalDate;

/**
 * 天气信息数据模型
 */
public class WeatherInfo {
    private LocalDate date;
    private String dayOfWeek;
    private String weatherCondition;
    private int highTemperature;
    private int lowTemperature;
    private String windDirection;
    private String windLevel;
    private String weatherIcon;

    public WeatherInfo() {}

    public WeatherInfo(LocalDate date, String dayOfWeek, String weatherCondition, 
                      int highTemperature, int lowTemperature, String windDirection, 
                      String windLevel, String weatherIcon) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.weatherCondition = weatherCondition;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.windDirection = windDirection;
        this.windLevel = windLevel;
        this.weatherIcon = weatherIcon;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public int getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(int highTemperature) {
        this.highTemperature = highTemperature;
    }

    public int getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(int lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(String windLevel) {
        this.windLevel = windLevel;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "date=" + date +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", highTemperature=" + highTemperature +
                ", lowTemperature=" + lowTemperature +
                ", windDirection='" + windDirection + '\'' +
                ", windLevel='" + windLevel + '\'' +
                ", weatherIcon='" + weatherIcon + '\'' +
                '}';
    }
}