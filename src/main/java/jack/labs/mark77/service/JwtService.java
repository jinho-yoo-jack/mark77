package jack.labs.mark77.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jack.labs.mark77.dto.CustomUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private final Key key;
    private final long accessTokenExpiresTime;

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration_time}") long accessTokenExpiresTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiresTime = accessTokenExpiresTime;
    }

    /**
     * Create Access Token
     *
     * @param member
     * @return Access Token
     */
    public String createToken(CustomUserInfoDto member) {
        return createToken(member, accessTokenExpiresTime);
    }

    public String getUserId(String token) {
        return parseClaims(token).get("user_id", String.class);
    }

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userId = userDetails.getUsername();
        }
        return userId;
    }
    

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private String createToken(CustomUserInfoDto member, long accessTokenExpiresTime) {
        Claims claims = Jwts.claims();
        log.info("CREATE JWT Token ::: user_id ::: {}", member.getUserId());
        claims.put("user_id", member.getUserId());
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusSeconds(accessTokenExpiresTime); // CurrentTime + ExpireTime

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expires.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * JWT Claims 추출
     *
     * @param accessToken
     * @return JWT Claims
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
