package com.avn.weather.ui;

import com.avn.weather.model.airquality.AQIIndex;
import com.avn.weather.model.airquality.AirQuality;
import com.avn.weather.model.airquality.Pollutant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 空气质量卡片UI组件
 */
public class AirQualityCard extends VBox {
    private Label titleLabel;
    private Label aqiValueLabel;
    private Label aqiCategoryLabel;
    private Label primaryPollutantLabel;
    private VBox pollutantsContainer;
    private Label healthAdviceLabel;
    
    private AirQuality airQuality;

    public AirQualityCard() {
        initializeComponents();
        setupLayout();
        applyStyles();
    }

    public AirQualityCard(AirQuality airQuality) {
        this();
        setAirQuality(airQuality);
    }

    private void initializeComponents() {
        titleLabel = new Label("空气质量");
        aqiValueLabel = new Label("--");
        aqiCategoryLabel = new Label("--");
        primaryPollutantLabel = new Label("--");
        pollutantsContainer = new VBox();
        healthAdviceLabel = new Label("--");
    }

    private void setupLayout() {
        // 标题
        titleLabel.setAlignment(Pos.CENTER);
        
        // AQI值和类别容器
        VBox aqiContainer = new VBox(8);
        aqiContainer.setAlignment(Pos.CENTER);
        aqiContainer.setPadding(new Insets(10, 0, 15, 0));
        aqiContainer.getChildren().addAll(aqiValueLabel, aqiCategoryLabel);
        
        // 首要污染物容器
        VBox pollutantSection = new VBox(8);
        pollutantSection.setAlignment(Pos.CENTER_LEFT);
        pollutantSection.setPadding(new Insets(0, 0, 10, 0));
        
        Label pollutantTitle = new Label("首要污染物");
        pollutantTitle.getStyleClass().add("section-title");
        
        HBox pollutantContainer = new HBox(8);
        pollutantContainer.setAlignment(Pos.CENTER_LEFT);
        pollutantContainer.getChildren().add(primaryPollutantLabel);
        
        pollutantSection.getChildren().addAll(pollutantTitle, pollutantContainer);
        
        // 污染物详情区域
        VBox pollutantsSection = new VBox(8);
        pollutantsSection.setAlignment(Pos.CENTER_LEFT);
        pollutantsSection.setPadding(new Insets(0, 0, 10, 0));
        
        Label pollutantsTitle = new Label("污染物浓度");
        pollutantsTitle.getStyleClass().add("section-title");
        
        pollutantsSection.getChildren().addAll(pollutantsTitle, pollutantsContainer);
        
        // 健康建议区域
        VBox healthSection = new VBox(8);
        healthSection.setAlignment(Pos.CENTER_LEFT);
        
        Label healthTitle = new Label("健康建议");
        healthTitle.getStyleClass().add("section-title");
        
        healthSection.getChildren().addAll(healthTitle, healthAdviceLabel);
        
        // 设置主容器间距和对齐
        this.setSpacing(12);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(20, 18, 20, 18));
        
        // 添加所有组件
        this.getChildren().addAll(
            titleLabel,
            aqiContainer,
            pollutantSection,
            pollutantsSection,
            healthSection
        );
    }

    private void applyStyles() {
        // 卡片整体样式 - 现代化设计
        this.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8fafc);" +
            "-fx-border-color: #e1e7ef;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 3);"
        );
        
        // 设置推荐大小 - 更宽敞的布局
        this.setPrefWidth(320);
        this.setPrefHeight(380);
        this.setMaxWidth(320);
        
        // 标题样式 - 更现代的字体
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1a202c;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // AQI值样式 - 更突出的显示
        aqiValueLabel.setStyle(
            "-fx-font-size: 42px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2d3748;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // AQI类别样式 - 更清晰的层次
        aqiCategoryLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #4a5568;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
        
        // 首要污染物样式
        primaryPollutantLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #718096;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
        
        // 健康建议样式 - 更好的可读性
        healthAdviceLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #718096;" +
            "-fx-wrap-text: true;" +
            "-fx-line-spacing: 2px;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
        healthAdviceLabel.setMaxWidth(280);
        
        // 污染物容器样式
        pollutantsContainer.setSpacing(6);
        
        // 添加CSS类
        this.getStyleClass().add("air-quality-card");
        titleLabel.getStyleClass().add("card-title");
        aqiValueLabel.getStyleClass().add("aqi-value");
        aqiCategoryLabel.getStyleClass().add("aqi-category");
        
        // 为section-title类添加统一样式
        this.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .flatMap(vbox -> ((VBox) vbox).getChildren().stream())
            .filter(node -> node instanceof Label && ((Label) node).getStyleClass().contains("section-title"))
            .forEach(label -> ((Label) label).setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #2d3748;" +
                "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
            ));
    }

    /**
     * 设置空气质量数据
     */
    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
        updateDisplay();
    }

    /**
     * 更新显示内容
     */
    private void updateDisplay() {
        if (airQuality == null) {
            resetDisplay();
            return;
        }

        // 获取主要AQI指数（优先使用US EPA）
        AQIIndex primaryIndex = airQuality.getUSEPAIndex();
        if (primaryIndex == null) {
            primaryIndex = airQuality.getPrimaryIndex();
        }

        if (primaryIndex != null) {
            // 更新AQI值和类别
            aqiValueLabel.setText(primaryIndex.getAqiDisplay());
            aqiCategoryLabel.setText(primaryIndex.getCategory() != null ? 
                                   primaryIndex.getCategory() : "");
            
            // 设置AQI值的颜色
            if (primaryIndex.getColor() != null) {
                String color = primaryIndex.getColor().toCssColor();
                aqiValueLabel.setStyle(aqiValueLabel.getStyle() + 
                                     "-fx-text-fill: " + color + ";");
                aqiCategoryLabel.setStyle(aqiCategoryLabel.getStyle() + 
                                        "-fx-text-fill: " + color + ";");
            }
            
            // 更新首要污染物
            if (primaryIndex.getPrimaryPollutant() != null) {
                primaryPollutantLabel.setText(primaryIndex.getPrimaryPollutant().getName());
            }
            
            // 更新健康建议
            if (primaryIndex.getHealth() != null && 
                primaryIndex.getHealth().getAdvice() != null) {
                String advice = primaryIndex.getHealth().getAdvice().getGeneralPopulation();
                healthAdviceLabel.setText(advice != null ? advice : "");
            }
        }

        // 更新污染物详情
        updatePollutantsDisplay();
    }

    /**
     * 更新污染物显示
     */
    private void updatePollutantsDisplay() {
        pollutantsContainer.getChildren().clear();
        
        if (airQuality.getPollutants() != null) {
            for (Pollutant pollutant : airQuality.getPollutants()) {
                HBox pollutantRow = createPollutantRow(pollutant);
                pollutantsContainer.getChildren().add(pollutantRow);
            }
        }
    }

    /**
     * 创建污染物行
     */
    private HBox createPollutantRow(Pollutant pollutant) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        row.setStyle(
            "-fx-background-color: rgba(249, 250, 251, 0.8);" +
            "-fx-background-radius: 6px;" +
            "-fx-border-color: rgba(229, 231, 235, 0.6);" +
            "-fx-border-radius: 6px;" +
            "-fx-border-width: 1px;"
        );
        
        // 污染物名称
        Label nameLabel = new Label(pollutant.getName());
        nameLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-min-width: 50px;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;"
        );
        
        // 浓度值
        Label concentrationLabel = new Label();
        if (pollutant.getConcentration() != null) {
            concentrationLabel.setText(pollutant.getConcentration().getDisplayValue());
        }
        concentrationLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #4b5563;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;"
        );
        
        // AQI分指数（使用US EPA）
        Label aqiLabel = new Label();
        if (pollutant.getSubIndexes() != null) {
            pollutant.getSubIndexes().stream()
                    .filter(subIndex -> "us-epa".equals(subIndex.getCode()))
                    .findFirst()
                    .ifPresent(subIndex -> {
                        aqiLabel.setText("AQI: " + subIndex.getAqiDisplay());
                    });
        }
        aqiLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', 'Microsoft YaHei', sans-serif;"
        );
        
        row.getChildren().addAll(nameLabel, concentrationLabel, aqiLabel);
        return row;
    }

    /**
     * 重置显示
     */
    private void resetDisplay() {
        aqiValueLabel.setText("--");
        aqiCategoryLabel.setText("--");
        primaryPollutantLabel.setText("--");
        healthAdviceLabel.setText("--");
        pollutantsContainer.getChildren().clear();
        
        // 重置颜色
        aqiValueLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #0f172a;"
        );
        aqiCategoryLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #475569;"
        );
    }

    /**
     * 获取当前空气质量数据
     */
    public AirQuality getAirQuality() {
        return airQuality;
    }
}