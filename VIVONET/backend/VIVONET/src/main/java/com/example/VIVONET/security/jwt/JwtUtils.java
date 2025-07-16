package com.example.VIVONET.security.jwt;

import com.example.VIVONET.models.User;
import com.example.VIVONET.repositories.UserRepository;
import com.example.VIVONET.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${vivonet.app.jwtSecret}")
    private String jwtSecret;
    @Value("${vivonet.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${vivonet.app.jwtCookieName}")
    private String jwtCookie;
    @Autowired
    private UserRepository userRepository;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String authority = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null); // This assumes there's always at least one authority
        String jwt = generateTokenFrom(userPrincipal.getId(), authority, userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(15* 60).httpOnly(true).build();
        return cookie;
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }
    public Long getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("id", Long.class);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
            Long userId = claims.get("id", Long.class);
            User user = userRepository.findById(userId).orElse(null);

            if (user == null || (!user.getEnabled())) { // assumes you have an `active` boolean flag
                logger.error("Korisnik ne postoji ili je deaktiviran");
                return false;
            }
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateTokenFrom(Long id, String role, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .claim("id", id)
                .claim("role", role)
                .claim("username", username)
                .claim("lastActivity", now.getTime())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
