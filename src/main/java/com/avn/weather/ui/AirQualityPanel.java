package com.avn.weather.ui;

import com.avn.weather.model.airquality.AQIIndex;
import com.avn.weather.model.airquality.AirQuality;
import com.avn.weather.model.airquality.Pollutant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * 空气质量平铺面板组件 - 水平布局充分利用下方空间
 */
public class AirQualityPanel extends VBox {
    
    private AirQuality airQuality;
    
    // 主要AQI显示区域
    private VBox aqiMainSection;
    private Label aqiValueLabel;
    private Label aqiCategoryLabel;
    private Label primaryPollutantLabel;
    
    // 污染物指标网格
    private GridPane pollutantsGrid;
    
    // 健康建议区域
    private VBox healthSection;
    private Label healthAdviceLabel;

    public AirQualityPanel() {
        initializeComponents();
        setupLayout();
        applyStyles();
    }

    private void initializeComponents() {
        // 主要AQI显示组件
        aqiMainSection = new VBox(8);
        aqiValueLabel = new Label("--");
        aqiCategoryLabel = new Label("--");
        primaryPollutantLabel = new Label("首要污染物: --");
        
        // 污染物网格
        pollutantsGrid = new GridPane();
        
        // 健康建议组件
        healthSection = new VBox(8);
        healthAdviceLabel = new Label("暂无数据");
    }

    private void setupLayout() {
        this.setSpacing(20);
        this.setPadding(new Insets(24, 20, 24, 20));
        this.setAlignment(Pos.TOP_CENTER);
        
        // 创建水平主容器
        HBox mainContainer = new HBox(30);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPrefHeight(200);
        
        // 左侧：主要AQI信息
        setupAqiMainSection();
        
        // 中间：污染物指标网格
        setupPollutantsGrid();
        
        // 右侧：健康建议
        setupHealthSection();
        
        // 设置各部分的宽度比例
        HBox.setHgrow(aqiMainSection, Priority.NEVER);
        HBox.setHgrow(pollutantsGrid, Priority.ALWAYS);
        HBox.setHgrow(healthSection, Priority.NEVER);
        
        mainContainer.getChildren().addAll(aqiMainSection, pollutantsGrid, healthSection);
        
        this.getChildren().add(mainContainer);
    }
    
    private void setupAqiMainSection() {
        aqiMainSection.setAlignment(Pos.CENTER);
        aqiMainSection.setPrefWidth(180);
        aqiMainSection.setMaxWidth(180);
        aqiMainSection.setPadding(new Insets(20));
        
        // AQI值容器
        VBox aqiValueContainer = new VBox(5);
        aqiValueContainer.setAlignment(Pos.CENTER);
        aqiValueContainer.getChildren().addAll(aqiValueLabel, aqiCategoryLabel);
        
        // 首要污染物容器
        VBox pollutantContainer = new VBox(5);
        pollutantContainer.setAlignment(Pos.CENTER);
        pollutantContainer.getChildren().add(primaryPollutantLabel);
        
        aqiMainSection.getChildren().addAll(aqiValueContainer, pollutantContainer);
    }
    
    private void setupPollutantsGrid() {
        pollutantsGrid.setHgap(15);
        pollutantsGrid.setVgap(12);
        pollutantsGrid.setAlignment(Pos.CENTER);
        pollutantsGrid.setPadding(new Insets(20));
        
        // 设置网格约束，让内容居中分布
        for (int i = 0; i < 3; i++) {
            pollutantsGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
            pollutantsGrid.getColumnConstraints().get(i).setHgrow(Priority.ALWAYS);
            pollutantsGrid.getColumnConstraints().get(i).setFillWidth(true);
        }
    }
    
    private void setupHealthSection() {
        healthSection.setAlignment(Pos.TOP_LEFT);
        healthSection.setPrefWidth(220);
        healthSection.setMaxWidth(220);
        healthSection.setPadding(new Insets(20));
        
        Label healthTitle = new Label("健康建议");
        healthTitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2d3748;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        healthAdviceLabel.setWrapText(true);
        healthAdviceLabel.setMaxWidth(200);
        
        healthSection.getChildren().addAll(healthTitle, healthAdviceLabel);
    }

    private void applyStyles() {
        // 整体面板样式
        this.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8fafc);" +
            "-fx-border-color: #e1e7ef;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 4);"
        );
        
        // 设置面板尺寸
        this.setPrefHeight(240);
        this.setMaxHeight(240);
        
        // AQI主区域样式
        aqiMainSection.setStyle(
            "-fx-background-color: rgba(59, 130, 246, 0.08);" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: rgba(59, 130, 246, 0.2);" +
            "-fx-border-radius: 12px;" +
            "-fx-border-width: 1px;"
        );
        
        // AQI值样式
        aqiValueLabel.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2d3748;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // AQI类别样式
        aqiCategoryLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #4a5568;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // 首要污染物样式
        primaryPollutantLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #718096;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
        
        // 污染物网格样式
        pollutantsGrid.setStyle(
            "-fx-background-color: rgba(249, 250, 251, 0.6);" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: rgba(229, 231, 235, 0.8);" +
            "-fx-border-radius: 12px;" +
            "-fx-border-width: 1px;"
        );
        
        // 健康建议区域样式
        healthSection.setStyle(
            "-fx-background-color: rgba(16, 185, 129, 0.08);" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: rgba(16, 185, 129, 0.2);" +
            "-fx-border-radius: 12px;" +
            "-fx-border-width: 1px;"
        );
        
        // 健康建议文本样式
        healthAdviceLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #4a5568;" +
            "-fx-line-spacing: 3px;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
    }

    /**
     * 设置空气质量数据并更新显示
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
                aqiValueLabel.setStyle(aqiValueLabel.getStyle().replaceAll("-fx-text-fill: [^;]+;", "") + 
                                     "-fx-text-fill: " + color + ";");
                aqiCategoryLabel.setStyle(aqiCategoryLabel.getStyle().replaceAll("-fx-text-fill: [^;]+;", "") + 
                                        "-fx-text-fill: " + color + ";");
            }
            
            // 更新首要污染物
            if (primaryIndex.getPrimaryPollutant() != null) {
                primaryPollutantLabel.setText("首要污染物: " + primaryIndex.getPrimaryPollutant().getName());
            }
            
            // 更新健康建议
            if (primaryIndex.getHealth() != null && 
                primaryIndex.getHealth().getAdvice() != null) {
                String advice = primaryIndex.getHealth().getAdvice().getGeneralPopulation();
                healthAdviceLabel.setText(advice != null ? advice : "暂无健康建议");
            }
        }

        // 更新污染物网格显示
        updatePollutantsGrid();
    }

    /**
     * 更新污染物网格显示
     */
    private void updatePollutantsGrid() {
        pollutantsGrid.getChildren().clear();
        
        if (airQuality.getPollutants() != null) {
            int row = 0;
            int col = 0;
            
            for (Pollutant pollutant : airQuality.getPollutants()) {
                VBox pollutantCard = createPollutantCard(pollutant);
                pollutantsGrid.add(pollutantCard, col, row);
                
                col++;
                if (col >= 3) { // 每行3个
                    col = 0;
                    row++;
                }
            }
        }
    }

    /**
     * 创建污染物卡片
     */
    private VBox createPollutantCard(Pollutant pollutant) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16, 12, 16, 12));
        card.setPrefWidth(130);
        card.setMinHeight(100);
        
        // 获取AQI值用于颜色判断
        String aqiValue = "--";
        String aqiColor = "#3b82f6"; // 默认蓝色
        
        if (pollutant.getSubIndexes() != null) {
            pollutant.getSubIndexes().stream()
                    .filter(subIndex -> "us-epa".equals(subIndex.getCode()))
                    .findFirst()
                    .ifPresent(subIndex -> {
                        // 这里可以根据AQI值设置不同颜色
                        // 暂时使用默认逻辑，可以后续根据实际AQI值范围调整
                    });
        }
        
        // 根据污染物类型设置不同的背景色调
        String backgroundColor = getBackgroundColorForPollutant(pollutant.getName());
        String borderColor = getBorderColorForPollutant(pollutant.getName());
        
        card.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-radius: 10px;" +
            "-fx-border-width: 1.5px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 3);"
        );
        
        // 污染物名称
        Label nameLabel = new Label(pollutant.getName());
        nameLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // 浓度值
        Label concentrationLabel = new Label();
        if (pollutant.getConcentration() != null) {
            concentrationLabel.setText(pollutant.getConcentration().getDisplayValue());
        } else {
            concentrationLabel.setText("--");
        }
        concentrationLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + getTextColorForPollutant(pollutant.getName()) + ";" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        
        // AQI分指数（使用US EPA）
        Label aqiLabel = new Label();
        if (pollutant.getSubIndexes() != null) {
            pollutant.getSubIndexes().stream()
                    .filter(subIndex -> "us-epa".equals(subIndex.getCode()))
                    .findFirst()
                    .ifPresent(subIndex -> {
                        aqiLabel.setText("AQI " + subIndex.getAqiDisplay());
                    });
        }
        if (aqiLabel.getText().isEmpty()) {
            aqiLabel.setText("AQI --");
        }
        aqiLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'SF Pro Text', 'PingFang SC', sans-serif;"
        );
        
        card.getChildren().addAll(nameLabel, concentrationLabel, aqiLabel);
        return card;
    }
    
    /**
     * 根据污染物类型获取背景颜色
     */
    private String getBackgroundColorForPollutant(String pollutantName) {
        switch (pollutantName.toUpperCase()) {
            case "PM2.5":
                return "linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(248, 113, 113, 0.05))";
            case "PM10":
                return "linear-gradient(135deg, rgba(245, 101, 101, 0.1), rgba(252, 165, 165, 0.05))";
            case "SO2":
                return "linear-gradient(135deg, rgba(168, 85, 247, 0.1), rgba(196, 181, 253, 0.05))";
            case "NO2":
                return "linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(147, 197, 253, 0.05))";
            case "CO":
                return "linear-gradient(135deg, rgba(16, 185, 129, 0.1), rgba(110, 231, 183, 0.05))";
            case "O3":
                return "linear-gradient(135deg, rgba(245, 158, 11, 0.1), rgba(251, 191, 36, 0.05))";
            default:
                return "linear-gradient(135deg, rgba(107, 114, 128, 0.1), rgba(156, 163, 175, 0.05))";
        }
    }
    
    /**
     * 根据污染物类型获取边框颜色
     */
    private String getBorderColorForPollutant(String pollutantName) {
        switch (pollutantName.toUpperCase()) {
            case "PM2.5":
                return "rgba(239, 68, 68, 0.3)";
            case "PM10":
                return "rgba(245, 101, 101, 0.3)";
            case "SO2":
                return "rgba(168, 85, 247, 0.3)";
            case "NO2":
                return "rgba(59, 130, 246, 0.3)";
            case "CO":
                return "rgba(16, 185, 129, 0.3)";
            case "O3":
                return "rgba(245, 158, 11, 0.3)";
            default:
                return "rgba(107, 114, 128, 0.3)";
        }
    }
    
    /**
     * 根据污染物类型获取文本颜色
     */
    private String getTextColorForPollutant(String pollutantName) {
        switch (pollutantName.toUpperCase()) {
            case "PM2.5":
                return "#dc2626";
            case "PM10":
                return "#ea580c";
            case "SO2":
                return "#7c3aed";
            case "NO2":
                return "#2563eb";
            case "CO":
                return "#059669";
            case "O3":
                return "#d97706";
            default:
                return "#374151";
        }
    }

    /**
     * 重置显示
     */
    private void resetDisplay() {
        aqiValueLabel.setText("--");
        aqiCategoryLabel.setText("--");
        primaryPollutantLabel.setText("首要污染物: --");
        healthAdviceLabel.setText("暂无数据");
        pollutantsGrid.getChildren().clear();
        
        // 重置颜色
        aqiValueLabel.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2d3748;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
        aqiCategoryLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #4a5568;" +
            "-fx-font-family: 'SF Pro Display', 'PingFang SC', sans-serif;"
        );
    }

    /**
     * 获取当前空气质量数据
     */
    public AirQuality getAirQuality() {
        return airQuality;
    }
    
    /**
     * 隐藏空气质量面板
     */
    public void hide() {
        this.setVisible(false);
        this.setManaged(false);
    }
    
    /**
     * 显示空气质量面板
     */
    public void show() {
        this.setVisible(true);
        this.setManaged(true);
    }
}