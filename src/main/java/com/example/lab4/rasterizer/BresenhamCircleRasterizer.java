package com.example.lab4.rasterizer;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class BresenhamCircleRasterizer {
    public Point2D[] rasterizeCircle(Point2D center, int radius) {
        int x = 0;
        int y = radius;
        int e = 3 - 2 * radius;
        List<Point2D> points = new ArrayList<>();

        while (x <= y) {
            points.add(new Point2D(x, y));
            if (e >= 0) {
                e += 4 * (x - y) + 10;
                x++;
                y--;
            } else {
                e += 4 * x + 6;
                x++;
            }
        }
        return normalize(points, center);
    }

    private Point2D[] normalize(List<Point2D> points, Point2D center) {
        var normalized = new ArrayList<Point2D>(points.size() * 8);
        for (var point : points) {
            normalized.add(new Point2D(point.getX() + center.getX(), point.getY() + center.getY()));
            normalized.add(new Point2D(point.getX() + center.getX(), -point.getY() + center.getY()));
            normalized.add(new Point2D(-point.getX() + center.getX(), point.getY() + center.getY()));
            normalized.add(new Point2D(-point.getX() + center.getX(), -point.getY() + center.getY()));
            normalized.add(new Point2D(point.getY() + center.getX(), point.getX() + center.getY()));
            normalized.add(new Point2D(point.getY() + center.getX(), -point.getX() + center.getY()));
            normalized.add(new Point2D(-point.getY() + center.getX(), point.getX() + center.getY()));
            normalized.add(new Point2D(-point.getY() + center.getX(), -point.getX() + center.getY()));
        }
        return normalized.toArray(Point2D[]::new);
    }
}
