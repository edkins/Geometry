package io.pantheist.geometry;

import static org.junit.Assert.*;

/**
 * Created by giles on 12/08/17.
 */

public class Approx {

    private final double margin;

    private Approx(double margin)
    {
        this.margin = margin;
    }

    public static Approx within(double margin)
    {
        return new Approx(margin);
    }

    public void assertEqual(Complex z, Complex w)
    {
        assertTrue(String.format("%s should be near %s", z, w), z.sub(w).abs_squared() < margin * margin);
    }
}
