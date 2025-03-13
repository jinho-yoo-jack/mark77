package jack.labs.mark77.service;

import io.jsonwebtoken.ExpiredJwtException;
import jack.labs.mark77.dto.Authority;
import jack.labs.mark77.dto.JwtUserInfoDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@ActiveProfiles("test")
@SpringBootTest
class JwtServiceIntegrationsTests {
    @Autowired
    private JwtService jwtService;

    private JwtUserInfoDto jwtUserInfoDto;

    @BeforeEach
    void init() {
        jwtUserInfoDto = new JwtUserInfoDto("tony", Authority.USER);
    }

    @Test
    void generateAccessToken_Consistent_ReturnAccessToken() {
        String accessToken = jwtService.createToken(jwtUserInfoDto);
        assertThat(accessToken).isNotNull().isNotEmpty();
    }

    @Test
    void generateAccessToken_ExpiredTime_30min() {
        String accessToken = jwtService.createToken(jwtUserInfoDto);
        System.out.println(jwtService.getExpiredTime(accessToken));
        Instant now = Instant.now();
        Instant expiredTime = Instant.ofEpochMilli(jwtService.getExpiredTime(accessToken).getTime());

        // 두 시간의 차이를 분 단위로 변환
        long effectiveTime = Duration.between(now, expiredTime).toMinutes();
        long expectedMin = 28;
        long expectedMax = 30;

        assertThat(effectiveTime).isBetween(expectedMin, expectedMax);
    }

    @Test
    void getUserId_Consistent_ReturnUserId() {
        String accessToken = jwtService.createToken(jwtUserInfoDto);
        String expectedUserId = "tony";
        assertThat(jwtService.getUserId(accessToken)).isEqualTo(expectedUserId);
    }

    @Test
    void getUserId_ExpiredAccessToken_ReturnNull() {
        Instant expiredTime = Instant.now().minusSeconds(300 * 24 * 60 * 60);
        String accessToken = jwtService.createToken(jwtUserInfoDto, expiredTime);
        assertThatThrownBy(() -> jwtService.getUserId(accessToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

}
