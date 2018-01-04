package com.last.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Circle extends Figure {

    public Circle(PointF startPoint, int color, float paintWeight) {
        super(startPoint, color, paintWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float x1 = getStartPoint().x;
        float x2 = getEndPoint().x;
        float y1 = getStartPoint().y;
        float y2 = getEndPoint().y;
        double radius = Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getPaintWeight());
        canvas.drawCircle(getStartPoint().x, getStartPoint().y, (float) radius, paint);
    }
}