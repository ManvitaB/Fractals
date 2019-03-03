package com.fractals.DB;

import com.fractals.FractalFlower;

/**
 * FractalFlowerEntityRepository --- Interface extending Fractal2DEntityRepository for FractalFlowerEntity
 * 								     that will be implemented automatically by Spring
 * 								     into a Bean called fractalFlowerEntityRepository.
 * @author Scott Wolfskill
 * @created     03/02/2019
 * @last_edit   03/02/2019
 */
public interface FractalFlowerEntityRepository extends Fractal2DEntityRepository<FractalFlowerEntity, FractalFlower>
{
	/* Since parent interface Fractal2DEntityRepository has annotation '@NoRepositoryBean',
	 * to have Spring generate this repository automatically we needed to create this
	 * interface explicitly.
	 * 
	 * (as opposed to simply creating an autowired variable
	 * Fractal2DEntityRepository<FractalFlowerEntity> fractalFlowerEntities)
	 */ 
}
