package com.last.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Line extends Figure {

    public Line(PointF startPoint, int color, float paintWeight) {
        super(startPoint, color, paintWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float x1 = getStartPoint().x;
        float x2 = getEndPoint().x;
        float y1 = getStartPoint().y;
        float y2 = getEndPoint().y;
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getPaintWeight());
        canvas.drawLine(x1, y1, x2, y2, paint);
    }
}