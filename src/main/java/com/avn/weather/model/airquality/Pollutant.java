package com.avn.weather.model.airquality;

import java.util.List;

/**
 * 污染物数据模型
 */
public class Pollutant {
    private String code;
    private String name;
    private String fullName;
    private Concentration concentration;
    private List<SubIndex> subIndexes;

    // 构造函数
    public Pollutant() {}

    public Pollutant(String code, String name, String fullName, Concentration concentration) {
        this.code = code;
        this.name = name;
        this.fullName = fullName;
        this.concentration = concentration;
    }

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Concentration getConcentration() { return concentration; }
    public void setConcentration(Concentration concentration) { this.concentration = concentration; }

    public List<SubIndex> getSubIndexes() { return subIndexes; }
    public void setSubIndexes(List<SubIndex> subIndexes) { this.subIndexes = subIndexes; }

    /**
     * 污染物浓度信息
     */
    public static class Concentration {
        private double value;
        private String unit;

        public Concentration() {}

        public Concentration(double value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        // Getter和Setter方法
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        /**
         * 获取格式化的浓度显示字符串
         */
        public String getDisplayValue() {
            if (value == (long) value) {
                return String.format("%.0f %s", value, unit);
            } else {
                return String.format("%.1f %s", value, unit);
            }
        }
    }

    /**
     * 污染物分指数信息
     */
    public static class SubIndex {
        private String code;
        private double aqi;
        private String aqiDisplay;

        public SubIndex() {}

        public SubIndex(String code, double aqi, String aqiDisplay) {
            this.code = code;
            this.aqi = aqi;
            this.aqiDisplay = aqiDisplay;
        }

        // Getter和Setter方法
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public double getAqi() { return aqi; }
        public void setAqi(double aqi) { this.aqi = aqi; }

        public String getAqiDisplay() { return aqiDisplay; }
        public void setAqiDisplay(String aqiDisplay) { this.aqiDisplay = aqiDisplay; }
    }
}