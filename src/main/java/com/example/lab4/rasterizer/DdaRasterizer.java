package com.example.lab4.rasterizer;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class DdaRasterizer implements Rasterizer {
    @Override
    public Point2D[] rasterize(Point2D begin, Point2D end) {
        var deltaX = end.getX() - begin.getX();
        var deltaY = end.getY() - begin.getY();
        var step = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        deltaX /= step;
        deltaY /= step;
        var x = begin.getX();
        var y = begin.getY();

        var points = new ArrayList<Point2D>();
        points.add(new Point2D(x, y));
        for (int i = 0; i < step; i++) {
            x += deltaX;
            y += deltaY;
            points.add(new Point2D(x, Math.round(y)));
        }
        return points.toArray(Point2D[]::new);
    }
}
