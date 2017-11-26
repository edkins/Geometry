package io.pantheist.geometry;

public class Coefficients2 {
    public CxMut c0 = CxMut.of(0.25,0.03);
    public CxMut d0 = CxMut.of(0.0,0.0);

    public enum Coefficient
    {
        C0,D0;
    }

    public void set(Coefficients2 other)
    {
        c0.set(other.c0);
        d0.set(other.d0);
    }

    public void change(Coefficient c, double dx, double dy)
    {
        switch(c)
        {
            case C0:
                c0.add_xy(dx,dy);
                break;
            case D0:
                d0.add_xy(dx,dy);
                break;
        }
    }
}
