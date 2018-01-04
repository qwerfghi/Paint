package com.last.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Oval extends Figure {
    public Oval(PointF startPoint, int color, float paintWeight) {
        super(startPoint, color, paintWeight);
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(this.getStartPoint().x, this.getEndPoint().x);
        float right = Math.max(this.getStartPoint().x, this.getEndPoint().x);
        float top = Math.min(this.getStartPoint().y, this.getEndPoint().y);
        float bottom = Math.max(this.getStartPoint().y, this.getEndPoint().y);
        Paint paint = new Paint();
        paint.setColor(getColor());
        paint.setStrokeWidth(getPaintWeight());
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawOval(rectF, paint);
    }
}