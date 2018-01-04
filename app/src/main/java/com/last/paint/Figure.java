package com.last.paint;

import android.graphics.Canvas;
import android.graphics.PointF;

public abstract class Figure {
    private int mColor;
    private float mPaintWeight;
    private PointF mStartPoint;
    private PointF mEndPoint;

    public Figure(PointF startPoint, int color, float paintWeight) {
        mStartPoint = startPoint;
        mEndPoint = startPoint;
        mColor = color;
        mPaintWeight = paintWeight;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(PointF endPoint) {
        mEndPoint = endPoint;
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public float getPaintWeight() {
        return mPaintWeight;
    }

    public abstract void draw(Canvas canvas);
}