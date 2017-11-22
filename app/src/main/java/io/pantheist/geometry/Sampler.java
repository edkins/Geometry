package io.pantheist.geometry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sampler {
    // Viewport
    private double x0, y0, x1, y1;

    private List<Fragment> fragments;
    private boolean initialized = false;

    public Sampler()
    {
        fragments = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            fragments.add(new Fragment());
        }
    }

    public void draw(Canvas c) {
        Matrix matrix = new Matrix();
        float a = (float)(c.getWidth()/(x1-x0));
        float b = (float)(c.getHeight()/(y1-y0));
        matrix.setValues(new float[]{
                a, 0.0f, -(float)x0 * a,
                0.0f, b, -(float)y0 * b,
                0.0f, 0.0f, 1.0f
        });

        for (Fragment fragment : fragments)
        {
            if (fragment.complete())
            {
                fragment.draw(c, matrix);
            }
        }
    }

    public boolean improve()
    {
        long endTime = System.currentTimeMillis() + 100;

        for (Fragment fragment : fragments)
        {
            if (fragment.work(endTime))
            {
                return true;
            }
        }
        return false;
    }

    public void set_viewport(double x0, double y0, double x1, double y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;

        if (!initialized)
        {
            double size = (y1 - y0) / 12;
            for (int y = 0; y < 12; y++)
            {
                for (int x = 0; x < 8; x++)
                {
                    this.fragments.get(8 * y + x).init(x0 + x * size, y0 + y * size, size);
                }
            }
            initialized = true;
        }
    }
}
