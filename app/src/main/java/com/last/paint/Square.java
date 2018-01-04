package com.last.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Square extends Figure {

    public Square(PointF startPoint, int color, float paintWeight) {
        super(startPoint, color, paintWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(getStartPoint().x, getEndPoint().x);
        float right = Math.max(getStartPoint().x, getEndPoint().x);
        float top = Math.min(getStartPoint().y, getEndPoint().y);
        float bottom = right - left + top;
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getPaintWeight());
        canvas.drawRect(left, top, right, bottom, paint);
    }
}