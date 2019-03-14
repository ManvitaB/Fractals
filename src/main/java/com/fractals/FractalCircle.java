package com.fractals;

import javax.persistence.Embeddable;

/**
 * FractalCircle --- Represents a Fractal2D created by drawing orbiting circles around a parent
 * 					 for a set number of iterations.
 * @author Scott Wolfskill
 * @created     02/14/2019
 * @last_edit   03/13/2019
 */
@Embeddable
public class FractalCircle extends Fractal2D 
{
	protected int satelliteCount;   //Number of child node 'satellites' to generate per iteration
	protected double scalingFactor; //Scaling factor for the radius of each child node in the fractal
		
	public FractalCircle() {} //Embeddable must define default CTOR
	
	/**
	 * Creates a non-generated FractalCircle with specified parameters.
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param satelliteCount Number of child node 'satellites' to generate per iteration.
	 * @param scalingFactor Scaling factor for the radius of each child node in the fractal.
	 * @param zoomFactor Multiplier to scale start element by relative to usable width & height.
	 * @param rotation Global rotation in radians of the FractalCircle, starting from 3 o'clock CCW. 
	 * @param padding_horizontal Horizontal padding in the image to generate.
	 * @param padding_vertical Vertical padding in the image to generate.
	 */
	public FractalCircle(int width, int height, int iterations, int satelliteCount, double scalingFactor,
					     double zoomFactor, double rotation, int padding_horizontal, int padding_vertical)
	{
		initialize(width, height, iterations, zoomFactor, rotation, padding_horizontal, padding_vertical);
		setSatelliteCount(satelliteCount);
		setScalingFactor(scalingFactor);
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
			System.out.println("FractalCircle.equals: " + e.getClass().getName() + " '" + e.getMessage() + "'.");
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
		double startRadius = zoomFactor * Math.min(usableWidth, usableHeight);
		iterate(centerX, centerY, startRadius, totalIterations);
	}
	
	private void iterate(double centerX, double centerY, double radius, int iterationsRemaining)
	{
		final String msgPrefix = "FractalCircle.iterate: ";
		if(cancelled != null && cancelled.get()) {
			return;
		}
		if(iterationsRemaining <= 0) {
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
			double childAngle_rad = (2 * Math.PI * i) / satelliteCount + rotation;
			double childCenterX = radiiSum * Math.cos(childAngle_rad) + centerX;
			double childCenterY = radiiSum * Math.sin(childAngle_rad) + centerY;
			iterate(childCenterX, childCenterY, childRadius, iterationsRemaining - 1);
		}
	}
	
	public int getSatelliteCount()
	{
		return satelliteCount;
	}
	
	public void setSatelliteCount(int satelliteCount)
	{
		this.satelliteCount = satelliteCount;
	}
	
	public double getScalingFactor()
	{
		return scalingFactor;
	}
	
	public void setScalingFactor(double scalingFactor)
	{
		this.scalingFactor = scalingFactor;
	}

}
