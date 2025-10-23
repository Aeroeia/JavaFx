package com.avn.weather.model;

/**
 * 每日天气预报数据模型
 */
public class DailyWeather {
    private String fxDate;          // 预报日期
    private String sunrise;         // 日出时间
    private String sunset;          // 日落时间
    private String moonrise;        // 月升时间
    private String moonset;         // 月落时间
    private String moonPhase;       // 月相名称
    private String moonPhaseIcon;   // 月相图标代码
    private String tempMax;         // 最高温度
    private String tempMin;         // 最低温度
    private String iconDay;         // 白天天气图标代码
    private String textDay;         // 白天天气描述
    private String iconNight;       // 夜间天气图标代码
    private String textNight;       // 夜间天气描述
    private String wind360Day;      // 白天风向360角度
    private String windDirDay;      // 白天风向
    private String windScaleDay;    // 白天风力等级
    private String windSpeedDay;    // 白天风速
    private String wind360Night;    // 夜间风向360角度
    private String windDirNight;    // 夜间风向
    private String windScaleNight;  // 夜间风力等级
    private String windSpeedNight;  // 夜间风速
    private String humidity;        // 相对湿度
    private String precip;          // 降水量
    private String pressure;        // 大气压强
    private String vis;             // 能见度
    private String cloud;           // 云量
    private String uvIndex;         // 紫外线强度指数

    // 构造函数
    public DailyWeather() {}

    // Getter和Setter方法
    public String getFxDate() { return fxDate; }
    public void setFxDate(String fxDate) { this.fxDate = fxDate; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }

    public String getMoonrise() { return moonrise; }
    public void setMoonrise(String moonrise) { this.moonrise = moonrise; }

    public String getMoonset() { return moonset; }
    public void setMoonset(String moonset) { this.moonset = moonset; }

    public String getMoonPhase() { return moonPhase; }
    public void setMoonPhase(String moonPhase) { this.moonPhase = moonPhase; }

    public String getMoonPhaseIcon() { return moonPhaseIcon; }
    public void setMoonPhaseIcon(String moonPhaseIcon) { this.moonPhaseIcon = moonPhaseIcon; }

    public String getTempMax() { return tempMax; }
    public void setTempMax(String tempMax) { this.tempMax = tempMax; }

    public String getTempMin() { return tempMin; }
    public void setTempMin(String tempMin) { this.tempMin = tempMin; }

    public String getIconDay() { return iconDay; }
    public void setIconDay(String iconDay) { this.iconDay = iconDay; }

    public String getTextDay() { return textDay; }
    public void setTextDay(String textDay) { this.textDay = textDay; }

    public String getIconNight() { return iconNight; }
    public void setIconNight(String iconNight) { this.iconNight = iconNight; }

    public String getTextNight() { return textNight; }
    public void setTextNight(String textNight) { this.textNight = textNight; }

    public String getWind360Day() { return wind360Day; }
    public void setWind360Day(String wind360Day) { this.wind360Day = wind360Day; }

    public String getWindDirDay() { return windDirDay; }
    public void setWindDirDay(String windDirDay) { this.windDirDay = windDirDay; }

    public String getWindScaleDay() { return windScaleDay; }
    public void setWindScaleDay(String windScaleDay) { this.windScaleDay = windScaleDay; }

    public String getWindSpeedDay() { return windSpeedDay; }
    public void setWindSpeedDay(String windSpeedDay) { this.windSpeedDay = windSpeedDay; }

    public String getWind360Night() { return wind360Night; }
    public void setWind360Night(String wind360Night) { this.wind360Night = wind360Night; }

    public String getWindDirNight() { return windDirNight; }
    public void setWindDirNight(String windDirNight) { this.windDirNight = windDirNight; }

    public String getWindScaleNight() { return windScaleNight; }
    public void setWindScaleNight(String windScaleNight) { this.windScaleNight = windScaleNight; }

    public String getWindSpeedNight() { return windSpeedNight; }
    public void setWindSpeedNight(String windSpeedNight) { this.windSpeedNight = windSpeedNight; }

    public String getHumidity() { return humidity; }
    public void setHumidity(String humidity) { this.humidity = humidity; }

    public String getPrecip() { return precip; }
    public void setPrecip(String precip) { this.precip = precip; }

    public String getPressure() { return pressure; }
    public void setPressure(String pressure) { this.pressure = pressure; }

    public String getVis() { return vis; }
    public void setVis(String vis) { this.vis = vis; }

    public String getCloud() { return cloud; }
    public void setCloud(String cloud) { this.cloud = cloud; }

    public String getUvIndex() { return uvIndex; }
    public void setUvIndex(String uvIndex) { this.uvIndex = uvIndex; }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "fxDate='" + fxDate + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", textDay='" + textDay + '\'' +
                ", textNight='" + textNight + '\'' +
                '}';
    }
}