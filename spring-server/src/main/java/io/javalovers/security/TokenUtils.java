package io.javalovers.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class TokenUtils {

  private static final String ISSUER = "HOTELA";

  private final Algorithm algorithm;

  private Long validity;

  public TokenUtils(String secret, Long validity) {
    try {
      this.algorithm = Algorithm.HMAC512(secret);
      this.validity = validity;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(userDetails.getUsername());
  }

  public String refreshToken(String token) {
    String subject = JWT.decode(token).getSubject();
    return generateToken(subject);
  }

  public String getUsernameFromToken(String token) {
    return token != null ? JWT.decode(token).getSubject() : null;
  }

  public boolean validateToken(String token, String userName) {
    try {
      JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).withSubject(userName).build();
      verifier.verify(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String generateToken(String subject) {
    return JWT.create().withIssuer(ISSUER).withIssuedAt(generateCurrentDate()).withExpiresAt(generateExpirationDate())
        .withSubject(subject).sign(algorithm);
  }

  private Date generateCurrentDate() {
    return new Date(System.currentTimeMillis());
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + this.validity * 1000);
  }

}
