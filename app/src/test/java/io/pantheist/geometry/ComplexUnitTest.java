package io.pantheist.geometry;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by giles on 12/08/17.
 */

public class ComplexUnitTest {
    @Test
    public void testAdd() {
        assertEquals(Complex.of(1, 2).add(Complex.of(3, 4)), Complex.of(4, 6));
    }

    @Test
    public void testSub() {
        assertEquals(Complex.of(1, 2).sub(Complex.of(3, 7)), Complex.of(-2,-5));
    }

    @Test
    public void testScale() {
        assertEquals(Complex.of(1,2).scale(10), Complex.of(10,20));
    }

    @Test
    public void testMulReal() {
        assertEquals(Complex.of(1,2).mul(Complex.real(10)), Complex.of(10,20));
    }

    @Test
    public void testMulOne() {
        assertEquals(Complex.of(123,234).mul(Complex.one), Complex.of(123,234));
    }
    @Test
    public void testMulZero() {
        assertEquals(Complex.of(123,234).mul(Complex.zero), Complex.of(0,0));
    }

    @Test
    public void testMulI() {
        assertEquals(Complex.of(123,234).mul(Complex.i), Complex.of(-234,123));
    }

    @Test
    public void testMulConj() {
        Complex z = Complex.of(1.5,-6.25);
        assertEquals(z.mul(z.conj()), Complex.real(z.abs_squared()));
    }

    @Test
    public void testRecipReal() {
        assertEquals(Complex.real(4).recip(), Complex.real(0.25));
    }

    @Test
    public void testRecipI() {
        assertEquals(Complex.i.recip(), Complex.i.neg());
    }

    @Test
    public void testRecipMul() {
        Complex z = Complex.of(1.5,-6.25);
        Approx.within(0.00001).assertEqual(z.mul(z.recip()), Complex.one);
    }

    @Test
    public void testDivReal() {
        assertEquals(Complex.of(3,4).div(Complex.real(2)), Complex.of(1.5,2));
    }

    @Test
    public void testDivI() {
        assertEquals(Complex.of(3,4).div(Complex.i), Complex.of(4,-3));
    }
}