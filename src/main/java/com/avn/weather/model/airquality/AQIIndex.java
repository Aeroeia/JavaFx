package com.avn.weather.model.airquality;

/**
 * AQI指数数据模型
 */
public class AQIIndex {
    private String code;
    private String name;
    private double aqi;
    private String aqiDisplay;
    private String level;
    private String category;
    private AQIColor color;
    private PrimaryPollutant primaryPollutant;
    private HealthAdvice health;

    // 构造函数
    public AQIIndex() {}

    public AQIIndex(String code, String name, double aqi, String aqiDisplay, 
                   String level, String category, AQIColor color) {
        this.code = code;
        this.name = name;
        this.aqi = aqi;
        this.aqiDisplay = aqiDisplay;
        this.level = level;
        this.category = category;
        this.color = color;
    }

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getAqi() { return aqi; }
    public void setAqi(double aqi) { this.aqi = aqi; }

    public String getAqiDisplay() { return aqiDisplay; }
    public void setAqiDisplay(String aqiDisplay) { this.aqiDisplay = aqiDisplay; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public AQIColor getColor() { return color; }
    public void setColor(AQIColor color) { this.color = color; }

    public PrimaryPollutant getPrimaryPollutant() { return primaryPollutant; }
    public void setPrimaryPollutant(PrimaryPollutant primaryPollutant) { this.primaryPollutant = primaryPollutant; }

    public HealthAdvice getHealth() { return health; }
    public void setHealth(HealthAdvice health) { this.health = health; }

    /**
     * AQI颜色信息
     */
    public static class AQIColor {
        private int red;
        private int green;
        private int blue;
        private double alpha;

        public AQIColor() {}

        public AQIColor(int red, int green, int blue, double alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        // Getter和Setter方法
        public int getRed() { return red; }
        public void setRed(int red) { this.red = red; }

        public int getGreen() { return green; }
        public void setGreen(int green) { this.green = green; }

        public int getBlue() { return blue; }
        public void setBlue(int blue) { this.blue = blue; }

        public double getAlpha() { return alpha; }
        public void setAlpha(double alpha) { this.alpha = alpha; }

        /**
         * 获取CSS颜色字符串
         */
        public String toCssColor() {
            return String.format("rgba(%d, %d, %d, %.2f)", red, green, blue, alpha);
        }
    }

    /**
     * 首要污染物信息
     */
    public static class PrimaryPollutant {
        private String code;
        private String name;
        private String fullName;

        public PrimaryPollutant() {}

        public PrimaryPollutant(String code, String name, String fullName) {
            this.code = code;
            this.name = name;
            this.fullName = fullName;
        }

        // Getter和Setter方法
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }

    /**
     * 健康建议信息
     */
    public static class HealthAdvice {
        private String effect;
        private Advice advice;

        public HealthAdvice() {}

        public HealthAdvice(String effect, Advice advice) {
            this.effect = effect;
            this.advice = advice;
        }

        // Getter和Setter方法
        public String getEffect() { return effect; }
        public void setEffect(String effect) { this.effect = effect; }

        public Advice getAdvice() { return advice; }
        public void setAdvice(Advice advice) { this.advice = advice; }

        public static class Advice {
            private String generalPopulation;
            private String sensitivePopulation;

            public Advice() {}

            public Advice(String generalPopulation, String sensitivePopulation) {
                this.generalPopulation = generalPopulation;
                this.sensitivePopulation = sensitivePopulation;
            }

            // Getter和Setter方法
            public String getGeneralPopulation() { return generalPopulation; }
            public void setGeneralPopulation(String generalPopulation) { this.generalPopulation = generalPopulation; }

            public String getSensitivePopulation() { return sensitivePopulation; }
            public void setSensitivePopulation(String sensitivePopulation) { this.sensitivePopulation = sensitivePopulation; }
        }
    }
}