package com.fractals.DB;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Fractal2DEntity --- A JPA MappedSuperclass that holds Fractal2D database information.
 * 					   Derived classes should add field(s) for their respective concrete Fractal2D type.
 * @author Scott Wolfskill
 * @created     02/27/2019
 * @last_edit   02/27/2019
 */
@MappedSuperclass
public abstract class Fractal2DEntity 
{
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;					//(note: all child classes will share the same id pool)
    private String imageSrc; 			//relative path of the generated fractal2D image
    private String loadingMessage; 		//message displayed to client about generation status
    private Date expirationDate; 		//date that this entry should be deleted from the DB, and the image off the disk.
    private Boolean generationComplete; //false => in-progress; true => finished.
    
    public Fractal2DEntity() {} //MappedSuperclass must define default CTOR
    
	public Fractal2DEntity(String imageSrc, String loadingMessage, Date expirationDate, Boolean generationComplete)
	{
		setImageSrc(imageSrc);
		setLoadingMessage(loadingMessage);
		setExpirationDate(expirationDate);
		setGenerationComplete(generationComplete);
	}
    
	public Integer getId() 
	{
		return id;
	}

	public void setId(Integer id) 
	{
		this.id = id;
	}
	
	public String getImageSrc() 
	{
		return imageSrc;
	}
	
	public void setImageSrc(String imageSrc)
	{
		this.imageSrc = imageSrc;
	}
	
	public String getLoadingMessage() 
	{
		return loadingMessage;
	}
	
	public void setLoadingMessage(String loadingMessage)
	{
		this.loadingMessage = loadingMessage;
	}
	
	public Date getExpirationDate() 
	{
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}
	
	public Boolean getGenerationComplete() 
	{
		return generationComplete;
	}
	
	public void setGenerationComplete(Boolean generationComplete)
	{
		this.generationComplete = generationComplete;
	}
}
