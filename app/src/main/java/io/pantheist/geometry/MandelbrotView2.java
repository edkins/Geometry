package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MandelbrotView2 extends View {
    Sampler sampler = new Sampler();

    public MandelbrotView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        sampler.init();
    }

    @Override
    protected void onDraw(Canvas c)
    {
        sampler.draw(c);
        if (sampler.improve())
        {
            invalidate();
        }
    }
}