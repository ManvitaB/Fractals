package com.fractals.DB;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.fractals.Fractal2D;
import com.fractals.FractalCircle;

/**
 * FractalCircleEntity --- A JPA Entity that holds Fractal2D database information for a FractalCircle. 
 * @author Scott Wolfskill
 * @created     02/27/2019
 * @last_edit   02/27/2019
 */
@Entity
public class FractalCircleEntity extends Fractal2DEntity
{
	@Embedded
	private FractalCircle fractalCircle;
	
	public FractalCircleEntity() {} //Entity must define default CTOR
	
	public FractalCircleEntity(FractalCircle fractalCircle, String imageSrc, String loadingMessage, 
							 Date expirationDate, Boolean generationComplete) 
	{
		super(imageSrc, loadingMessage, expirationDate, generationComplete);
		setFractalCircle(fractalCircle);
	}
	
	public Fractal2D getFractalCircle() 
	{
		return fractalCircle;
	}
	
	public void setFractalCircle(FractalCircle fractalCircle)
	{
		this.fractalCircle = fractalCircle;
	}
}
