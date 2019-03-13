package com.fractals;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * Fractal2D --- Abstract class that represents a 2D Fractal
 * 				 that is generated on an image and can be saved to the disk.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   03/12/2019
 */
@MappedSuperclass
public abstract class Fractal2D extends Fractal 
{
	//transient keyword denotes won't be serialized or stored in DB
	public transient AtomicBoolean cancelled;   //thread-safe boolean. If true, current generation or writing to disk will be cancelled.
	
	protected int width;			  //width  of fractal image to generate
	protected int height;			  //height of fractal image to generate
	protected int padding_horizontal; //padding (px) for left/right sides of generated image
	protected int padding_vertical;   //padding (px) for top/bottom sides of generated image
	protected transient BufferedImage image;    //image to draw the fractal onto
	protected transient Graphics2D gfx;		  //Graphics of image
	
	/**
	 * Method for derived classes to implement to generate the fractal,
	 * running for totalIterations iterations.
	 */
	public abstract void generate();
	
	/**
	 * Determine if this Fractal2D has equivalent Fractal2D-scope-only variables to other.
	 * (to be completely equal, equals must return true too in the concrete derived class)
	 * @param other Fractal2D to compare this Fractal2D to.
	 * @return true if equivalent in Fractal2D-scope only, false otherwise.
	 */
	public boolean equals(Fractal2D other)
	{
		if(other == null) {
			return false;
		}
		if(totalIterations != other.totalIterations ||
		   width != other.width ||
		   height != other.height ||
		   padding_horizontal != other.padding_horizontal ||
		   padding_vertical != other.padding_vertical) 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Output this generated FractalTree to a location on disk as an image,
	 * and output a generation message in the same directory at relativePath.
	 * @param relativePath Directory relative to this classpath to create the file.
	 * @param filename Name of the file to create in directory relativePath.
	 * @param imageType Type of image to create (e.g. "png")
	 * @return Absolute path of the image file created on disk.
	 * @throws Exception If I/O error occurred, or if cancelled.
	 */
	public String outputToFile(String relativePath, String imageFilename, String imageType) throws Exception
	{
		final String msgPrefix = "Fractal2D.outputToFile: ";
		if(cancelled != null && cancelled.get()) {
			//System.out.println(msgPrefix + "cancelled!");
			throw new CancellationException("Image writing to disk was cancelled.");
		}
		
		ClassLoader classLoader = getClass().getClassLoader();
		URL classpath = classLoader.getResource(".");
		File outputDirectory = new File(classpath.getPath() + relativePath);
		
		//1. Create output directory if doesn't exist already
		if(!outputDirectory.exists()) {
			System.out.println(msgPrefix + "attempting to create non-existant directory '" + outputDirectory.getAbsolutePath() + "'");
			if(outputDirectory.mkdirs()) {
				System.out.println(msgPrefix + "created new directory successfully.");
			} else {
				throw new Exception(msgPrefix + "could not make directory at '" + outputDirectory.getAbsolutePath() + "'.");
			}
		}
		
		//2. Create output image file and message file if any don't exist already
		String fullRelativePath = classpath.getPath() + relativePath;
		File outputImageFile = new File(fullRelativePath + imageFilename);
		String fullpath = outputImageFile.getAbsolutePath();
		if(!outputImageFile.exists()) {
			System.out.println(msgPrefix + "attempt to create new file at path '" + outputImageFile.getAbsolutePath() + "'");
			outputImageFile.createNewFile();
			System.out.println(msgPrefix + "created new file successfully.");
		} else {
			System.out.println(msgPrefix + "overwriting existing file at '" + outputImageFile.getAbsolutePath() + "'");
		}
		
		//3. Write image to file (if operation not cancelled)
		if(cancelled != null && cancelled.get()) {
			//System.out.println(msgPrefix + "cancelled!");
			throw new CancellationException("Image writing to disk was cancelled.");
		}
		ImageIO.write(image, imageType, outputImageFile);
		return fullpath;
	}
	
	protected void initialize(int width, int height, int iterations, int padding_horizontal, int padding_vertical) 
	{
		this.width = width;
		this.height = height;
		this.totalIterations = iterations;
		this.padding_horizontal = padding_horizontal;
		this.padding_vertical = padding_vertical;
		image = null;
		gfx = null;
		cancelled = new AtomicBoolean(false);
		if (totalIterations <= 0) {
			System.out.println("WARNING: Fractal2D initialized with non-positive iterations (" + totalIterations + ")!");
		}
	}
	
	protected void initImage()
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		gfx = image.createGraphics();
	}
	
	protected void drawLine(double startX, double startY, double endX, double endY)
	{
		Line2D line = new Line2D.Double(startX, startY, endX, endY);
		gfx.draw(line);
	}
	
	protected void drawEllipse(double centerX, double centerY, double width, double height)
	{
		//Ellipse2D.Double constructor generates ellipse 
		//whose leftmost point is at minX and whose topmost point is at minY
		double minX = centerX - (width / 2);
		double minY = centerY - (height / 2);
		Ellipse2D.Double ellipse = new Ellipse2D.Double(minX, minY, width, height);
		gfx.draw(ellipse);
	}
	
	protected void drawArc(double centerX, double centerY, double width, double height, double startAngle, double arcAngle)
	{
		int startAngle_deg = Math.round((float) Math.toDegrees(startAngle));
		int arcAngle_deg = Math.round((float) Math.toDegrees(arcAngle));
		int minX = Math.round((float) (centerX - (width / 2)));
		int minY = Math.round((float) (centerY - (height / 2)));
		int width_int = Math.round((float) width);
		int height_int = Math.round((float) height);
		gfx.drawArc(minX, minY, width_int, height_int, startAngle_deg, arcAngle_deg);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getPadding_horizontal()
	{
		return padding_horizontal;
	}
	
	public void setPadding_horizontal(int padding_horizontal)
	{
		this.padding_horizontal = padding_horizontal;
	}
	
	public int getPadding_vertical()
	{
		return padding_vertical;
	}
	
	public void setPadding_vertical(int padding_vertical)
	{
		this.padding_vertical = padding_vertical;
	}
}
