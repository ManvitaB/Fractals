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
 * @last_edit   02/27/2019
 */
@Entity
public class FractalTreeEntity extends Fractal2DEntity
{
	@Embedded
	private FractalTree fractalTree;
	
	public FractalTreeEntity() {} //Entity must define default CTOR
	
	public FractalTreeEntity(FractalTree fractalTree, String imageSrc, String loadingMessage, 
							 Date expirationDate, Boolean generationComplete) 
	{
		super(imageSrc, loadingMessage, expirationDate, generationComplete);
		setFractalTree(fractalTree);
	}
	
	public Fractal2D getFractalTree() 
	{
		return fractalTree;
	}
	
	public void setFractalTree(FractalTree fractalTree)
	{
		this.fractalTree = fractalTree;
	}
}
