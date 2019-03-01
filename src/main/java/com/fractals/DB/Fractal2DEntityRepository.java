package com.fractals.DB;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.fractals.Fractal2D;

/**
 * Fractal2DEntityRepository --- Interface representing a Repository of a concrete class extending
 * 								 Fractal2DEntity, whose methods are implemented automatically by Spring.
 * @author Scott Wolfskill
 * @created     02/28/2019
 * @last_edit   02/28/2019
 */
@NoRepositoryBean
public interface Fractal2DEntityRepository<T extends Fractal2DEntity> extends CrudRepository<T, Integer>//PagingAndSortingRepository<T, Long>
{
	
}
