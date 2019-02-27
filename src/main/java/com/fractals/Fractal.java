package com.fractals;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

/**
 * Fractal --- Abstract class that represents a Fractal that is generated via
 * 			   a set number of iterations.
 * @author Scott Wolfskill
 * @created     02/12/2019
 * @last_edit   02/27/2019
 */
//@Embeddable
//@MappedSuperclass
public abstract class Fractal 
{
	protected int totalIterations; //total number of iterations to perform to generate the fractal
	
	//public Fractal() {} //Embeddable classes must define default CTOR
	
	public int getTotalIterations()
	{
		return totalIterations;
	}
	
	public void setTotalIterations(int totalIterations)
	{
		this.totalIterations = totalIterations;
	}
}
