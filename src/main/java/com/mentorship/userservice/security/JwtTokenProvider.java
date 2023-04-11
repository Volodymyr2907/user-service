package com.mentorship.userservice.security;

import com.mentorship.userservice.controllers.exeptions.UserValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${APP_JWT_SECRET}")
    private String jwtSecret;

    @Value("${APP_JWT_EXPIRATION_MILLISECONDS}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.from(Instant.now()).plusMillis(jwtExpirationDate)))
            .signWith(key())
            .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtSecret)
        );
    }


    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) throws UserValidationException {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
            return true;
        } catch (MalformedJwtException ex) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}
