package com.avn.weather.model.geo;

/**
 * 地理位置信息模型
 * 对应和风天气地理位置查询API的返回数据
 */
public class LocationInfo {
    private String name;        // 地区/城市名称
    private String id;          // 地区/城市ID
    private String lat;         // 地区/城市纬度
    private String lon;         // 地区/城市经度
    private String adm2;        // 地区/城市的上级行政区划名称
    private String adm1;        // 地区/城市所属一级行政区域
    private String country;     // 地区/城市所属国家名称
    private String tz;          // 地区/城市所在时区
    private String utcOffset;   // 地区/城市目前与UTC时间偏移的小时数
    private String isDst;       // 地区/城市是否当前处于夏令时
    private String type;        // 地区/城市的属性
    private String rank;        // 地区评分
    private String fxLink;      // 该地区的天气预报网页链接

    // 构造函数
    public LocationInfo() {}

    // Getter和Setter方法
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }

    public String getLon() { return lon; }
    public void setLon(String lon) { this.lon = lon; }

    public String getAdm2() { return adm2; }
    public void setAdm2(String adm2) { this.adm2 = adm2; }

    public String getAdm1() { return adm1; }
    public void setAdm1(String adm1) { this.adm1 = adm1; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTz() { return tz; }
    public void setTz(String tz) { this.tz = tz; }

    public String getUtcOffset() { return utcOffset; }
    public void setUtcOffset(String utcOffset) { this.utcOffset = utcOffset; }

    public String getIsDst() { return isDst; }
    public void setIsDst(String isDst) { this.isDst = isDst; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getFxLink() { return fxLink; }
    public void setFxLink(String fxLink) { this.fxLink = fxLink; }

    /**
     * 获取纬度（double类型）
     */
    public double getLatitude() {
        try {
            return lat != null ? Double.parseDouble(lat) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 获取经度（double类型）
     */
    public double getLongitude() {
        try {
            return lon != null ? Double.parseDouble(lon) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", adm1='" + adm1 + '\'' +
                ", adm2='" + adm2 + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}