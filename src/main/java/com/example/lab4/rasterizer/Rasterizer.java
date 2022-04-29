package com.example.lab4.rasterizer;

import javafx.geometry.Point2D;

public interface Rasterizer {
    Point2D[] rasterize(Point2D begin, Point2D end);
}
