package com.fractals;

import java.awt.geom.Ellipse2D;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FractalCircle --- Represents a Fractal2D created by drawing orbiting circles around a parent
 * 					 for a set number of iterations.
 * @author Scott Wolfskill
 * @created     02/14/2019
 * @last_edit   02/25/2019
 */
public class FractalCircle extends Fractal2D 
{
	protected int satelliteCount;   //Number of child node 'satellites' to generate per iteration
	protected double scalingFactor; //Scaling factor for the radius of each child node in the fractal
	
	private static double initialRadiusFactor = 0.20; //set initial radius to be 20% of min(usableWidth, usableHeight)'s value
	
	/**
	 * Creates a non-generated FractalCircle with specified parameters.
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param satellites Number of child node 'satellites' to generate per iteration.
	 * @param scalingFactor Scaling factor for the radius of each child node in the fractal.
	 * @param padding_horizontal Horizontal padding in the image to generate.
	 * @param padding_vertical Vertical padding in the image to generate.
	 */
	public FractalCircle(int width, int height, int iterations, int satelliteCount, 
					     double scalingFactor, int padding_horizontal, int padding_vertical)
	{
		initialize(width, height, iterations, padding_horizontal, padding_vertical);
		this.satelliteCount = satelliteCount;
		this.scalingFactor = scalingFactor;
	}
	
	@Override
	public boolean equals(Fractal2D other)
	{
		if(!super.equals(other)) //first, check basic comparison with Fractal2D.equals
		{
			return false;
		}
		try {
			FractalCircle o = (FractalCircle) other;
			if(satelliteCount != o.satelliteCount ||
			   scalingFactor != o.scalingFactor)
			{
				return false;
			}
		} catch (Exception e) {
			System.out.println("FractalTree.equals: Exception '" + e.getMessage() + "'.");
			return false;
		}
		return true;
	}
	
	/**
	 * Generates the FractalCircle on image by running for totalIterations iterations.
	 */
	@Override
	public void generate()
	{
		initImage();
		double usableWidth = width - 2 * padding_horizontal;
		double usableHeight = height - 2 * padding_horizontal;
		double centerX = width / 2;
		double centerY = height / 2;
		double startRadius = initialRadiusFactor * Math.min(usableWidth, usableHeight);
		iterate(centerX, centerY, startRadius, totalIterations);
	}
	
	private void iterate(double centerX, double centerY, double radius, int iterationsRemaining)
	{
		final String msgPrefix = "FractalCircle.iterate: ";
		if(cancelled != null && cancelled.get()) {
			return;
		}
		if(iterationsRemaining == 0) {
			return;
		}
		if(radius <= 0) {
			System.out.println(msgPrefix + " radius (" + radius + ") became too small! ");
			return;
		}
		//1. Draw parent circle at (centerX, centerY)
		drawEllipse(centerX, centerY, radius * 2, radius * 2);
		
		//2. Calculate info and call iterate on each child satellite recursively
		double childRadius = scalingFactor * radius;
		double radiiSum = radius + childRadius;
		
		for(int i = 0; i < satelliteCount; i++)
		{
			double childAngle_rad = (2 * Math.PI * i) / satelliteCount;
			double childCenterX = radiiSum * Math.cos(childAngle_rad) + centerX;
			double childCenterY = radiiSum * Math.sin(childAngle_rad) + centerY;
			iterate(childCenterX, childCenterY, childRadius, iterationsRemaining - 1);
		}
	}

}
