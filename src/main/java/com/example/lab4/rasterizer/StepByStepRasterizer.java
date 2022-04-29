package com.example.lab4.rasterizer;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class StepByStepRasterizer implements Rasterizer {
    @Override
    public Point2D[] rasterize(Point2D begin, Point2D end) {
        var points = new ArrayList<Point2D>();
        var k = (end.getY() - begin.getY()) / (end.getX() - begin.getX());
        var b = begin.getY() - k * begin.getX();
        for (int x = (int) begin.getX(); x <= end.getX(); x++) {
            points.add(new Point2D(x, Math.round(k * x + b)));
        }
        return points.toArray(Point2D[]::new);
    }
}
