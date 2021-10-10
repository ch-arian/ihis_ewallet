package com.ihis.ewallet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ihis.ewallet.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
            "/login",
            "/register",
            "/csrf"
    };

	
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
			   .antMatchers(AUTH_WHITELIST).permitAll()
			   .anyRequest().authenticated();
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			   
	}
	
}
