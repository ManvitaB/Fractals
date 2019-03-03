package com.fractals;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.persistence.Embeddable;

/**
 * FractalTree --- Represents a Fractal2D Tree generated by creating two smaller children
 * 				   with a specified angle between them for each existing node every iteration.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   03/02/2019
 */
@Embeddable
public class FractalTree extends Fractal2D
{
	protected double angle;		      //angle (radians) created between existing line segment
	protected double scalingFactor;	  //factor to scale line segment length by each iteration
	
	private static double initialSegmentLengthFactor = 0.20; //set initial segmentLength to be 20% of usableHeight's value
	private static double initialAngle_deg = 90; //initial angle (degrees) of the 1st line segment
	
	public FractalTree() {} //Embeddable must define default CTOR
	
	/**
	 * Creates a non-generated FractalTree with specified parameters.
	 * @param width Width of the image to generate.
	 * @param height Height of the image to generate.
	 * @param iterations Number of fractal iterations to perform.
	 * @param angle Angle in radians between child nodes in the fractal.
	 * @param scalingFactor Scaling factor for each child node in the fractal.
	 * @param padding_horizontal Horizontal padding in the image to generate.
	 * @param padding_vertical Vertical padding in the image to generate.
	 */
	public FractalTree(int width, int height, int iterations, double angle, 
					   double scalingFactor, int padding_horizontal, int padding_vertical)
	{
		initialize(width, height, iterations, padding_horizontal, padding_vertical);
		setAngle(angle);
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
			FractalTree o = (FractalTree) other;
			if(angle != o.angle ||
			   scalingFactor != o.scalingFactor)
			{
				return false;
			}
		} catch (Exception e) {
			System.out.println("FractalTree.equals: " + e.getClass().getName() + " '" + e.getMessage() + "'.");
			return false;
		}
		return true;
	}
	
	/**
	 * Generates the FractalTree on image by running for totalIterations iterations.
	 */
	@Override
	public void generate()
	{
		//Set segmentLength to be a percent factor of the total usable height
		//(taking into account padding on both top and bottom)
		int usableHeight = height - 2 * padding_vertical;
		double segmentLength = initialSegmentLengthFactor * usableHeight;
		
		double startX = width / 2;
		double startY = height - padding_vertical;
		double endX = startX;
		double endY = startY - segmentLength;
		double startAngle = Math.toRadians(initialAngle_deg);
		initImage();
		/*gfx.setBackground(Color.DARK_GRAY); //update the color to set as background color when clearRect is called
		gfx.clearRect(0, 0, width, height); //set width x height region with background color only
		gfx.setColor(Color.blue);*/ //set paint color to use in future draw calls
		iterate(startX, startY, endX, endY, startAngle, segmentLength, totalIterations); //begin iterating (iterationsRemaining) times
	}
	
	private void iterate(double startX, double startY, double endX, double endY, 
						 double currAngle, double segmentLength, int iterationsRemaining)
	{
		final String msgPrefix = "FractalTree.iterate: ";
		if(cancelled != null && cancelled.get()) {
			return;
		}
		if(iterationsRemaining <= 0) {
			return;
		}
		if(segmentLength <= 0) {
			System.out.println(msgPrefix + "segmentLength (" + segmentLength + ") became too small! ");
			return;
		}
		//1. Draw single parent line segment
		drawLine(startX, startY, endX, endY);
		
		//2. Calculate endpoints of left and right child nodes
		double childSegmentLength = scalingFactor * segmentLength;
		double lchild_angle = currAngle + angle / 2;
		double lchild_endX = endX + childSegmentLength * Math.cos(lchild_angle);
		double lchild_endY = endY - childSegmentLength * Math.sin(lchild_angle);
		
		double rchild_angle = currAngle - angle / 2;
		double rchild_endX = endX + childSegmentLength * Math.cos(rchild_angle);
		double rchild_endY = endY - childSegmentLength * Math.sin(rchild_angle);
		
		//3. Call iterate on each child recursively
		iterate(endX, endY, lchild_endX, lchild_endY, lchild_angle, childSegmentLength, iterationsRemaining - 1); //left child
		iterate(endX, endY, rchild_endX, rchild_endY, rchild_angle, childSegmentLength, iterationsRemaining - 1); //right child
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setAngle(double angle)
	{
		this.angle = angle;
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
