package com.example.Api_Auth.Security;
import com.example.Api_Auth.DTO.AuthenticationResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final String Secret_KEY = "d276cd9e2b6b3f4470030d5d427bdc771cff07edfc16c21e6985f2686b524a12";

    private final Set<String> revokedTokens = new HashSet<>();

    @Getter
    @Value("${jwt.expiration-time}")
    private int jwtExpirationTime;


    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extracClaims,
            UserDetails userDetails
    ) {

        return Jwts
                .builder()
                .setClaims(extracClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +jwtExpirationTime))
                .claim("roles",userDetails.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Secret_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValidToken = !revokedTokens.contains(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && isValidToken;
    }

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // Handle exceptions appropriately based on your application's requirements
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.get("roles");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}

