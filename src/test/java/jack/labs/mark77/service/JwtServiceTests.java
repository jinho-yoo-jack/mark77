package jack.labs.mark77.service;

import jack.labs.mark77.dto.Authority;
import jack.labs.mark77.dto.JwtUserInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTests {
    @Mock
    private RedisService redisService;

    private JwtService jwtService;

    public static String generateSecretKey(int size) {
        byte[] key = new byte[size];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    @BeforeEach
    void setUp() {
        String secretKey = generateSecretKey(32);
        String refreshSecretKey = generateSecretKey(64);
        long accessTokenExpiresTime = 30L;
        jwtService = new JwtService(secretKey, refreshSecretKey, accessTokenExpiresTime, redisService);
    }

    // 테스트메서드_시나리오_예상결과
    @Test
    void createToken_normal_returnCorrectAccessToken() {
        JwtUserInfoDto member = new JwtUserInfoDto("tony", Authority.USER);
        String accessToken = jwtService.createToken(member);
        assertThat(accessToken).isNotNull().isNotEmpty();
        assertThat(accessToken)
                .isNotNull()
                .contains(".")
                .matches(token -> token.chars().filter(ch -> ch == '.').count() == 2, "JWT 형식(헤더.페이로드.서명)이어야 합니다.");
    }

    void parseClaims_ExpireTimeIs30min_setIs30min(){

    }

    @Test
    void getUserId_normal_returnTony(){
        JwtUserInfoDto member = new JwtUserInfoDto("tony", Authority.USER);
        String accessToken = jwtService.createToken(member);
        String actualUserId = jwtService.getUserId(accessToken);
        String expectedUserId = "tony";

        assertThat(actualUserId).isEqualTo(expectedUserId);

    }
}
