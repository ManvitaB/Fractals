package com.fractals;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fractals.DB.DB;
import com.fractals.DB.Fractal2DEntityRepository;


@Configuration
@ComponentScan(basePackageClasses = Fractal2DEntityRepository.class)
public class BeanConfig 
{	
	@Bean
	public DB db()
	{
		return DB.getInstance(); 
	}
}
