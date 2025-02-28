package jack.labs.mark77.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jack.labs.mark77.dto.Authority;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.Key;
import java.util.Date;

@ActiveProfiles("test")
@SpringBootTest
@EnableConfigurationProperties
class JwtServiceTests {

    private final static long ONE_MINUTE = 60 * 1000;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration_time}")
    private long accessTokenExpiresTime;

    private Key key;

    private long accessTokenExpireTime;


    @BeforeEach
    void init() {
        System.out.print("SecretKey ::: " + secretKey);
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        accessTokenExpireTime = ONE_MINUTE * accessTokenExpiresTime;        // 30 min
    }

    @Test
    void createToken(){
        Claims claims = Jwts.claims();
        claims.put("user_id", "tony");
        claims.put("role", Authority.valueOf("ADMIN"));

        long now = (new Date()).getTime();
        Date expires = new Date(now + accessTokenExpireTime);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expires)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Assertions.assertThat(accessToken).isNotEmpty();

    }

    @Test
    void parseClaims_ValidToken() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoidG9ueSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MzkyMzAzMDIsImV4cCI6MTc0OTIzMDMwMn0.A7qnN7RduUXS4AIVd46GzMj2OfIJIZ5YABwVEzS5tMI";
        String secretKey = "i123am123iron123man123For123tony123stark99mark77";
        Claims c = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        String actual = c.get("user_id", String.class);
        String expected = "tony";

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEqualTo(expected);

    }
}
