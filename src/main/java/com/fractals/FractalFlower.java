package com.fractals;

import java.awt.Color;

import javax.persistence.Embeddable;

/**
 * FractalFlower --- Represents a Fractal2D created by drawing flower petals as semicircles around a central circle.
 * @author Scott Wolfskill
 * @created     03/02/2019
 * @last_edit   03/12/2019
 */
@Embeddable
public class FractalFlower extends Fractal2D 
{
	protected int petalCount;       //Number of child node petals to generate per iteration
	protected double arcAngle;		//Angle (radians) that child node petal arcs should span (e.g. pi for semicircle)
	protected double scalingFactor;  	//Scaling factor for the radius of each child node in the fractal
	protected double scalingPower;   	//Exponent power to raise scalingFactor to
	
	private transient double raisedScalingFactor; //sqrt(scalingFactor^scalingPower)
	
	private static double initialRadiusFactor = 0.20; //set initial radius to be 20% of min(usableWidth, usableHeight)'s value
	
	public FractalFlower() {} //Embeddable must define default CTOR
	
	/**
	 * Creates a non-generated FractalCircle with specified parameters.
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param petalCount Number of child node petals to generate per iteration.
	 * @param arcAngle Angle (radians) that child node petal arcs should span (e.g. pi for semicircle)
	 * @param scalingFactor Scaling factor for the arc length of each child node in the fractal.
	 * @param padding_horizontal Horizontal padding in the image to generate.
	 * @param padding_vertical Vertical padding in the image to generate.
	 */
	public FractalFlower(int width, int height, int iterations, int petalCount, double arcAngle,
						 double scalingFactor, double scalingPower, int padding_horizontal, int padding_vertical)
	{
		initialize(width, height, iterations, padding_horizontal, padding_vertical);
		setPetalCount(petalCount);
		setArcAngle(arcAngle);
		setScalingFactor(scalingFactor);
		setScalingPower(scalingPower);
	}
	
	@Override
	public boolean equals(Fractal2D other)
	{
		if(!super.equals(other)) //first, check basic comparison with Fractal2D.equals
		{
			return false;
		}
		try {
			FractalFlower o = (FractalFlower) other;
			if(petalCount != o.petalCount ||
			   arcAngle != o.arcAngle ||
			   scalingFactor != o.scalingFactor ||
			   scalingPower != o.scalingPower)
			{
				return false;
			}
		} catch (Exception e) {
			System.out.println("FractalFlower.equals: " + e.getClass().getName() + " '" + e.getMessage() + "'.");
			return false;
		}
		return true;
	}
	
	/**
	 * Generates the FractalFlower on image by running for totalIterations iterations.
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
		double startArcAngle = 2 * Math.PI; //whole circle
		gfx.setColor(Color.white);
		iterate(centerX, centerY, startRadius, startArcAngle, 0, totalIterations);
	}
	
	private void iterate(double centerX, double centerY, double radius, double arcAngle, 
						 double rotation, int iterationsRemaining)
	{
		final String msgPrefix = "FractalFlower.iterate: ";
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
		//1. Draw parent arc around (centerX, centerY)
		double start = (arcAngle / -2.0f) + rotation; //start pt. of the arc on the circle (r, start) in polar coords
		drawArc(centerX, centerY, radius * 2, radius * 2, start, arcAngle);
		
		//2. Calculate info and call iterate on each child petal recursively
		double childArcAngle = this.arcAngle;
		double childRadius = raisedScalingFactor * radius;
		
		for(int i = 0; i < petalCount; i++)
		{
			double parentToChildStartAngle = Math.PI - (childArcAngle / 2); //mu
			double phi = Math.asin(childRadius * Math.sin(parentToChildStartAngle) / radius); //phi
			double parentToChildAngle = (childArcAngle / 2) - phi; //lambda: angle (radians) between (centerX, centerY) and (childCenterX, childCenterY)
			double distToChildCenter = radius * Math.sin(parentToChildAngle) / Math.sin(parentToChildStartAngle); //u distance between (centerX, centerY) and (childCenterX, childCenterY)
			double childRotation;
			if(petalCount > 1) {
				childRotation = (arcAngle / -2.0f + arcAngle / (2*petalCount)) + (i * arcAngle / petalCount) + rotation;
			} else {
				childRotation = rotation;
			}
			
			double childCenterX = distToChildCenter * Math.cos(childRotation) + centerX;
			double childCenterY = distToChildCenter * Math.sin(-1.0f * childRotation) + centerY;
			iterate(childCenterX, childCenterY, childRadius, childArcAngle, childRotation, iterationsRemaining - 1);
		}
	}
	
	private void setRaisedScalingFactor()
	{
		raisedScalingFactor = Math.pow(scalingFactor, scalingPower / 2);
	}
	
	public int getPetalCount()
	{
		return petalCount;
	}
	
	public void setPetalCount(int petalCount)
	{
		this.petalCount = petalCount;
	}
	
	public double getArcAngle()
	{
		return arcAngle;
	}
	
	public void setArcAngle(double arcAngle)
	{
		this.arcAngle = arcAngle;
	}
	
	public double getScalingFactor()
	{
		return scalingFactor;
	}
	
	public void setScalingFactor(double scalingFactor)
	{
		this.scalingFactor = scalingFactor;
		setRaisedScalingFactor();
	}
	
	public double getScalingPower()
	{
		return scalingPower;
	}
	
	public void setScalingPower(double scalingPower)
	{
		this.scalingPower = scalingPower;
		setRaisedScalingFactor();
	}

}
