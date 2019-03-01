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
 * @last_edit   02/28/2019
 */
@Entity
public class FractalCircleEntity extends Fractal2DEntity<FractalCircle>
{	
	public FractalCircleEntity() {} //Entity must define default CTOR
	
	public FractalCircleEntity(FractalCircle fractalCircle) 
	{
		super(fractalCircle);
	}
	
	@Override
	public FractalCircle getFractal2D()
	{
		return fractal2D;
	}
	
	@Override
	public void setFractal2D(FractalCircle fractal2D)
	{
		try {
			this.fractal2D = (FractalCircle) fractal2D;
		} catch (Exception e) {
			System.out.println("FractalCircleEntity.setFractal2D: " + e.getClass().getName() + "'" + e.getMessage() + "'.");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FractalCircleEntityRepository getRepository()
	{
		return DB.getFractalCircleEntities();
	}
	
	
}
