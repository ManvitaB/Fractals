package com.fractals.DB;

import org.springframework.data.repository.CrudRepository;

//This will be AUTO IMPLEMENTED by Spring into a Bean called fractal2DEntryRepository
//CRUD refers Create, Read, Update, Delete
public interface FractalCircleEntityRepository extends CrudRepository<FractalCircleEntity, Integer>
{
	
}
