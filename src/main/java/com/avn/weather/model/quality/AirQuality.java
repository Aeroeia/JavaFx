package com.avn.weather.model.quality;

import java.time.LocalDateTime;

/**
 * 空气质量数据模型
 */
public class AirQuality {
    private LocalDateTime updateTime;
    private int aqi;
    private double pm25;
    private double pm10;
    private double so2;
    private double no2;
    private double co;
    private double o3;
    private String qualityLevel;
    private String qualityDescription;
    private String healthAdvice;
    private String dataSource;

    public AirQuality() {}

    public AirQuality(LocalDateTime updateTime, int aqi, double pm25, double pm10, 
                     double so2, double no2, double co, double o3, 
                     String qualityLevel, String qualityDescription, 
                     String healthAdvice, String dataSource) {
        this.updateTime = updateTime;
        this.aqi = aqi;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.so2 = so2;
        this.no2 = no2;
        this.co = co;
        this.o3 = o3;
        this.qualityLevel = qualityLevel;
        this.qualityDescription = qualityDescription;
        this.healthAdvice = healthAdvice;
        this.dataSource = dataSource;
    }

    // Getters and Setters
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getSo2() {
        return so2;
    }

    public void setSo2(double so2) {
        this.so2 = so2;
    }

    public double getNo2() {
        return no2;
    }

    public void setNo2(double no2) {
        this.no2 = no2;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getO3() {
        return o3;
    }

    public void setO3(double o3) {
        this.o3 = o3;
    }

    public String getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public String getQualityDescription() {
        return qualityDescription;
    }

    public void setQualityDescription(String qualityDescription) {
        this.qualityDescription = qualityDescription;
    }

    public String getHealthAdvice() {
        return healthAdvice;
    }

    public void setHealthAdvice(String healthAdvice) {
        this.healthAdvice = healthAdvice;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return "AirQuality{" +
                "updateTime=" + updateTime +
                ", aqi=" + aqi +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                ", so2=" + so2 +
                ", no2=" + no2 +
                ", co=" + co +
                ", o3=" + o3 +
                ", qualityLevel='" + qualityLevel + '\'' +
                ", qualityDescription='" + qualityDescription + '\'' +
                ", healthAdvice='" + healthAdvice + '\'' +
                ", dataSource='" + dataSource + '\'' +
                '}';
    }
}