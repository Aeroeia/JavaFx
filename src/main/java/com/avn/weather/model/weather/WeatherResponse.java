package com.avn.weather.model.weather;

import java.util.List;

/**
 * 天气API响应模型
 */
public class WeatherResponse {
    private String code;                    // 状态码
    private String updateTime;              // 当前API的最近更新时间
    private String fxLink;                  // 当前数据的响应式页面
    private List<DailyWeather> daily;       // 天气预报数据
    private ReferInfo refer;                // 数据来源信息

    // 构造函数
    public WeatherResponse() {}

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public String getFxLink() { return fxLink; }
    public void setFxLink(String fxLink) { this.fxLink = fxLink; }

    public List<DailyWeather> getDaily() { return daily; }
    public void setDaily(List<DailyWeather> daily) { this.daily = daily; }

    public ReferInfo getRefer() { return refer; }
    public void setRefer(ReferInfo refer) { this.refer = refer; }

    /**
     * 数据来源信息内部类
     */
    public static class ReferInfo {
        private List<String> sources;   // 原始数据来源
        private List<String> license;   // 数据许可或版权声明

        public List<String> getSources() { return sources; }
        public void setSources(List<String> sources) { this.sources = sources; }

        public List<String> getLicense() { return license; }
        public void setLicense(List<String> license) { this.license = license; }
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", dailyCount=" + (daily != null ? daily.size() : 0) +
                '}';
    }
}