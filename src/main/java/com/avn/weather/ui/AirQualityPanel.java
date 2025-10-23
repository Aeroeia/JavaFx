package com.avn.weather.ui;

import com.avn.weather.model.quality.AirQuality;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.format.DateTimeFormatter;

/**
 * 空气质量显示面板组件
 */
public class AirQualityPanel extends VBox {
    
    private Label titleLabel;
    private Label aqiLabel;
    private Label levelLabel;
    private Label updateTimeLabel;
    private Label descriptionLabel;
    private Label adviceLabel;
    private GridPane detailsGrid;
    private Label sourceLabel;
    
    public AirQualityPanel() {
        initializeComponents();
        setupLayout();
        applyStyles();
    }
    
    private void initializeComponents() {
        titleLabel = new Label("空气质量");
        aqiLabel = new Label("--");
        levelLabel = new Label("--");
        updateTimeLabel = new Label("更新时间: --");
        descriptionLabel = new Label("--");
        adviceLabel = new Label("健康建议: --");
        detailsGrid = new GridPane();
        sourceLabel = new Label("数据来源: --");
    }
    
    private void setupLayout() {
        setSpacing(10);
        setPadding(new Insets(15));
        setAlignment(Pos.TOP_LEFT);
        
        // 标题和主要指标
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox aqiBox = new VBox(2);
        aqiBox.setAlignment(Pos.CENTER);
        aqiBox.getChildren().addAll(aqiLabel, levelLabel);
        
        headerBox.getChildren().addAll(titleLabel, aqiBox);
        
        // 详细数据网格
        setupDetailsGrid();
        
        getChildren().addAll(
            headerBox,
            updateTimeLabel,
            descriptionLabel,
            adviceLabel,
            detailsGrid,
            sourceLabel
        );
    }
    
    private void setupDetailsGrid() {
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(8);
        detailsGrid.setPadding(new Insets(10, 0, 0, 0));
        
        // 添加详细指标标签
        String[] labels = {"PM2.5", "PM10", "SO₂", "NO₂", "CO", "O₃"};
        for (int i = 0; i < labels.length; i++) {
            Label nameLabel = new Label(labels[i]);
            nameLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
            nameLabel.setStyle("-fx-text-fill: #6c757d;");
            
            Label valueLabel = new Label("--");
            valueLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            valueLabel.setStyle("-fx-text-fill: #495057;");
            
            detailsGrid.add(nameLabel, i % 3, (i / 3) * 2);
            detailsGrid.add(valueLabel, i % 3, (i / 3) * 2 + 1);
        }
    }
    
    private void applyStyles() {
        setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: #dee2e6; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8;");
        
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #495057;");
        
        aqiLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        
        levelLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        updateTimeLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
        updateTimeLabel.setStyle("-fx-text-fill: #6c757d;");
        
        descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        descriptionLabel.setStyle("-fx-text-fill: #495057;");
        descriptionLabel.setWrapText(true);
        
        adviceLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        adviceLabel.setStyle("-fx-text-fill: #28a745;");
        adviceLabel.setWrapText(true);
        
        sourceLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));
        sourceLabel.setStyle("-fx-text-fill: #6c757d;");
        
        setPrefWidth(400);
    }
    
    public void updateAirQuality(AirQuality airQuality) {
        if (airQuality != null) {
            aqiLabel.setText(String.valueOf(airQuality.getAqi()));
            levelLabel.setText(airQuality.getQualityLevel());
            
            // 根据AQI等级设置颜色
            String color = getAqiColor(airQuality.getAqi());
            aqiLabel.setStyle("-fx-text-fill: " + color + ";");
            levelLabel.setStyle("-fx-text-fill: " + color + ";");
            
            updateTimeLabel.setText("更新时间: " + 
                airQuality.getUpdateTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));
            
            descriptionLabel.setText(airQuality.getQualityDescription());
            adviceLabel.setText("健康建议: " + airQuality.getHealthAdvice());
            sourceLabel.setText("数据来源: " + airQuality.getDataSource());
            
            // 更新详细数据
            updateDetailValues(airQuality);
        } else {
            // 当查询的数据不存在或者没选择区域时数据显示为空
            titleLabel.setText("空气质量");
            aqiLabel.setText("--");
            levelLabel.setText("--");
            
            // 重置颜色样式为默认
            aqiLabel.setStyle("-fx-text-fill: #495057;");
            levelLabel.setStyle("-fx-text-fill: #495057;");
            
            updateTimeLabel.setText("更新时间: --");
            descriptionLabel.setText("--");
            adviceLabel.setText("健康建议: --");
            sourceLabel.setText("数据来源: --");
            
            // 清空详细数据显示
            clearDetailValues();
        }
    }
    
    private void updateDetailValues(AirQuality airQuality) {
        double[] values = {
            airQuality.getPm25(),
            airQuality.getPm10(),
            airQuality.getSo2(),
            airQuality.getNo2(),
            airQuality.getCo(),
            airQuality.getO3()
        };
        
        String[] units = {"μg/m³", "μg/m³", "μg/m³", "μg/m³", "mg/m³", "μg/m³"};
        
        for (int i = 0; i < values.length; i++) {
            Label valueLabel = (Label) detailsGrid.getChildren().get(i * 2 + 1);
            if (i == 4) { // CO 单位是 mg/m³
                valueLabel.setText(String.format("%.2f %s", values[i], units[i]));
            } else {
                valueLabel.setText(String.format("%.0f %s", values[i], units[i]));
            }
        }
    }
    
    private void clearDetailValues() {
        // 清空所有详细数据值标签
        for (int i = 0; i < 6; i++) {
            Label valueLabel = (Label) detailsGrid.getChildren().get(i * 2 + 1);
            valueLabel.setText("--");
        }
    }
    
    private String getAqiColor(int aqi) {
        if (aqi <= 50) {
            return "#00e400"; // 优 - 绿色
        } else if (aqi <= 100) {
            return "#ffff00"; // 良 - 黄色
        } else if (aqi <= 150) {
            return "#ff7e00"; // 轻度污染 - 橙色
        } else if (aqi <= 200) {
            return "#ff0000"; // 中度污染 - 红色
        } else if (aqi <= 300) {
            return "#8f3f97"; // 重度污染 - 紫色
        } else {
            return "#7e0023"; // 严重污染 - 褐红色
        }
    }
}