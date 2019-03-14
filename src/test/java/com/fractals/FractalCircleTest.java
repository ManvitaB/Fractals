package com.fractals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FractalCircleTest --- Contains Tests for FractalCircle related to its equals method.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   03/13/2019
 */
public class FractalCircleTest 
{
	public static FractalCircle a;
	public static FractalCircle a_equals;
	public static FractalCircle[] b;
	public static FractalTree fractalTree;
	
	//Runs setup only once before the entire test fixture
	@BeforeClass
	public static void setUp()
	{
		a = new FractalCircle(1, 2, 3, 4, 5, 6, 7, 8, 9);
		a_equals = new FractalCircle(1, 2, 3, 4, 5, 6, 7, 8, 9);
		b = new FractalCircle[9];
		b[0] = new FractalCircle(100, 2, 3, 4, 5, 6, 7, 8, 9);
		b[1] = new FractalCircle(1, 200, 3, 4, 5, 6, 7, 8, 9);
		b[2] = new FractalCircle(1, 2, 634, 4, 5, 6, 7, 8, 9);
		b[3] = new FractalCircle(1, 2, 3, 600, 5, 6, 7, 8, 9);
		b[4] = new FractalCircle(1, 2, 3, 4, 500, 6, 7, 8, 9);
		b[5] = new FractalCircle(1, 2, 3, 4, 5, 600, 7, 8, 9);
		b[6] = new FractalCircle(1, 2, 3, 4, 5, 6, 700, 8, 9);
		b[7] = new FractalCircle(1, 2, 3, 4, 5, 6, 7, 800, 9);
		b[8] = new FractalCircle(1, 2, 3, 4, 5, 6, 7, 8, 900);
		fractalTree = new FractalTree(1, 2, 3, 4, 5, 6, 7, 8, 9);
	}
	
	@Test
	public void equalsTest_equality()
	{
		// 1. Equality by same reference
		assertTrue(a.equals(a));

		// 2. Equality by value, different reference
		assertTrue(a.equals(a_equals));
	}
	
	@Test
	public void equalsTest_inequality_differentType()
	{
		// 1. Inequality of non-null a with null
		assertFalse(a.equals(null));

		// 2. Inequality by type with a FractalTree (different type of Fractal2D)
		assertFalse(a.equals(fractalTree));
	}
	
	@Test
	public void equalsTest_inequality_sameType()
	{
		// Inequality by value with another FractalCircle; only one param differs
		for(FractalCircle other : b) {
			assertFalse(a.equals(other));
		}
	}
}
