package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// get the value of jwt properties configured in the application.properties
@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  // generate JWT token with username
  public String generateToken(Authentication authentication) {

    String username = authentication.getName();

    Date currentDate = new Date();

    Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

    String token = Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(expireDate)
            .signWith(key())
            .compact();

    return token;
  }

  // decode the secret key in resource folder into base64 format
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // get username from JWT token
  public String getUsername(String token) {

    return Jwts.parser()
            .verifyWith((SecretKey) key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
  }

  // validate jwt token
  public boolean validToken(String token) {
    try{
      Jwts.parser()
              .verifyWith((SecretKey) key())
              .build()
              .parse(token);
      return true;
    } catch (MalformedJwtException malformedJwtException) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token.");
    } catch (ExpiredJwtException expiredJwtException) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token.");
    } catch (UnsupportedJwtException unsupportedJwtException) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token.");
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is null or empty.");
    }



  }
}
