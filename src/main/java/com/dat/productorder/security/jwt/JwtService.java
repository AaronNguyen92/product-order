package com.dat.productorder.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private Key getSignInKey(){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String buildToken(UserDetails userDetails, Long expiration){

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();

    }

    public String generateAccessToken(UserDetails userDetails) {

        return buildToken(userDetails, jwtExpiration);

    }

    public String generateRefreshToken(UserDetails userDetails) {

        return buildToken(userDetails, refreshExpiration);

    }

    public String extractUsername(String token){

        return extractClaim(token, Claims::getSubject);

    }

    public Date extractExpiration(String token){

        return extractClaim(token, Claims::getExpiration);

    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);

    }


    private Claims extractAllClaims(String token){

        return Jwts.parser().verifyWith((SecretKey) getSignInKey())
                .build().parseSignedClaims(token).getPayload();

    }

    private boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());

    }

    public boolean isTokenValid(String token, UserDetails userDetails){

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }
}
