package io.pantheist.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MandelbrotView2 extends View {
    private Sampler sampler = new Sampler();

    private double cenx = -2.0 + 2.0 * 3.0 / 4.0;
    private double ceny = 0.0;
    private double scale = 2.0;

    private Map<Integer,Float> prev_x = new HashMap<>();
    private Map<Integer,Float> prev_y = new HashMap<>();

    private boolean initialized = false;

    public MandelbrotView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        if (!initialized)
        {
            inval();
            initialized = true;
        }
        sampler.draw(c);
        if (sampler.improve())
        {
            invalidate();
        }
    }

    private double hypot(double x, double y)
    {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {
            double scale1 = 1.0 / (double)getWidth();
            if (motionEvent.getPointerCount() == 1) {
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

                double cx1 = 0.5 * (prev_x.get(id0) + prev_x.get(id1) - getWidth()) * scale1;
                double cy1 = 0.5 * (prev_y.get(id0) + prev_y.get(id1) - getHeight()) * scale1;

                double cx2 = 0.5 * (motionEvent.getX(0) + motionEvent.getX(1) - getWidth()) * scale1;
                double cy2 = 0.5 * (motionEvent.getY(0) + motionEvent.getY(1) - getHeight()) * scale1;

                double sf = Math.pow(d1 / d2, 5);

                cenx -= cx2 * scale * (sf+3) - 4 * cx1 * scale;
                ceny -= cy2 * scale * (sf+3) - 4 * cy1 * scale;

                scale *= sf;
            }

            if (cenx < -2) cenx = -2;
            if (cenx > 2) cenx = 2;
            if (ceny < -2) ceny = -2;
            if (ceny > 2) ceny = 2;
            if (scale > 8.0) scale = 8.0;

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

    private void inval() {
        double xscale = scale * getWidth() / getHeight();
        sampler.set_viewport(cenx - xscale, ceny - scale, cenx + xscale, ceny + scale);
        invalidate();
    }
}