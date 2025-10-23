package com.avn.weather.model.quality;

import java.util.List;

/**
 * 空气质量API响应模型
 */
public class AirQualityResponse {
    private String code;                    // 状态码
    private String updateTime;              // 当前API的最近更新时间
    private String fxLink;                  // 当前数据的响应式页面
    private AirQualityNow now;              // 当前空气质量数据
    private ReferInfo refer;                // 数据来源信息

    // 构造函数
    public AirQualityResponse() {}

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public String getFxLink() { return fxLink; }
    public void setFxLink(String fxLink) { this.fxLink = fxLink; }

    public AirQualityNow getNow() { return now; }
    public void setNow(AirQualityNow now) { this.now = now; }

    public ReferInfo getRefer() { return refer; }
    public void setRefer(ReferInfo refer) { this.refer = refer; }

    /**
     * 当前空气质量数据内部类
     */
    public static class AirQualityNow {
        private String pubTime;     // 空气质量数据发布时间
        private String aqi;         // 空气质量指数
        private String level;       // 空气质量指数等级
        private String category;    // 空气质量指数级别
        private String primary;     // 空气质量的主要污染物，优时返回NA
        private String pm10;        // PM10
        private String pm2p5;       // PM2.5
        private String no2;         // 二氧化氮
        private String so2;         // 二氧化硫
        private String co;          // 一氧化碳
        private String o3;          // 臭氧

        // Getter和Setter方法
        public String getPubTime() { return pubTime; }
        public void setPubTime(String pubTime) { this.pubTime = pubTime; }

        public String getAqi() { return aqi; }
        public void setAqi(String aqi) { this.aqi = aqi; }

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getPrimary() { return primary; }
        public void setPrimary(String primary) { this.primary = primary; }

        public String getPm10() { return pm10; }
        public void setPm10(String pm10) { this.pm10 = pm10; }

        public String getPm2p5() { return pm2p5; }
        public void setPm2p5(String pm2p5) { this.pm2p5 = pm2p5; }

        public String getNo2() { return no2; }
        public void setNo2(String no2) { this.no2 = no2; }

        public String getSo2() { return so2; }
        public void setSo2(String so2) { this.so2 = so2; }

        public String getCo() { return co; }
        public void setCo(String co) { this.co = co; }

        public String getO3() { return o3; }
        public void setO3(String o3) { this.o3 = o3; }
    }

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
        return "AirQualityResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", aqi=" + (now != null ? now.getAqi() : "null") +
                '}';
    }
}