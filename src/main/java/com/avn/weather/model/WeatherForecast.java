package com.avn.weather.model;

import java.util.List;

/**
 * 天气预报响应数据模型
 */
public class WeatherForecast {
    private String code;            // 状态码
    private String updateTime;      // 最近更新时间
    private String fxLink;          // 响应式页面链接
    private List<DailyWeather> daily; // 每日天气预报列表
    private Refer refer;            // 数据来源信息

    // 构造函数
    public WeatherForecast() {}

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public String getFxLink() { return fxLink; }
    public void setFxLink(String fxLink) { this.fxLink = fxLink; }

    public List<DailyWeather> getDaily() { return daily; }
    public void setDaily(List<DailyWeather> daily) { this.daily = daily; }

    public Refer getRefer() { return refer; }
    public void setRefer(Refer refer) { this.refer = refer; }

    /**
     * 数据来源信息内部类
     */
    public static class Refer {
        private List<String> sources;   // 原始数据来源
        private List<String> license;   // 数据许可或版权声明

        public Refer() {}

        public List<String> getSources() { return sources; }
        public void setSources(List<String> sources) { this.sources = sources; }

        public List<String> getLicense() { return license; }
        public void setLicense(List<String> license) { this.license = license; }

        @Override
        public String toString() {
            return "Refer{" +
                    "sources=" + sources +
                    ", license=" + license +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", dailyCount=" + (daily != null ? daily.size() : 0) +
                '}';
    }
}