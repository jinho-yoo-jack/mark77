package jack.labs.mark77.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jack.labs.mark77.dto.CustomUserInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private final Key key;
    private final Key refreshKey;

    private final long accessTokenExpiresTime;
    private final long refreshTokenExpiresTime;

    private final RedisService redisService;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 3;            // 30분


    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.refresh_secret}") String refreshSecretKey,
                      @Value("${jwt.expiration_time}") long accessTokenExpiresTime,
                      RedisService redisService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.accessTokenExpiresTime = accessTokenExpiresTime;
        long oneHour = this.accessTokenExpiresTime * 10;
        long oneDay = oneHour * 24;
        this.refreshTokenExpiresTime = oneDay * 10;
        this.redisService = redisService;
    }

    /**
     * Create Access Token
     *
     * @param member
     * @return Access Token
     */
    public String createToken(CustomUserInfoDto member) {
        String accessToken = generateAccessToken(member);
        String refreshToken = generateRefreshToken(member);
        saveToRedis(accessToken, refreshToken);
        return accessToken;
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


    public boolean validateAccessToken(String token, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new MalformedJwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            response.addHeader("Authorization", "Bearer " + renewToken(token));
            return true;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid Refresh Token", e);
            throw new MalformedJwtException("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new MalformedJwtException("Expired Refresh Token.\n Please login again.\n");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    private String generateAccessToken(CustomUserInfoDto member) {
        Claims claims = Jwts.claims();
        claims.put("user_id", member.getUserId());
        claims.put("role", member.getRole());

//        ZonedDateTime now = ZonedDateTime.now();
//        ZonedDateTime expires = now.plusSeconds(accessTokenExpiresTime); // CurrentTime + ExpireTime
        long now = (new Date()).getTime();
        Date expires = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        log.info("JWT token expires: " + expires);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(1739517604))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateAccessToken(Claims claims) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusSeconds(accessTokenExpiresTime); // CurrentTime + ExpireTime

        return makeToken(key, claims, Date.from(now.toInstant()), Date.from(expires.toInstant()));
    }

    private String generateRefreshToken(CustomUserInfoDto member) {
        Claims claims = Jwts.claims();
        claims.put("user_id", member.getUserId());
        claims.put("role", member.getRole());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusSeconds(refreshTokenExpiresTime);
        return makeToken(refreshKey, claims, Date.from(now.toInstant()), Date.from(expires.toInstant()));
    }

    private String makeToken(Key key, Claims claims, Date start, Date expires) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expires)
                .setIssuedAt(start)
                .setExpiration(expires)
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

    private Claims parseClaimsForRefresh(String refreshToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Long saveToRedis(String accessToken, String refreshToken) {
        return redisService.add(accessToken, refreshToken);
    }


    private String renewToken(String accessToken) {
        String refreshToken = redisService.getValue(accessToken);
        boolean isValid = validateRefreshToken(refreshToken);
        if (isValid) return generateAccessToken(parseClaimsForRefresh(refreshToken));
        throw new ExpiredJwtException(null, null, null, null);
    }

}
