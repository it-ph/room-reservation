package com.personiv.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.personiv.config.filter.JwtAuthenticationTokenFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	@Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder());
		
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	// This method is used for override HttpSecurity of the web Application.
	// We can specify our authorization criteria inside this method.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
			// starts authorizing configurations
		http
			.authorizeRequests()
			.antMatchers("/","/index","/referral/**",
					    "/node_modules/**",
					    "/app/**",
					    "/metisMenu/**",
					    "/authenticate/**",
					    "/password/**",
					    "/rooms/**",
					    "/fonts/**",
					    "/reservation/**"
					    ).permitAll()
			.antMatchers("/user/**").hasAuthority("ADMIN").and()
			// disabling the basic authentication
			.httpBasic().disable()
			// configuring the session as state less. Which means there is
			// no session in the server
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			// disabling the CSRF - Cross Site Request Forgery
			.csrf().disable();
		
		  //JWT Custom filter
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	
		// disable page caching
		http.headers().cacheControl();
		
   
		
	}
	


}
