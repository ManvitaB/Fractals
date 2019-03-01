package com.fractals.DB;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.fractals.Fractal2D;
import com.fractals.FractalTree;

/**
 * FractalTreeEntity --- A JPA Entity that holds Fractal2D database information for a FractalTree. 
 * @author Scott Wolfskill
 * @created     02/27/2019
 * @last_edit   02/28/2019
 */
@Entity
public class FractalTreeEntity extends Fractal2DEntity<FractalTree>
{	
	public FractalTreeEntity() {} //Entity must define default CTOR
	
	public FractalTreeEntity(FractalTree fractalTree) 
	{
		super(fractalTree);
		setFractal2D(fractalTree);
	}
	
	public FractalTree getFractal2D() 
	{
		return fractal2D;
	}
	
	public void setFractal2D(FractalTree fractal2D)
	{
		try {
			this.fractal2D = (FractalTree) fractal2D;
		} catch (Exception e) {
			System.out.println("FractalTreeEntity.setFractal2D: " + e.getClass().getName() + "'" + e.getMessage() + "'.");
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FractalTreeEntityRepository getRepository()
	{
		return DB.getFractalTreeEntities();
	}
}
