package com.fractals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FractalsApplication
{
	public static void main(String[] args) 
	{
		SpringApplication.run(FractalsApplication.class, args);
	}

}

