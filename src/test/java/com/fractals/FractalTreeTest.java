package com.fractals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FractalTreeTest --- Contains Tests for FractalTree related to its equals method.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   03/13/2019
 */
public class FractalTreeTest 
{
	public static FractalTree a;
	public static FractalTree a_equals;
	public static FractalTree[] b;
	public static FractalCircle fractalCircle;
	
	//Runs setup only once before the entire test fixture
	@BeforeClass
	public static void setUp()
	{
		a = new FractalTree(1, 2, 3, 4, 5, 6, 7, 8, 9);
		a_equals = new FractalTree(1, 2, 3, 4, 5, 6, 7, 8, 9);
		b = new FractalTree[9];
		b[0] = new FractalTree(100, 2, 3, 4, 5, 6, 7, 8, 9);
		b[1] = new FractalTree(1, 200, 3, 4, 5, 6, 7, 8, 9);
		b[2] = new FractalTree(1, 2, 634, 4, 5, 6, 7, 8, 9);
		b[3] = new FractalTree(1, 2, 3, 600, 5, 6, 7, 8, 9);
		b[4] = new FractalTree(1, 2, 3, 4, 500, 6, 7, 8, 9);
		b[5] = new FractalTree(1, 2, 3, 4, 5, 600, 7, 8, 9);
		b[6] = new FractalTree(1, 2, 3, 4, 5, 6, 700, 8, 9);
		b[7] = new FractalTree(1, 2, 3, 4, 5, 6, 7, 800, 9);
		b[8] = new FractalTree(1, 2, 3, 4, 5, 6, 7, 8, 900);
		fractalCircle = new FractalCircle(1, 2, 3, 4, 5, 6, 7, 8, 9);
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

		// 2. Inequality by type with a FractalCircle (different type of Fractal2D)
		assertFalse(a.equals(fractalCircle));
	}
	
	@Test
	public void equalsTest_inequality_sameType()
	{
		// Inequality by value with another FractalTree; only one param differs
		for(FractalTree other : b) {
			assertFalse(a.equals(other));
		}
	}
}
