package com.avn.weather;

import com.avn.weather.model.district.CityDistrict;
import com.avn.weather.model.weather.WeatherInfo;
import com.avn.weather.model.airquality.AirQuality;
import com.avn.weather.service.WeatherDataService;
import com.avn.weather.service.AirQualityService;
import com.avn.weather.ui.WeatherCard;
import com.avn.weather.ui.AirQualityCard;
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
    private AirQualityService airQualityService;
    private ComboBox<CityDistrict> cityComboBox;
    private ComboBox<CityDistrict.District> districtComboBox;
    private HBox weatherCardsContainer;
    private AirQualityCard airQualityCard;
    private Label titleLabel;
    
    @Override
    public void start(Stage stage) {
        weatherService = new WeatherDataService();
        
        // 初始化空气质量服务
        airQualityService = new AirQualityService(
            weatherService.getApiHost(),
            weatherService.getJwtToken()
        );
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        loadInitialData();
        
        Scene scene = new Scene(createMainLayout(), 1400, 800);
        
        stage.setTitle("天气和空气质量查询系统");
        stage.setScene(scene);
        stage.show();
    }
    
    private void initializeComponents() {
        titleLabel = new Label("天气预报查询系统");
        titleLabel.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1a202c;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;"
        );
        
        cityComboBox = new ComboBox<>();
        cityComboBox.setPromptText("选择城市");
        cityComboBox.setPrefWidth(160);
        
        districtComboBox = new ComboBox<>();
        districtComboBox.setPromptText("选择区域");
        districtComboBox.setPrefWidth(160);
        districtComboBox.setDisable(true);
        
        weatherCardsContainer = new HBox(12);
        weatherCardsContainer.setAlignment(Pos.CENTER);
        weatherCardsContainer.setPadding(new Insets(20));
        
        // 初始化空气质量卡片
        airQualityCard = new AirQualityCard();
    }
    
    private void setupLayout() {
        // 设置组件样式 - 统一现代化设计
        String comboBoxStyle = 
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px 12px;" +
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #e1e7ef;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;";
        
        cityComboBox.setStyle(comboBoxStyle);
        districtComboBox.setStyle(comboBoxStyle);
    }
    
    private void setupEventHandlers() {
        cityComboBox.setOnAction(e -> {
            CityDistrict selectedCity = cityComboBox.getValue();
            if (selectedCity != null) {
                System.out.println("=== 用户选择操作 ===");
                System.out.println("用户选择城市: " + selectedCity.getCityName());
                System.out.println("城市可用区域数量: " + selectedCity.getDistricts().size());
                
                districtComboBox.getItems().clear();
                districtComboBox.getItems().addAll(selectedCity.getDistricts());
                districtComboBox.setDisable(false);
                districtComboBox.setPromptText("选择区域");
                
                // 清空之前的数据显示
                weatherCardsContainer.getChildren().clear();
                System.out.println("已清空之前的天气数据显示");
            }
        });
        
        districtComboBox.setOnAction(e -> {
            CityDistrict.District selectedDistrict = districtComboBox.getValue();
            if (selectedDistrict != null) {
                System.out.println("=== 用户选择操作 ===");
                System.out.println("用户选择区域: " + selectedDistrict.getDistrictName());
                System.out.println("区域代码: " + selectedDistrict.getDistrictCode());
                System.out.println("区域坐标: " + selectedDistrict.getLatitude() + ", " + selectedDistrict.getLongitude());
                System.out.println("开始加载天气数据...");
                
                loadWeather(selectedDistrict.getDistrictCode());
            }
        });
    }
    
    private void loadInitialData() {
        List<CityDistrict> cities = weatherService.getSupportedCities();
        cityComboBox.getItems().addAll(cities);
    }
    
    private void loadWeather(String districtCode) {
        System.out.println("=== 加载天气数据 ===");
        System.out.println("区域代码: " + districtCode);
        
        // 加载天气预报
        List<WeatherInfo> forecast = weatherService.getWeatherForecast(districtCode);
        System.out.println("获取到天气预报数据条数: " + (forecast != null ? forecast.size() : 0));
        
        weatherCardsContainer.getChildren().clear();
        
        if (forecast != null) {
            for (WeatherInfo weather : forecast) {
                WeatherCard card = new WeatherCard();
                card.updateWeather(weather);
                weatherCardsContainer.getChildren().add(card);
            }
            System.out.println("天气卡片UI更新完成");
        }
        
        // 加载空气质量数据
        loadAirQuality(districtCode);
    }

    private void loadAirQuality(String districtCode) {
        System.out.println("=== 加载空气质量数据 ===");
        
        // 获取选中区域的经纬度
        CityDistrict.District selectedDistrict = districtComboBox.getValue();
        if (selectedDistrict != null) {
            double latitude = selectedDistrict.getLatitude();
            double longitude = selectedDistrict.getLongitude();
            
            System.out.println("空气质量查询坐标: " + latitude + ", " + longitude);
            
            // 异步加载空气质量数据
            airQualityService.getCurrentAirQuality(latitude, longitude)
                .thenAccept(airQuality -> {
                    if (airQuality != null) {
                        System.out.println("空气质量数据获取成功，开始更新UI");
                        // 在JavaFX应用线程中更新UI
                        javafx.application.Platform.runLater(() -> {
                            airQualityCard.setAirQuality(airQuality);
                            System.out.println("空气质量UI更新完成");
                        });
                    } else {
                        System.err.println("空气质量数据获取失败");
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("空气质量数据加载异常: " + throwable.getMessage());
                    return null;
                });
        } else {
            System.err.println("无法获取选中区域信息，跳过空气质量数据加载");
        }
    }

    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8fafc;");
        
        // 顶部标题和选择器
        VBox topContainer = new VBox(18);
        topContainer.setPadding(new Insets(24));
        topContainer.setAlignment(Pos.CENTER);
        
        HBox selectorBox = new HBox(18);
        selectorBox.setAlignment(Pos.CENTER);
        
        // 统一标签样式
        String labelStyle = 
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;";
        
        Label cityLabel = new Label("城市:");
        cityLabel.setStyle(labelStyle);
        
        Label districtLabel = new Label("区域:");
        districtLabel.setStyle(labelStyle);
        
        selectorBox.getChildren().addAll(cityLabel, cityComboBox, districtLabel, districtComboBox);
        topContainer.getChildren().addAll(titleLabel, selectorBox);
        
        root.setTop(topContainer);
        
        // 中间内容区域
        VBox centerContainer = new VBox(24);
        centerContainer.setPadding(new Insets(0, 24, 24, 24));
        
        // 统一标题样式
        String sectionTitleStyle = 
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;";
        
        // 天气预报标题
        Label weatherTitle = new Label("7天天气预报");
        weatherTitle.setStyle(sectionTitleStyle);
        
        // 天气卡片容器
        weatherCardsContainer.setAlignment(Pos.CENTER);
        weatherCardsContainer.setSpacing(12);
        
        // 空气质量标题
        Label airQualityTitle = new Label("实时空气质量");
        airQualityTitle.setStyle(sectionTitleStyle);
        
        // 空气质量卡片容器
        HBox airQualityContainer = new HBox();
        airQualityContainer.setAlignment(Pos.CENTER);
        airQualityContainer.getChildren().add(airQualityCard);
        
        centerContainer.getChildren().addAll(
            weatherTitle,
            weatherCardsContainer,
            airQualityTitle,
            airQualityContainer
        );
        
        // 创建滚动面板包装中间内容区域
        ScrollPane scrollPane = new ScrollPane(centerContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;"
        );
        
        // 设置滚动面板的内容背景透明
        scrollPane.setContent(centerContainer);
        
        root.setCenter(scrollPane);
        
        return root;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
