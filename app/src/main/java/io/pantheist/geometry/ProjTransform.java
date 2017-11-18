package io.pantheist.geometry;

/**
 * Created by giles on 12/08/17.
 */

public class ProjTransform {
    public final Complex a;
    public final Complex b;
    public final Complex c;
    public final Complex d;

    private ProjTransform(Complex a, Complex b, Complex c, Complex d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public ProjTransform of(Complex a, Complex b, Complex c, Complex d)
    {
        return new ProjTransform(a,b,c,d);
    }
}
