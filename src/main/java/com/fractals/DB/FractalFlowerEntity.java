package com.fractals.DB;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.fractals.Fractal2D;
import com.fractals.FractalFlower;

/**
 * FractalFlowerEntity --- A JPA Entity that holds Fractal2D database information for a FractalFlower. 
 * @author Scott Wolfskill
 * @created     03/02/2019
 * @last_edit   03/02/2019
 */
@Entity
public class FractalFlowerEntity extends Fractal2DEntity<FractalFlower>
{	
	public FractalFlowerEntity() {} //Entity must define default CTOR
	
	public FractalFlowerEntity(FractalFlower fractalFlower) 
	{
		super(fractalFlower);
		setFractal2D(fractalFlower);
	}
	
	public FractalFlower getFractal2D() 
	{
		return fractal2D;
	}
	
	public void setFractal2D(FractalFlower fractal2D)
	{
		try {
			this.fractal2D = (FractalFlower) fractal2D;
		} catch (Exception e) {
			System.out.println("FractalFlowerEntity.setFractal2D: " + e.getClass().getName() + "'" + e.getMessage() + "'.");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FractalFlowerEntityRepository defaultRepository()
	{
		return DB.getFractalFlowerEntities();
	}
}
