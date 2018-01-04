package com.last.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    private Figure mCurrentFigure;
    private FigureType mFigureType;
    private List<Figure> mFigures = new ArrayList<>();
    private Paint mFigurePaint;
    private Paint mBackgroundPaint;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFigurePaint = new Paint();
        mFigurePaint.setColor(Color.CYAN);
        mFigurePaint.setStrokeWidth(5);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mFigureType = FigureType.RECTANGLE;
    }

    public Paint getFigurePaint() {
        return mFigurePaint;
    }

    public List<Figure> getFigures() {
        return mFigures;
    }

    public void setFigureType(FigureType figureType) {
        mFigureType = figureType;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentFigure = getFigureByFigureType(current);
                mFigures.add(mCurrentFigure);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentFigure != null) {
                    mCurrentFigure.setEndPoint(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentFigure = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentFigure = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Figure figure : mFigures) {
            figure.draw(canvas);
        }
    }

    private Figure getFigureByFigureType(PointF current) {
        switch (mFigureType) {
            case CIRCLE:
                return new Circle(current, mFigurePaint.getColor(), mFigurePaint.getStrokeWidth());
            case RECTANGLE:
                return new Rectangle(current, mFigurePaint.getColor(), mFigurePaint.getStrokeWidth());
            case OVAL:
                return new Oval(current, mFigurePaint.getColor(), mFigurePaint.getStrokeWidth());
            case SQUARE:
                return new Square(current, mFigurePaint.getColor(), mFigurePaint.getStrokeWidth());
            case LINE:
                return new Line(current, mFigurePaint.getColor(), mFigurePaint.getStrokeWidth());
            default:
                return null;
        }
    }
}