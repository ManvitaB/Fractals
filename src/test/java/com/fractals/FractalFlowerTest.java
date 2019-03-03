package com.fractals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * FractalFlowerTest --- Contains Tests for FractalFlower related to its equals method.
 * @author Scott Wolfskill
 * @created     03/02/2019
 * @last_edit   03/02/2019
 */
public class FractalFlowerTest 
{
	public static FractalFlower a;
	public static FractalFlower a_equals;
	public static FractalFlower[] b;
	public static FractalCircle fractalCircle;
	
	//Runs setup only once before the entire test fixture
	@BeforeClass
	public static void setUp()
	{
		a = new FractalFlower(1, 2, 3, 4, 5, 6, 7, 8);
		a_equals = new FractalFlower(1, 2, 3, 4, 5, 6, 7, 8);
		b = new FractalFlower[8];
		b[0] = new FractalFlower(100, 2, 3, 4, 5, 6, 7, 8);
		b[1] = new FractalFlower(1, 200, 3, 4, 5, 6, 7, 8);
		b[2] = new FractalFlower(1, 2, 334, 4, 5, 6, 7, 8);
		b[3] = new FractalFlower(1, 2, 3, 600, 5, 6, 7, 8);
		b[4] = new FractalFlower(1, 2, 3, 4, 500, 6, 7, 8);
		b[5] = new FractalFlower(1, 2, 3, 4, 5, 600, 7, 8);
		b[6] = new FractalFlower(1, 2, 3, 4, 5, 6, 700, 8);
		b[7] = new FractalFlower(1, 2, 3, 4, 5, 6, 7, 800);
		fractalCircle = new FractalCircle(1, 2, 3, 4, 5, 6, 7);
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
		// Inequality by value with another FractalFlower; only one param differs
		for(FractalFlower other : b) {
			assertFalse(a.equals(other));
		}
	}
}
