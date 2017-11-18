package io.pantheist.geometry;

/**
 * Created by giles on 12/08/17.
 */

public class ProjPoint {
    private final Complex z;
    private final Complex w;

    private ProjPoint( Complex z, Complex w)
    {
        this.z = z;
        this.w = w;
    }

    public static ProjPoint of(Complex z, Complex w)
    {
        double scale = 1.0 / nearbyPowerOf2(z.manhattan() + w.manhattan());
        return new ProjPoint(z.scale(scale), w.scale(scale));
    }

    private static double nearbyPowerOf2( double x )
    {
        double result = 1.0;
        int i = 0;
        Math.getExponent(x);
        while (result < x)
        {
            double next = result * 2.0;
            if (Double.isInfinite(next) || 1.0/next == 0.0)
            {
                return result;
            }
            result = next;
        }
        while (result > x)
        {
            double next = result * 0.5;
            if (Double.isInfinite(1.0/next) || next == 0.0)
            {
                return result;
            }
            result = next;
        }
        return result;
    }

    public boolean isInSqrtRadius(double limit)
    {
        return z.abs_squared() < w.abs_squared() * limit;
    }

    public ProjPoint transform(ProjTransform tr)
    {
        return ProjPoint.of(z.mul(tr.a).add(w.mul(tr.b)), z.mul(tr.c).add(w.mul(tr.d)));
    }
}
