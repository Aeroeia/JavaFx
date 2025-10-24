package com.avn.weather.model.airquality;

import java.util.List;

/**
 * 空气质量数据模型
 */
public class AirQuality {
    private Metadata metadata;
    private List<AQIIndex> indexes;
    private List<Pollutant> pollutants;
    private List<Station> stations;

    // 构造函数
    public AirQuality() {}

    public AirQuality(Metadata metadata, List<AQIIndex> indexes, 
                     List<Pollutant> pollutants, List<Station> stations) {
        this.metadata = metadata;
        this.indexes = indexes;
        this.pollutants = pollutants;
        this.stations = stations;
    }

    // Getter和Setter方法
    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    public List<AQIIndex> getIndexes() { return indexes; }
    public void setIndexes(List<AQIIndex> indexes) { this.indexes = indexes; }

    public List<Pollutant> getPollutants() { return pollutants; }
    public void setPollutants(List<Pollutant> pollutants) { this.pollutants = pollutants; }

    public List<Station> getStations() { return stations; }
    public void setStations(List<Station> stations) { this.stations = stations; }

    /**
     * 获取主要的AQI指数（通常是第一个）
     */
    public AQIIndex getPrimaryIndex() {
        return indexes != null && !indexes.isEmpty() ? indexes.get(0) : null;
    }

    /**
     * 获取US EPA AQI指数
     */
    public AQIIndex getUSEPAIndex() {
        if (indexes != null) {
            return indexes.stream()
                    .filter(index -> "us-epa".equals(index.getCode()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 获取QAQI指数
     */
    public AQIIndex getQAQIIndex() {
        if (indexes != null) {
            return indexes.stream()
                    .filter(index -> "qaqi".equals(index.getCode()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 根据污染物代码获取污染物信息
     */
    public Pollutant getPollutantByCode(String code) {
        if (pollutants != null) {
            return pollutants.stream()
                    .filter(pollutant -> code.equals(pollutant.getCode()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 元数据信息
     */
    public static class Metadata {
        private String tag;

        public Metadata() {}

        public Metadata(String tag) {
            this.tag = tag;
        }

        // Getter和Setter方法
        public String getTag() { return tag; }
        public void setTag(String tag) { this.tag = tag; }
    }
}