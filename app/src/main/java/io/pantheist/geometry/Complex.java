package io.pantheist.geometry;

import java.util.Objects;

/**
 * Created by giles on 12/08/17.
 */

public final class Complex implements IComplex {
    private final double a;
    private final double b;

    private Complex(double a, double b)
    {
        this.a = a;
        this.b = b;
    }

    public double x()
    {
        return a;
    }
    public double y()
    {
        return b;
    }

    public static Complex of(double a, double b)
    {
        return new Complex(a,b);
    }

    public static Complex real(double x)
    {
        return new Complex(x,0);
    }

    public static final Complex zero = new Complex(0,0);
    public static final Complex one = new Complex(1,0);
    public static final Complex i = new Complex(0,1);

    public double abs_squared()
    {
        return a * a + b * b;
    }

    public Complex add(Complex other)
    {
        return new Complex(a + other.a, b + other.b);
    }

    public Complex sub(IComplex other)
    {
        return new Complex(a - other.x(), b - other.y());
    }

    public Complex scale(double x)
    {
        return new Complex(x * a, x * b);
    }

    public Complex neg()
    {
        return new Complex(-a, -b);
    }

    public Complex mul(Complex other)
    {
        return new Complex(a * other.a - b * other.b, a * other.b + b * other.a);
    }

    public Complex conj()
    {
        return new Complex(a, -b);
    }

    public Complex recip()
    {
        double x = abs_squared();
        return new Complex(a/x, -b/x);
    }

    public Complex div(Complex other)
    {
        return mul(other.recip());
    }

    public double manhattan()
    {
        return Math.abs(a) + Math.abs(b);
    }

    public Complex sqrt()
    {
        double root_half = Math.sqrt(0.5);
        double r = Math.sqrt(abs_squared());
        double signb = Math.signum(b);

        return Complex.of(
                root_half * Math.sqrt(r + a),
                root_half * signb * Math.sqrt(r - a)
        );

    }

    public Complex squared()
    {
        return mul(this);
    }

    public static Complex solve_quadratic(Complex a, Complex b, Complex c)
    {
        return b.squared().sub(a.mul(c).scale(4)).sqrt().sub(b).div(a).scale(0.5);
    }

    public static Complex solve_quadratic2(Complex a, Complex b, Complex c)
    {
        return b.squared().sub(a.mul(c).scale(4)).sqrt().neg().sub(b).div(a).scale(0.5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complex complex = (Complex) o;

        return a == complex.a && b == complex.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString()
    {
        return String.format("%f%+fi", a, b);
    }
}
