package com.fractals.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * DB --- Bean for managing Fractal2DEntity repositories and interacting with the database.
 * 
 * @author Scott Wolfskill
 * @created     02/28/2019
 * @last_edit   02/28/2019
 */
@Component
public class DB 
{
	private static DB db = null;
	
	@Autowired
	private FractalTreeEntityRepository fractalTreeEntities;
	@Autowired
	private FractalCircleEntityRepository fractalCircleEntities;
	
	private DB() {}
	
	public static DB getInstance()
	{
		if(db == null)
		{
			db = new DB();
		}
		return db;
	}
	
	public static FractalTreeEntityRepository getFractalTreeEntities()
	{
		DB db = getInstance();
		if(db.fractalTreeEntities == null)
		{
			System.out.println("DB WARNING: fractalTreeEntities was null!");
		}
		return db.fractalTreeEntities;
	}
	
	public static FractalCircleEntityRepository getFractalCircleEntities()
	{
		DB db = getInstance();
		if(db.fractalCircleEntities == null)
		{
			System.out.println("DB WARNING: fractalCircleEntities was null!");
		}
		return db.fractalCircleEntities;
	}
}
