package com.ihis.ewallet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			   .formLogin().disable()
			   .csrf().disable()
			   .httpBasic()
			   
			   //session stateless
			   .and()
			   .sessionManagement()
			   .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			   
			   //authentication of APIs
			   .and()
			   .authorizeRequests()
			   .antMatchers("/").permitAll();
			   
	}
	
}
