package com.avn.weather.model.airquality;

/**
 * 监测站数据模型
 */
public class Station {
    private String id;
    private String name;

    // 构造函数
    public Station() {}

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Station{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}