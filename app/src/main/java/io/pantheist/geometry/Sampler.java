package io.pantheist.geometry;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Sampler {
    // Viewport
    private double x0, y0, x1, y1;

    private FragmentGrid grid;

    public Sampler()
    {
        grid = new FragmentGrid(1.0);
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

        grid.draw(c, matrix);
    }

    public boolean improve()
    {
        long endTime = System.currentTimeMillis() + 100;

        return grid.work(endTime);
    }

    public void set_viewport(double x0, double y0, double x1, double y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;

        grid.set_viewport(x0, y0, x1, y1);
    }
}
