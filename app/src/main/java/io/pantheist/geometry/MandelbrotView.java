package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MandelbrotView extends View {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 128;
    private static final int MAXITER = 100;
    private static final double BAILOUT = 4000.0;

    private final Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
    private final Paint paint = new Paint();
    private final Rect srcRect = new Rect(0,0,WIDTH, HEIGHT);
    private final Rect dstRect = new Rect(0,0,0,0);

    private double cenx = -1.0;
    private double ceny = 0.0;
    private double scale = 4.0 / WIDTH;

    private Map<Integer,Float> prev_x = new HashMap<>();
    private Map<Integer,Float> prev_y = new HashMap<>();

    private boolean changed = true;
    public MandelbrotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        regenerate();
    }

    private void regenerate()
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                double cx = ((double)x - WIDTH*0.5) * scale + cenx;
                double cy = ((double)y - HEIGHT*0.5) * scale + ceny;

                double zx = 0;
                double zy = 0;

                int region = 0;
                int iter = 0;
                while (iter < MAXITER)
                {
                    double xx = zx * zx;
                    double yy = zy * zy;
                    if (xx + yy > BAILOUT)
                    {
                        region = 1;
                        break;
                    }

                    double nzx = xx - yy + cx;
                    zy = 2 * zx * zy + cy;
                    zx = nzx;
                    iter++;
                }

                int color = 0xff400000;
                int br = 64 + (15 * iter) % 192;
                switch(region)
                {
                    case 1:
                        color = 0xff000000 + 0x10101 * br;
                        break;
                }
                bitmap.setPixel(x,y,color);
            }
        }
    }

    private void inval()
    {
        changed = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c)
    {
        if (changed)
        {
            regenerate();
            changed = false;
        }

        dstRect.set(0,0,getWidth(),getHeight());
        c.drawBitmap(bitmap,
                srcRect,
                dstRect,
                paint);
    }

    private double hypot(double x, double y)
    {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (motionEvent.getPointerCount() == 1) {
                double scale1 = (double)WIDTH / (double)getWidth();
                double dx = (motionEvent.getX(0) - prev_x.get(motionEvent.getPointerId(0))) * scale1;
                double dy = (motionEvent.getY(0) - prev_y.get(motionEvent.getPointerId(0))) * scale1;

                cenx -= 2 * dx * scale;
                ceny -= 2 * dy * scale;
            }
            else if (motionEvent.getPointerCount() == 2) {
                int id0 = motionEvent.getPointerId(0);
                int id1 = motionEvent.getPointerId(1);
                double d1 = hypot(
                        prev_x.get(id0) - prev_x.get(id1),
                        prev_y.get(id0) - prev_y.get(id1));
                double d2 = hypot(
                        motionEvent.getX(0) - motionEvent.getX(1),
                        motionEvent.getY(0) - motionEvent.getY(1));

                scale *= Math.pow(d1 / d2, 3);
            }

            inval();
        }

        prev_x.clear();
        prev_y.clear();
        for (int i = 0; i < motionEvent.getPointerCount(); i++)
        {
            int id = motionEvent.getPointerId(i);
            prev_x.put(id,motionEvent.getX(i));
            prev_y.put(id,motionEvent.getY(i));
        }

        return true;
    }

}
