package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by giles on 18/11/17.
 */

public class FractalView extends View {
    public FractalView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        Paint p = new Paint();
        p.setColor(0xff123456);
        c.drawRect(0,0,100,100, p);
        c.drawRect(100,100,200,200, p);
    }
}
