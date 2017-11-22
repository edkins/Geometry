package io.pantheist.geometry;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Fragment {
    private static final int SIZE = 16;
    private double x0;
    private double y0;
    private double scale;
    private int progress;
    private boolean in_use = false;
    private Bitmap bitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);

    private static final int MAXITER = 100;
    private static final double BAILOUT = 4000.0;

    public void init(double x0, double y0, double size)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.scale = size / SIZE;
        this.progress = 0;
        this.in_use = true;
    }

    public void abandon()
    {
        this.in_use = false;
    }

    public boolean in_use()
    {
        return in_use;
    }

    public boolean complete()
    {
        return in_use && progress == SIZE * SIZE;
    }

    public boolean overlaps(double x0, double y0, double x1, double y1)
    {
        if (this.x0 + scale * SIZE <= x0) return false;
        if (this.y0 + scale * SIZE <= y0) return false;
        if (this.x0 >= x1) return false;
        if (this.y0 >= y1) return false;
        return true;
    }

    public boolean work(long endTime)
    {
        boolean did_work = false;
        if (!in_use)
        {
            return false;
        }

        while(progress < SIZE*SIZE)
        {
            int x = progress % SIZE;
            int y = (progress-x) / SIZE;
            
            visit(x,y);
            progress++;
            did_work = true;

            if (System.currentTimeMillis() > endTime)
            {
                return true;
            }
        }
        if (did_work)
        {
            System.out.println(String.format("Completed %s %s %s",x0,y0,scale));
        }
        return false;
    }

    private void visit(int x, int y) {
        double cx = x0 + scale * x;
        double cy = y0 + scale * y;

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

        int color;
        if (region == 1)
        {
            int br = 64 + (15 * iter) % 192;
            color = 0xff000000 + 0x10101 * br;
        }
        else {
            color = 0xff400000;
        }

        bitmap.setPixel(x,y,color);
    }

    public void draw(Canvas c, Matrix matrix) {
        Matrix m2 = new Matrix();

        m2.set(matrix);
        m2.preTranslate((float)x0, (float)y0);
        m2.preScale((float)scale, (float)scale);

        c.drawBitmap(bitmap, m2, null);

    }
}
