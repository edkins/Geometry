package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by giles on 18/11/17.
 */

public class FractalView extends View {
    private final Bitmap bitmap;
    private final Paint paint;

    public FractalView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.bitmap = Bitmap.createBitmap(96, 128, Bitmap.Config.ARGB_8888);
        this.paint = new Paint();
        paint.setColor(0xffffc040);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        for (int y = 0; y < 128; y++)
        {
            for (int x = 0; x < 96; x++)
            {
                bitmap.setPixel(x,y,0xff000000 + x + y);
            }
        }

        c.drawBitmap(bitmap,
                new Rect(0,0,96,128),
                new Rect(0,0,getWidth(),getHeight()),
                paint);
    }
}
