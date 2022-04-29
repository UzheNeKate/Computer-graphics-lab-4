package com.example.lab4.view;

import com.example.lab4.rasterizer.BresenhamCircleRasterizer;
import com.example.lab4.rasterizer.BresenhamRasterizer;
import com.example.lab4.rasterizer.DdaRasterizer;
import com.example.lab4.rasterizer.StepByStepRasterizer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class View {
    TextField tfBeginX, tfBeginY, tfEndX, tfEndY, tfCircleX, tfCircleY, tfRadius;
    ComboBox<String> cbLineType, cbCircleType;
    Node currentLineCanvas, currentCircleCanvas;
    GridPane pane;

    public void start(Stage stage) {
        stage.setTitle("Rasterizer");
        Group root = new Group();
        pane = new GridPane();
        pane.addRow(0, lineMenuPane());
        pane.addRow(1, drawCanvas(null, null));
        pane.addRow(0, circleMenuPane());
        pane.addRow(1, drawCanvas(null, null));

        root.getChildren().add(pane);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private Pane lineMenuPane() {
        var pane = new GridPane();
        pane.setPadding(new Insets(15, 12, 15, 12));
        var options = List.of("Bresenham", "Dda", "StepByStep");
        var textBegin = new Text("Start Point (X, Y)");
        tfBeginX = new TextField();
        tfBeginX.setPromptText("X");
        tfBeginY = new TextField();
        tfBeginY.setPromptText("Y");
        var textEnd = new Text("End Point (X, Y)");
        tfEndX = new TextField();
        tfEndX.setPromptText("X");
        tfEndY = new TextField();
        tfEndY.setPromptText("Y");
        pane.addRow(0, new Text("Line"));
        cbLineType = new ComboBox<>(FXCollections.observableArrayList(options));
        pane.addRow(1, new Text("Filter type: "), cbLineType);
        pane.addRow(2, textBegin, tfBeginX, tfBeginY);
        pane.addRow(3, textEnd, tfEndX, tfEndY);
        var bt = new Button("Rasterize");
        bt.setOnAction((e) -> rasterizeLine());
        pane.addRow(4, bt);
        return pane;
    }

    private void rasterizeLine() {
        var beginXStr = tfBeginX.getText();
        var beginYStr = tfBeginY.getText();
        var endXStr = tfEndX.getText();
        var endYStr = tfEndY.getText();
        Point2D begin, end;
        try {
            begin = new Point2D(Integer.parseInt(beginXStr), Integer.parseInt(beginYStr));
            end = new Point2D(Integer.parseInt(endXStr), Integer.parseInt(endYStr));
        } catch (NumberFormatException e) {
            return;
        }
        Point2D[] points = new Point2D[]{};
        var type = cbLineType.getValue();
        if (type != null) {
            switch (type) {
                case "Bresenham" -> points = new BresenhamRasterizer().rasterize(begin, end);
                case "Dda" -> points = new DdaRasterizer().rasterize(begin, end);
                case "StepByStep" -> points = new StepByStepRasterizer().rasterize(begin, end);
            }
        }
        pane.add(drawCanvas(lineSeries(begin, end), points), 0, 1);
    }

    private Node drawCanvas(XYChart.Series<Number, Number> figSeries, Point2D[] rasterization) {
        final NumberAxis x = new NumberAxis(-20, 20, 1);
        final NumberAxis y = new NumberAxis(-20, 20, 1);

        x.setAutoRanging(false);
        y.setAutoRanging(false);

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(x, y);
        lineChart.setPrefSize(600, 600);
        lineChart.setMaxSize(600, 600);
        lineChart.setMinSize(600, 600);

        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setAlternativeColumnFillVisible(false);
        lineChart.setAlternativeRowFillVisible(false);

        if (rasterization == null) {
            return lineChart;
        }
        for (var point : rasterization) {
            final var series = new XYChart.Series<Number, Number>();
            var pixelRect = new XYChart.Data<Number, Number>(point.getX() + 0.5, point.getY() + 0.5);
            var r = new Rectangle(0, 0, 14, 14);
            r.setFill(Color.BEIGE);
            r.setStroke(Color.BLACK);
            pixelRect.setNode(r);
            series.getData().add(pixelRect);

            lineChart.getData().add(series);
        }
        if (figSeries == null) {
            return lineChart;
        }
        lineChart.getData().add(figSeries);

        return lineChart;
    }

    private XYChart.Series<Number, Number> lineSeries(Point2D begin, Point2D end) {
        var lineSeries = new XYChart.Series<Number, Number>();
        var beginData = new XYChart.Data<Number, Number>(begin.getX(), begin.getY());
        var endData = new XYChart.Data<Number, Number>(end.getX(), end.getY());
        lineSeries.getData().add(beginData);
        lineSeries.getData().add(endData);
        return lineSeries;
    }


    private XYChart.Series<Number, Number> circleSeries(Point2D center, int radius) {
        var lineSeries = new XYChart.Series<Number, Number>();
        var centerData = new XYChart.Data<Number, Number>(center.getX() + 0.5, center.getY() + 0.5);
        var c = new Circle(radius * 13.5);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.RED);
        centerData.setNode(c);
        lineSeries.getData().add(centerData);
        return lineSeries;
    }

    private Pane circleMenuPane() {
        var pane = new GridPane();
        pane.setPadding(new Insets(15, 12, 15, 12));
        var options = List.of("BresenhamCircle");
        var textBegin = new Text("Center Point (X, Y)");
        tfCircleX = new TextField();
        tfCircleX.setPromptText("X");
        tfCircleY = new TextField();
        tfCircleY.setPromptText("Y");
        var textRadius = new Text("Radius");
        tfRadius = new TextField();
        tfRadius.setPromptText("Radius");
        pane.addRow(0, new Text("Circle"));
        cbCircleType = new ComboBox<>(FXCollections.observableArrayList(options));
        pane.addRow(1, new Text("Filter type: "), cbCircleType);
        pane.addRow(2, textBegin, tfCircleX, tfCircleY);
        pane.addRow(3, textRadius, tfRadius);
        var bt = new Button("Rasterize");
        bt.setOnAction((e) -> rasterizeCircle());
        pane.addRow(4, bt);
        return pane;
    }

    private void rasterizeCircle() {
        var circleXStr = tfCircleX.getText();
        var circleYStr = tfCircleY.getText();
        var radiusStr = tfRadius.getText();
        Point2D center;
        int radius;
        try {
            center = new Point2D(Integer.parseInt(circleXStr), Integer.parseInt(circleYStr));
            radius = Integer.parseInt(radiusStr);
        } catch (NumberFormatException e) {
            return;
        }
        Point2D[] points = new Point2D[]{};
        var type = cbCircleType.getValue();
        if (type != null) {
            if ("BresenhamCircle".equals(type)) {
                points = new BresenhamCircleRasterizer().rasterizeCircle(center, radius);
            }
        }
        pane.add(drawCanvas(circleSeries(center, radius), points), 1, 1);
    }
}
