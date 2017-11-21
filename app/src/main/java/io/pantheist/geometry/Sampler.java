package io.pantheist.geometry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sampler {
    private static final int SIZE = 512;
    private double zx[][] = new double[SIZE][SIZE];
    private double zy[][] = new double[SIZE][SIZE];
    private int iter[][] = new int[SIZE][SIZE];
    private int pixw[][] = new int[SIZE][SIZE];

    private double cx0;
    private double cy0;
    private double cx_scale;
    private double cy_scale;

    // Reusable
    private final Result result = new Result();

    // Viewport
    private double x0, y0, x1, y1;

    private Bitmap bitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);

    private static final int MAXITER = 100;
    private static final double BAILOUT = 4000.0;

    public void init()
    {
        cx_scale = 4.0 / SIZE;
        cy_scale = 4.0 / SIZE;
        cx0 = -2;
        cy0 = -2;

        x0 = 0;
        x1 = 384;
        y0 = 0;
        y1 = 512;

        calculate();
    }

    public void draw(Canvas c) {
        Paint paint = new Paint();

        Matrix matrix = new Matrix();
        float a = (float)(c.getWidth()/(x1-x0));
        float b = (float)(c.getHeight()/(y1-y0));
        matrix.setValues(new float[]{
                a, 0.0f, -(float)x0 * a,
                0.0f, b, -(float)y0 * b,
                0.0f, 0.0f, 1.0f
        });

        c.drawBitmap(bitmap, matrix, paint);
    }

    private static class Result
    {
        double zx;
        double zy;
        int iter;
    }

    private void calculate()
    {
        int psize = 64;
        for (int y = 0; y < 512; y += psize)
        {
            for (int x = 0; x < 512; x += psize)
            {
                visit(x, y, psize);
            }
        }
    }

    private void visit(int x, int y, int psize)
    {
        // Check that pixel overlaps viewport
        if (x + psize > x0 && x < x1 && y + psize > y0 && y < y1)
        {
            double cx = (double)x * cx_scale + cx0;
            double cy = (double)y * cy_scale + cy0;

            calculate_pixel(cx, cy, result);
            int color = calculate_color(result);

            zx[x][y] = result.zx;
            zy[x][y] = result.zy;
            iter[x][y] = result.iter;
            pixw[x][y] = psize;

            draw_bitmap_square(x, y, psize, color);
        }
    }

    private int get_fattest_pixel()
    {
        int psize = 1;
        for (int y = 0; y < 512; y ++) {
            for (int x = 0; x < 512; x ++) {
                if (pixw[x][y] > psize)
                {
                    psize = pixw[x][y];
                }
            }
        }
        return psize;
    }

    public boolean improve()
    {
        long endTime = System.currentTimeMillis() + 100;
        int psize = get_fattest_pixel();

        if (psize == 1)
        {
            return false;
        }

        int p2 = psize/2;

        for (int y = 0; y < 512; y += psize) {
            for (int x = 0; x < 512; x += psize) {
                if (pixw[x][y] == psize)
                {
                    visit(x,y,p2);
                    visit(x+p2,y,p2);
                    visit(x,y+p2,p2);
                    visit(x+p2,y+p2,p2);
                }
                if (System.currentTimeMillis() > endTime)
                {
                    return true;
                }
            }
        }
        return true;
    }

    private void draw_bitmap_square(int x, int y, int psize, int color) {
        for (int i = 0; i < psize; i++)
        {
            for (int j = 0; j < psize; j++)
            {
                bitmap.setPixel(x+i,y+j, color);
            }
        }
    }

    private void calculate_pixel(double cx, double cy, Result result) {
        double zx = 0;
        double zy = 0;

        int region = 0;
        int iter = 0;
        while (iter < MAXITER) {
            double xx = zx * zx;
            double yy = zy * zy;
            if (xx + yy > BAILOUT) {
                region = 1;
                break;
            }

            double nzx = xx - yy + cx;
            zy = 2 * zx * zy + cy;
            zx = nzx;
            iter++;
        }

        result.zx = zx;
        result.zy = zy;
        result.iter = iter;
    }

    private int calculate_color(Result result)
    {
        if (result.zx * result.zx + result.zy * result.zy > BAILOUT)
        {
            int br = 64 + (15 * result.iter) % 192;
            return 0xff000000 + 0x10101 * br;
        }
        else {
            return 0xff400000;
        }
    }

    public void set_viewport(double x0, double y0, double x1, double y1)
    {
        this.x0 = (x0 - cx0) / cx_scale;
        this.y0 = (y0 - cy0) / cy_scale;
        this.x1 = (x1 - cx0) / cx_scale;
        this.y1 = (y1 - cy0) / cy_scale;
    }
}
