package com.avn.weather.ui;

import com.avn.weather.model.weather.WeatherInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 天气信息卡片组件
 */
public class WeatherCard extends VBox {
    
    private Label dateLabel;
    private Label dayLabel;
    private Label iconLabel;
    private Label conditionLabel;
    private Label temperatureLabel;
    private Label windLabel;
    
    public WeatherCard() {
        initializeComponents();
        setupLayout();
        applyStyles();
    }
    
    private void initializeComponents() {
        dateLabel = new Label();
        dayLabel = new Label();
        iconLabel = new Label();
        conditionLabel = new Label();
        temperatureLabel = new Label();
        windLabel = new Label();
    }
    
    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(16, 12, 16, 12));
        setSpacing(8);
        
        getChildren().addAll(
            dateLabel,
            dayLabel,
            iconLabel,
            conditionLabel,
            temperatureLabel,
            windLabel
        );
    }
    
    private void applyStyles() {
        // 卡片整体样式 - 与AirQualityCard保持一致的现代化设计
        setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8fafc);" +
            "-fx-border-color: #e1e7ef;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 3);"
        );
        
        // 统一字体系列
        String fontFamily = "'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif";
        
        dateLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: normal;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        dayLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1a202c;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        iconLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: normal;" +
            "-fx-text-fill: #3b82f6;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        conditionLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        temperatureLabel.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1d4ed8;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        windLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: normal;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-family: " + fontFamily + ";"
        );
        
        // 调整卡片尺寸以适应新的设计
        setPrefWidth(145);
        setPrefHeight(185);
    }
    
    public void updateWeather(WeatherInfo weather) {
        if (weather != null) {
            dateLabel.setText(weather.getDate().toString().substring(5)); // MM-dd
            dayLabel.setText(weather.getDayOfWeek());
            iconLabel.setText(weather.getWeatherIcon());
            conditionLabel.setText(weather.getWeatherCondition());
            temperatureLabel.setText(weather.getHighTemperature() + "°/" + weather.getLowTemperature() + "°");
            windLabel.setText(weather.getWindDirection() + " " + weather.getWindLevel());
        }
    }
}