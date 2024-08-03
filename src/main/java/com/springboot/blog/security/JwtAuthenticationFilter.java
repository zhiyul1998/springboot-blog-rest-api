package com.springboot.blog.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// authenticate the JWT tokens(subsequent requests, check if the token is included in the header)
// execute before spring security filter
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;
  private UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  // execute once per request
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    // get JWT token from HTTP request header
    String token = getTokenFromRequest(request);

    // validate token
    if(StringUtils.hasText(token) && jwtTokenProvider.validToken(token)) {

      // get username from token
      String username = jwtTokenProvider.getUsername(token);
      // get user object from the database based on the username using UserDetailsService
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                                                              userDetails,
                                                                              null,
                                                                              userDetails.getAuthorities()
      );

      // add request to the authentication token
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      // set this authentication token to the security context holder
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response); // continue request processing, and let Spring Security to check the Security Context Holder
  }

  private String getTokenFromRequest(HttpServletRequest request) {

    String bearerToken = request.getHeader("Authorization");

    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}
