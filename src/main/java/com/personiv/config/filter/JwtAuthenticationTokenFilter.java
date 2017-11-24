package com.personiv.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.personiv.model.UserDetailsImpl;
import com.personiv.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader("Authorization");
        logger.info("Checking URI "+request.getRequestURI());
        String username = null;
        
        String authToken = null;
        logger.info("checking request header " + requestHeader);
        
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
        	
            authToken = requestHeader.substring(7);
            
            logger.info("checking token " + authToken);
            
            try {
            	
                username = jwtTokenUtil.getUsernameFromToken(authToken);
                
                logger.info("Fetched user " + username);
                
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        logger.info("checking authentication for user " + username);
        
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
        	
        	UserDetailsImpl user = (UserDetailsImpl)userDetailsService.loadUserByUsername(username);
        	
        	
        	user.getUser().setPassword(null);
        	
        	for(GrantedAuthority a: user.getAuthorities()) {
        		logger.info("User authority: "+a.getAuthority());
        	}
           // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, user)) {
        
                //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                for(GrantedAuthority a:  SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
                	
            		logger.info("Checking security authentication user authority: "+a.getAuthority());
            	}
            }
        }

        chain.doFilter(request, response);
    }
}