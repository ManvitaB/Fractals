package com.fractals;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

/**
 * Fractal2D --- Abstract class that represents a 2D Fractal
 * 				 that is generated on an image and can be saved to the disk.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   02/18/2019
 */
public abstract class Fractal2D extends Fractal 
{
	public AtomicBoolean cancelled;   //thread-safe boolean. If true, current generation or writing to disk will be cancelled.
	
	protected int width;			  //width  of fractal image to generate
	protected int height;			  //height of fractal image to generate
	protected int padding_horizontal; //padding (px) for left/right sides of generated image
	protected int padding_vertical;   //padding (px) for top/bottom sides of generated image
	protected BufferedImage image;    //image to draw the fractal onto
	protected Graphics2D gfx;		  //Graphics of image
	
	/**
	 * Method for derived classes to implement to generate the fractal,
	 * running for totalIterations iterations.
	 */
	public abstract void generate();
	
	protected void initialize(int width, int height, int iterations,
							  int padding_horizontal, int padding_vertical)
	{
		this.width = width;
		this.height = height;
		this.totalIterations = iterations;
		this.padding_horizontal = padding_horizontal;
		this.padding_vertical = padding_vertical;
		image = null;
		gfx = null;
		cancelled = new AtomicBoolean(false);
	}
	
	/**
	 * Output this generated FractalTree to a location on disk as an image.
	 * @param relativePath Directory relative to this classpath to create the file.
	 * @param filename Name of the file to create in directory relativePath.
	 * @param imageType Type of image to create (e.g. "png")
	 * @return Absolute path of the image file created on disk.
	 * @throws Exception If I/O error occurred, or if cancelled.
	 */
	public String outputToFile(String relativePath, String filename, String imageType) throws Exception
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
		
		//2. Create output file if doesn't exist already
		File outputfile = new File(classpath.getPath() + relativePath + filename);
		String fullpath = outputfile.getAbsolutePath();
		if(!outputfile.exists()) {
			System.out.println(msgPrefix + "attempt to create new file at path '" + outputfile.getAbsolutePath() + "'");
			outputfile.createNewFile();
			System.out.println(msgPrefix + "created new file successfully.");
		} else {
			System.out.println(msgPrefix + "overwriting existing file at '" + outputfile.getAbsolutePath() + "'");
		}
		
		//3. Write image to file (if operation not cancelled)
		if(cancelled != null && cancelled.get()) {
			//System.out.println(msgPrefix + "cancelled!");
			throw new CancellationException("Image writing to disk was cancelled.");
		}
		ImageIO.write(image, imageType, outputfile);
		return fullpath;
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
}
