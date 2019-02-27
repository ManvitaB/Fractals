package com.fractals;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringSecurityConfig --- Uses default Spring Security settings to disable caching.
 * @author Scott Wolfskill
 * @created     02/26/2019
 * @last_edit   02/26/2019
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
  
	//Automatically sets default cache-control headers:
	//[cache-control: no-cache, no-store, max-age=0, must-revalidate]
    @Override
    protected void configure(HttpSecurity http) throws Exception {}
}