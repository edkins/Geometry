package io.pantheist.geometry;

import java.util.Objects;

public final class CxMut {
    private double a;
    private double b;

    private CxMut(double a, double b)
    {
        this.a = a;
        this.b = b;
    }

    public static CxMut of(double a, double b)
    {
        return new CxMut(a,b);
    }

    public static CxMut copy(CxMut other) { return new CxMut(other.a, other.b);}

    public double abs_squared()
    {
        return a * a + b * b;
    }

    public double distance_squared(CxMut other)
    {
        return (a - other.a) * (a - other.a) + (b - other.b) * (b - other.b);
    }

    public void set(CxMut other)
    {
        a = other.a;
        b = other.b;
    }

    public void set_xy(double x, double y)
    {
        a = x;
        b = y;
    }

    public void add(CxMut other)
    {
        a += other.a;
        b += other.b;
    }

    public void sub(CxMut other)
    {
        a -= other.a;
        b -= other.b;
    }

    public void scale(double x)
    {
        a *= x;
        b *= x;
    }

    public void mul(CxMut other)
    {
        double na = a * other.a - b * other.b;
        b = a * other.b + b * other.a;
        a = na;
    }

    public void mul_xy(double x, double y)
    {
        double na = a * x - b * y;
        b = a * y + b * x;
        a = na;
    }

    public void div(CxMut other)
    {
        double rr = other.abs_squared();
        double x = other.a / rr;
        double y = -other.b / rr;
        mul_xy(x, y);
    }

    @Override
    public String toString()
    {
        return String.format("%f%+fi", a, b);
    }

    public void add_xy(double x, double y) {
        a += x;
        b += y;
    }

    public void add_product(CxMut w, CxMut z)
    {
        double aa = w.a * z.a - w.b * z.b;
        double bb = w.a * z.b + w.b * z.a;
        a += aa;
        b += bb;
    }

    public boolean close_to(CxMut other)
    {
        if (abs_squared() > 4000 && other.abs_squared() > 4000) return true;
        return distance_squared(other) < 0.00025;
    }
}
