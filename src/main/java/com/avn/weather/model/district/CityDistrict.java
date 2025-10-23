package com.avn.weather.model.district;

import com.avn.weather.model.quality.AirQuality;
import com.avn.weather.model.weather.WeatherInfo;

import java.util.List;

/**
 * 城市区域数据模型
 */
public class CityDistrict {
    private String cityName;
    private String cityCode;
    private List<District> districts;

    public CityDistrict() {}

    public CityDistrict(String cityName, String cityCode, List<District> districts) {
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.districts = districts;
    }

    // Getters and Setters
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    /**
     * 区域信息内部类
     */
    public static class District {
        private String districtName;
        private String districtCode;
        private List<WeatherInfo> weatherForecast;
        private AirQuality airQuality;

        public District() {}

        public District(String districtName, String districtCode) {
            this.districtName = districtName;
            this.districtCode = districtCode;
        }

        public District(String districtName, String districtCode, 
                       List<WeatherInfo> weatherForecast, AirQuality airQuality) {
            this.districtName = districtName;
            this.districtCode = districtCode;
            this.weatherForecast = weatherForecast;
            this.airQuality = airQuality;
        }

        // Getters and Setters
        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getDistrictCode() {
            return districtCode;
        }

        public void setDistrictCode(String districtCode) {
            this.districtCode = districtCode;
        }

        public List<WeatherInfo> getWeatherForecast() {
            return weatherForecast;
        }

        public void setWeatherForecast(List<WeatherInfo> weatherForecast) {
            this.weatherForecast = weatherForecast;
        }

        public AirQuality getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQuality airQuality) {
            this.airQuality = airQuality;
        }

        @Override
        public String toString() {
            return districtName;
        }
    }

    @Override
    public String toString() {
        return cityName;
    }
}