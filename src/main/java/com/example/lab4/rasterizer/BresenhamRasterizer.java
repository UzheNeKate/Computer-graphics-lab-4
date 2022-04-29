package com.example.lab4.rasterizer;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class BresenhamRasterizer implements Rasterizer {
    @Override
    public Point2D[] rasterize(Point2D begin, Point2D end) {
        int deltaX = (int) Math.abs(end.getX() - begin.getX());
        int deltaY = (int) Math.abs(end.getY() - begin.getY());
        if (deltaY > deltaX) {
            begin = new Point2D(begin.getY(), begin.getX());
            end = new Point2D(end.getY(), end.getX());
            var tmp = deltaX;
            deltaX = deltaY;
            deltaY = tmp;
        }

        int error = deltaX / 2;
        int y = (int) begin.getY();
        int dirY = (int) Math.signum(end.getY() - begin.getY());

        List<Point2D> points = new ArrayList<>();
        for (int x = (int) begin.getX(); x <= end.getX(); x++) {
            points.add(new Point2D(x, y));
            error -= deltaY;
            if (error < 0) {
                y = y + dirY;
                error += deltaX;
            }
        }
        return points.toArray(Point2D[]::new);
    }
}
