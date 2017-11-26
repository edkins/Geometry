package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giles on 18/11/17.
 */

public class JuliaView2 extends View {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 128;
    private static final int MAXITER = 100;
    private static final double BAILOUT = 4000.0;
    private final Bitmap bitmap;
    private final Paint paint;
    private final Rect srcRect;
    private final Rect dstRect;

    public Coefficients2 c = new Coefficients2();
    public Coefficients2 prev_c = new Coefficients2();

    private Map<Integer,Float> prev_x = new HashMap<>();
    private Map<Integer,Float> prev_y = new HashMap<>();
    private Map<Integer,Coefficients2.Coefficient> prev_coeff = new HashMap<>();

    private boolean changed = true;

    public JuliaView2(Context context, AttributeSet attrs) {
        super(context,attrs);

        this.bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        this.paint = new Paint();
        this.srcRect = new Rect(0,0,WIDTH, HEIGHT);
        this.dstRect = new Rect(0,0,0,0);

        regenerate();
    }

    private void regenerate()
    {
        CxMut t2 = CxMut.of(0,0);
        CxMut u2 = CxMut.of(0,0);
        CxMut zz = CxMut.of(0,0);

        CxMut z = CxMut.of(0,0);

        double scale = 8.0 / WIDTH;
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                z.set_xy(
                        ((double)x - WIDTH*0.5) * scale,
                        ((double)y - HEIGHT*0.5) * scale);

                int region = 0;
                int iter = 0;
                while (iter < MAXITER)
                {
                    zz.set(z);
                    zz.mul(z);

                    t2.set(zz);
                    t2.add(c.c0);

                    u2.set(z);
                    u2.mul(c.d0);
                    u2.add_xy(1.0,0.0);

                    t2.div(u2);
                    if (z.close_to(t2))
                    {
                        region = 1;
                        break;
                    }
                    z.set(t2);
                    iter++;
                }

                int color = 0xff400000;
                int br = 64 + (15 * iter) % 192;
                switch(region)
                {
                    case 1:
                        color = 0xff000000 + 0x10101 * br;
                        break;
                    case 2:
                        color = 0xff000000 + 0x100 * br;
                        break;
                    case 3:
                        color = 0xff000000 + 0x10000 * br;
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

    private Coefficients2.Coefficient getCoefficient(float x, float y)
    {
        if (y < getHeight() / 2)
        {
            return Coefficients2.Coefficient.C0;
        }
        else
        {
            return Coefficients2.Coefficient.D0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN ||
                motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN ||
                motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
        {
            prev_c.set(c);
            prev_x.clear();
            prev_y.clear();
            prev_coeff.clear();
            for (int i = 0; i < motionEvent.getPointerCount(); i++)
            {
                int id = motionEvent.getPointerId(i);
                prev_x.put(id,motionEvent.getX(i));
                prev_y.put(id,motionEvent.getY(i));
                prev_coeff.put(id, getCoefficient(motionEvent.getX(i),motionEvent.getY(i)));
            }
        }
        else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE)
        {
            c.set(prev_c);
            double scale = 1.0 / getWidth();

            for (int i = 0; i < motionEvent.getPointerCount(); i++)
            {
                int id = motionEvent.getPointerId(i);
                Coefficients2.Coefficient coeff = prev_coeff.get(id);
                c.change(coeff,
                        (motionEvent.getX(i)-prev_x.get(id))*scale,
                        (motionEvent.getY(i)-prev_y.get(id))*scale);
            }

            inval();
        }

        return true;
    }
}
