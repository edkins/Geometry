package io.pantheist.geometry;

public class Coefficients {
    public CxMut c0 = CxMut.of(0.25,0.03);
    public CxMut c1 = CxMut.of(0.0,0.0);
    public CxMut c2 = CxMut.of(1.0,0.0);
    public CxMut d0 = CxMut.of(1.0,0.0);
    public CxMut d1 = CxMut.of(0.0,0.0);
    public CxMut d2 = CxMut.of(0.0,0.0);

    public enum Coefficient
    {
        C0,C1,C2,D0,D1,D2;
    }

    public void set(Coefficients other)
    {
        c0.set(other.c0);
        c1.set(other.c1);
        c2.set(other.c2);
        d0.set(other.d0);
        d1.set(other.d1);
        d2.set(other.d2);
    }

    @Override
    public String toString()
    {
        return String.format("%s %s %s / %s %s %s", c0,c1,c2, d0, d1, d2);
    }

    public void translate(CxMut d)
    {
        /*
        a(x+d)^2 + b(x+d) + c - d
      = ax^2 + bx + c   + 2dax + add + bd - d
         */
        CxMut dd = CxMut.copy(d);
        dd.mul(d);

        c0.add_product(dd, c2);  //add
        c0.add_product(d, c1);   //bd
        c0.sub(d);
        c1.add_product(d,c2);    //da
        c1.add_product(d,c2);    //da
    }

    public void scale(CxMut d)
    {
        c0.mul(d);
        c2.div(d);
    }

    public void change(Coefficient c, double dx, double dy)
    {
        System.out.println(String.format("%s: move %.2f %.2f",c,dx,dy));
        switch(c)
        {
            case C0:
                c0.add_xy(dx,dy);
                break;
            case C1:
                c1.add_xy(dx,dy);
                break;
            case C2:
                c2.add_xy(dx,dy);
                break;
            case D0:
                d0.add_xy(dx,dy);
                break;
            case D1:
                d1.add_xy(dx,dy);
                break;
            case D2:
                d2.add_xy(dx,dy);
                break;

        }
    }
}
