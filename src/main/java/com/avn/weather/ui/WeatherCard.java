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
        setPadding(new Insets(10));
        setSpacing(5);
        
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
        setStyle("-fx-background-color: #58C2DC; " +
                "-fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8;");
        
        dateLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        dateLabel.setStyle("-fx-text-fill: #6c757d;");
        
        dayLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        dayLabel.setStyle("-fx-text-fill: #495057;");
        
        iconLabel.setFont(Font.font("System", FontWeight.NORMAL, 24));
        
        conditionLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        conditionLabel.setStyle("-fx-text-fill: #495057;");
        
        temperatureLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        temperatureLabel.setStyle("-fx-text-fill: #dc3545;");
        
        windLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
        windLabel.setStyle("-fx-text-fill: #6c757d;");
        
        setPrefWidth(120);
        setPrefHeight(160);
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