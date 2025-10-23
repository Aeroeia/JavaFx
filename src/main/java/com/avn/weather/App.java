package com.avn.weather;

import com.avn.weather.model.AirQuality;
import com.avn.weather.model.CityDistrict;
import com.avn.weather.model.WeatherInfo;
import com.avn.weather.service.WeatherDataService;
import com.avn.weather.ui.AirQualityPanel;
import com.avn.weather.ui.WeatherCard;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {
    
    private WeatherDataService weatherService;
    private ComboBox<CityDistrict> cityComboBox;
    private ComboBox<CityDistrict.District> districtComboBox;
    private HBox weatherCardsContainer;
    private AirQualityPanel airQualityPanel;
    private Label titleLabel;
    
    @Override
    public void start(Stage stage) {
        weatherService = new WeatherDataService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadInitialData();
        
        Scene scene = new Scene(createMainLayout(), 1000, 700);
        
        stage.setTitle("天气和空气质量查询系统");
        stage.setScene(scene);
        stage.show();
    }
    
    private void initializeComponents() {
        titleLabel = new Label("天气和空气质量查询系统");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        cityComboBox = new ComboBox<>();
        cityComboBox.setPromptText("选择城市");
        cityComboBox.setPrefWidth(150);
        
        districtComboBox = new ComboBox<>();
        districtComboBox.setPromptText("选择区域");
        districtComboBox.setPrefWidth(150);
        districtComboBox.setDisable(true);
        
        weatherCardsContainer = new HBox(10);
        weatherCardsContainer.setAlignment(Pos.CENTER);
        weatherCardsContainer.setPadding(new Insets(20));
        
        airQualityPanel = new AirQualityPanel();
    }
    
    private void setupLayout() {
        // 设置组件样式
        cityComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8;");
        districtComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8;");
    }
    
    private void setupEventHandlers() {
        cityComboBox.setOnAction(e -> {
            CityDistrict selectedCity = cityComboBox.getValue();
            if (selectedCity != null) {
                districtComboBox.getItems().clear();
                districtComboBox.getItems().addAll(selectedCity.getDistricts());
                districtComboBox.setDisable(false);
                districtComboBox.setPromptText("选择区域");
                
                // 清空之前的数据显示
                weatherCardsContainer.getChildren().clear();
                airQualityPanel.updateAirQuality(null);
            }
        });
        
        districtComboBox.setOnAction(e -> {
            CityDistrict.District selectedDistrict = districtComboBox.getValue();
            if (selectedDistrict != null) {
                loadWeatherAndAirQuality(selectedDistrict.getDistrictCode());
            }
        });
    }
    
    private void loadInitialData() {
        List<CityDistrict> cities = weatherService.getSupportedCities();
        cityComboBox.getItems().addAll(cities);
    }
    
    private void loadWeatherAndAirQuality(String districtCode) {
        // 加载天气预报
        List<WeatherInfo> forecast = weatherService.getWeatherForecast(districtCode);
        weatherCardsContainer.getChildren().clear();
        
        for (WeatherInfo weather : forecast) {
            WeatherCard card = new WeatherCard();
            card.updateWeather(weather);
            weatherCardsContainer.getChildren().add(card);
        }
        
        // 加载空气质量
        AirQuality airQuality = weatherService.getAirQuality(districtCode);
        airQualityPanel.updateAirQuality(airQuality);
    }
    
    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // 顶部标题和选择器
        VBox topContainer = new VBox(15);
        topContainer.setPadding(new Insets(20));
        topContainer.setAlignment(Pos.CENTER);
        
        HBox selectorBox = new HBox(15);
        selectorBox.setAlignment(Pos.CENTER);
        
        Label cityLabel = new Label("城市:");
        cityLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Label districtLabel = new Label("区域:");
        districtLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        selectorBox.getChildren().addAll(cityLabel, cityComboBox, districtLabel, districtComboBox);
        topContainer.getChildren().addAll(titleLabel, selectorBox);
        
        root.setTop(topContainer);
        
        // 中间内容区域
        VBox centerContainer = new VBox(20);
        centerContainer.setPadding(new Insets(0, 20, 20, 20));
        
        // 天气预报标题
        Label weatherTitle = new Label("7天天气预报");
        weatherTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        weatherTitle.setStyle("-fx-text-fill: #495057;");
        
        // 天气卡片滚动容器
        ScrollPane weatherScrollPane = new ScrollPane(weatherCardsContainer);
        weatherScrollPane.setFitToHeight(true);
        weatherScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        weatherScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        weatherScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        weatherScrollPane.setPrefHeight(180);
        
        // 空气质量标题
        Label airQualityTitle = new Label("实时空气质量");
        airQualityTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        airQualityTitle.setStyle("-fx-text-fill: #495057;");
        
        centerContainer.getChildren().addAll(
            weatherTitle,
            weatherScrollPane,
            airQualityTitle,
            airQualityPanel
        );
        
        root.setCenter(centerContainer);
        
        return root;
    }
    
    public static void main(String[] args) {
        launch();
    }
}
