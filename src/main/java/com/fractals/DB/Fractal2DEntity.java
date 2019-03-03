package com.fractals.DB;

import java.sql.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fractals.Fractal2D;

/**
 * Fractal2DEntity --- A JPA MappedSuperclass that holds Fractal2D database information.
 * 					   Derived classes should add field(s) for their respective concrete Fractal2D type.
 * @author Scott Wolfskill
 * @created     02/27/2019
 * @last_edit   03/02/2019
 */
@MappedSuperclass
public abstract class Fractal2DEntity<T extends Fractal2D>
{
	public static final String generatingLoadingMessage = "Generating...";
	private static final String defaultFractalImageDirectory = "images/";
	private static final long defaultExpirationPeriod = 10L * 1000L;	//default expiration period in milliseconds
	private static Long idCount = 0L;
	
	@Id
    private Long id;					//(note: all child classes will share the same id pool)
	@Embedded
	protected T fractal2D;
	private String imageSrc; 			//relative path of the generated fractal2D image
    private String loadingMessage; 		//message displayed to client about generation status
    private Date expirationDate; 		//date that this entry should be deleted from the DB, and the image off the disk.
    private Boolean generationComplete; //false => in-progress; true => finished.
    private Long generationTime;		//time spent generating the fractal2D in milliseconds.
    
    public Fractal2DEntity() {} //MappedSuperclass must define default CTOR
    
	public Fractal2DEntity(T fractal2D)
	{
		setId(idCount);
		idCount++;
		setFractal2D(fractal2D);
		setDefaultImageSrc();
		setGeneratingLoadingMessage();
		setDefaultExpirationDate();
		setGenerationComplete(false);
		setGenerationTime(0L);
	}
	
	/**
	 * Sets the global ID counter to a new value.
	 * Intended to be used on app startup if there is existing data in the database,
	 * to make sure we never assign the same ID twice.
	 * @param idCount New value to set static idCount to.
	 */
	public static void setIdCount(Long idCount)
	{
		Fractal2DEntity.idCount = idCount;
	}
	
	/**
	 * Sets imageSrc with the default scheme, [defaultFractalImageDirectory][fractal2D class name]#[id].png
	 * e.g. images/FractalTree#5.png
	 */
	public void setDefaultImageSrc()
    {
    	setImageSrc(defaultFractalImageDirectory + fractal2D.getClass().getSimpleName() + "_" + id + ".png");
    }
	
	public void setDefaultExpirationDate()
	{
		Long now = new java.util.Date().getTime(); //current time in milliseconds
		java.util.Date expirationDate = new java.util.Date(now + defaultExpirationPeriod);
		setExpirationDate(new Date(expirationDate.getTime()));
	}
	
	public void setGeneratingLoadingMessage()
	{
		setLoadingMessage(generatingLoadingMessage);
	}
	
	/**
	 * Gets the default repository of a class extending Fractal2DEntity<E, T> 
	 * where E is the derived class of Fractal2DEntity implementing the method.
	 * @return R class extending Fractal2DEntityRepository<E, T>
	 */
	public abstract <R extends Fractal2DEntityRepository<E, T>, E extends Fractal2DEntity<T>> R defaultRepository();
	
	public abstract T getFractal2D();
	
	public abstract void setFractal2D(T fractal2D);
    
	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
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
	
	public Long getGenerationTime() 
	{
		return generationTime;
	}

	public void setGenerationTime(Long generationTime) 
	{
		this.generationTime = generationTime;
	}
}
