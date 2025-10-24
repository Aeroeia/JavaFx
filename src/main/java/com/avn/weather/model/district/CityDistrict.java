package com.avn.weather.model.district;

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

        public District() {}

        public District(String districtName, String districtCode) {
            this.districtName = districtName;
            this.districtCode = districtCode;
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