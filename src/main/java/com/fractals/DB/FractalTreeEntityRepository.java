package com.fractals.DB;

import org.springframework.data.repository.CrudRepository;

/**
 * FractalTreeEntityRepository --- Interface extending Fractal2DEntityRepository for FractalTreeEntity
 * 								   that will be implemented automatically by Spring
 * 								   into a Bean called fractalTreeEntityRepository.
 * @author Scott Wolfskill
 * @created     02/28/2019
 * @last_edit   02/28/2019
 */
public interface FractalTreeEntityRepository extends Fractal2DEntityRepository<FractalTreeEntity>
{
	/* Since parent interface Fractal2DEntityRepository has annotation '@NoRepositoryBean',
	 * to have Spring generate this repository automatically we needed to create this
	 * interface explicitly.
	 * 
	 * (as opposed to simply creating an autowired variable
	 * Fractal2DEntityRepository<FractalTreeEntity> fractalTreeEntities)
	 */ 
}
