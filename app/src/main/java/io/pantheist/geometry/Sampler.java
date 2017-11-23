package io.pantheist.geometry;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Sampler {
    // Viewport
    private double x0, y0, x1, y1;

    private FragmentGrid grid;
    private FragmentGrid grid2;

    private static final int MAX_FRAGMENTS = 200;

    public Sampler()
    {
        grid = newGrid(1.0);
        grid2 = newGrid(0.5);
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
        grid2.draw(c, matrix);
    }

    public boolean improve()
    {
        long endTime = System.currentTimeMillis() + 100;

        if (grid.work(endTime)) return true;
        if (grid2.work(endTime)) return true;

        FragmentGrid new_grid = newGrid(grid2.size() * 0.5 );
        new_grid.set_viewport(x0,y0,x1,y1);
        if (new_grid.tile_count() <= MAX_FRAGMENTS)
        {
            grid = grid2;
            grid2 = new_grid;
            return true;
        }

        return false;
    }

    private FragmentGrid newGrid(double size)
    {
        return new FragmentGrid(size, max_iter_for_size(size));
    }

    public void set_viewport(double x0, double y0, double x1, double y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;

        grid.set_viewport(x0, y0, x1, y1);
        grid2.set_viewport(x0, y0, x1, y1);

        if (grid2.tile_count() > MAX_FRAGMENTS)
        {
            grid2 = grid;
            grid = newGrid(grid.size() * 2 );
            grid.set_viewport(x0,y0,x1,y1);
        }
    }

    private int max_iter_for_size(double size)
    {
        return (int)(-100 * Math.log(size));
    }
}
