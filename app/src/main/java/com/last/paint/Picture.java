package com.last.paint;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Picture extends Figure {
    private Bitmap mBitmap;

    public Picture(Bitmap bitmap) {
        super(null, 0, 0);
        mBitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, canvas.getClipBounds(), null);
    }
}