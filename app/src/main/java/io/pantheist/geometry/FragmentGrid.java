package io.pantheist.geometry;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

public class FragmentGrid {
    private double size;
    private long x0 = 0;
    private long y0 = 0;
    private long x1 = 0;
    private long y1 = 0;
    private List<Fragment> fragments;
    private int max_iter;

    public FragmentGrid(double size, int max_iter)
    {
        this.size = size;
        this.max_iter = max_iter;
    }

    public double size()
    {
        return size;
    }

    public int tile_count()
    {
        return (int)((x1-x0)*(y1-y0));
    }

    public void set_viewport(double vx0, double vy0, double vx1, double vy1)
    {
        long old_x0 = x0;
        long old_y0 = y0;
        long old_x1 = x1;
        long old_y1 = y1;

        x0 = (long)Math.floor(vx0 / size);
        y0 = (long)Math.floor(vy0 / size);
        x1 = (long)Math.ceil(vx1 / size);
        y1 = (long)Math.ceil(vy1 / size);

        List<Fragment> new_fragments = new ArrayList<>();

        for (long y = y0; y < y1; y++)
        {
            for (long x = x0; x < x1; x++)
            {
                if (x >= old_x0 && y >= old_y0 && x < old_x1 && y < old_y1)
                {
                    long old_ix = (y - old_y0) * (old_x1 - old_x0) + (x - old_x0);
                    new_fragments.add(fragments.get((int)old_ix));
                }
                else
                {
                    new_fragments.add(new Fragment(x * size, y * size, size, max_iter));
                }
            }
        }

        fragments = new_fragments;
    }

    public void draw(Canvas c, Matrix m)
    {
        for (Fragment fragment : fragments)
        {
            fragment.draw(c,m);
        }
    }

    public boolean work(long endTime)
    {
        for (Fragment fragment : fragments)
        {
            if (fragment.work(endTime))
            {
                return true;
            }
        }
        return false;
    }
}
