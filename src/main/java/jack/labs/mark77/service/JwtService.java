package jack.labs.mark77.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jack.labs.mark77.dto.JwtUserInfoDto;
import jack.labs.mark77.global.exception.NotFoundUserException;
import jakarta.servlet.http.HttpServletResponse;
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
    private final Key refreshKey;
    private final RedisService redisService;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    private static final long ONE_SECOND = 10000;
    private static final long ONE_MINUTE = ONE_SECOND * 600;
    private static final String USERNAME_KEY = "user_id";
    private static final String USERROLE_KEY = "role";
    private static final String INVALID_TOKEN_MESSAGE = "INVALID_TOKEN";


    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.refresh_secret}") String refreshSecretKey,
                      @Value("${jwt.expiration_time}") long accessTokenExpiresTime,
                      RedisService redisService) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.redisService = redisService;
        accessTokenExpireTime = ONE_MINUTE * accessTokenExpiresTime;        // 30 min
        refreshTokenExpireTime = ONE_MINUTE * (accessTokenExpiresTime * 2); // 60 min
    }

    /**
     * Create Access Token
     *
     * @return Access Token
     */
    public String createToken(JwtUserInfoDto member) {
        String accessToken = generateAccessToken(member);
        String refreshToken = generateRefreshToken(member);
        saveToRedis(accessToken, refreshToken);
        return accessToken;
    }

    public String getUserId(String token) {
        return parseClaims(token).get(USERNAME_KEY, String.class);
    }

    public String getUserId() {
        return getUserInfo(USERNAME_KEY);
    }

    public String getUserAuthority() {
        return getUserInfo(USERROLE_KEY);
    }

    private String getUserInfo(String needKey) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
            return switch (needKey) {
                case USERNAME_KEY -> userDetails.getUsername();
                case USERROLE_KEY -> userDetails.getAuthorities().toString();
                default -> throw new NotFoundUserException();
            };
        }
        return null;
    }


    public boolean validateAccessToken(String token, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info(INVALID_TOKEN_MESSAGE, e);
            throw new MalformedJwtException(INVALID_TOKEN_MESSAGE);
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
            log.info(INVALID_TOKEN_MESSAGE + " ::: Refresh", e);
            throw new MalformedJwtException(INVALID_TOKEN_MESSAGE + " ::: Refresh");
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


    private String generateAccessToken(JwtUserInfoDto member) {
        Claims claims = Jwts.claims();
        claims.put(USERNAME_KEY, member.getUserId());
        claims.put("role", member.getRole().getAuthority());

        long now = (new Date()).getTime();
        Date expires = new Date(now + accessTokenExpireTime);
        log.info("JWT token expires: " + expires);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expires)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateAccessToken(Claims claims) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusSeconds(accessTokenExpireTime); // CurrentTime + ExpireTime

        return makeToken(key, claims, Date.from(now.toInstant()), Date.from(expires.toInstant()));
    }

    private String generateRefreshToken(JwtUserInfoDto member) {
        Claims claims = Jwts.claims();
        claims.put(USERNAME_KEY, member.getUserId());
        claims.put("role", member.getRole().getAuthority());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expires = now.plusSeconds(refreshTokenExpireTime);
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

    private void saveToRedis(String accessToken, String refreshToken) {
        redisService.add(accessToken, refreshToken);
    }


    private String renewToken(String accessToken) {
        String refreshToken = redisService.getValue(accessToken);
        boolean isValid = validateRefreshToken(refreshToken);
        if (isValid) return generateAccessToken(parseClaimsForRefresh(refreshToken));
        throw new ExpiredJwtException(null, null, null, null);
    }

}
