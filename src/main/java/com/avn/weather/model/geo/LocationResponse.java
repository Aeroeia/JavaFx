package com.avn.weather.model.geo;

import java.util.List;

/**
 * 地理位置查询API响应模型
 */
public class LocationResponse {
    private String code;                    // 状态码
    private List<LocationInfo> location;    // 地理位置信息列表
    private ReferInfo refer;                // 数据来源信息

    // 构造函数
    public LocationResponse() {}

    // Getter和Setter方法
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<LocationInfo> getLocation() { return location; }
    public void setLocation(List<LocationInfo> location) { this.location = location; }

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
        return "LocationResponse{" +
                "code='" + code + '\'' +
                ", locationCount=" + (location != null ? location.size() : 0) +
                '}';
    }
}