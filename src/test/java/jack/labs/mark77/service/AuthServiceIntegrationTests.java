package jack.labs.mark77.service;

import jack.labs.mark77.dto.UserInfo;
import jack.labs.mark77.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class AuthServiceIntegrationTests {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    private final String userId = "tony";
    private final String password = "stark";
    private final String nickname = "iron-man";
    private final String role = "user";

    @BeforeEach
    void init() {
        UserInfo userInfo = UserInfo.builder()
            .userId(userId)
            .password(password)
            .nickname(nickname)
            .role(role)
            .build();
        User u = authService.signUp(userInfo);
        assertThat(u).isNotNull();
        assertThat(u.getId()).isNotNull().isNotBlank().isEqualTo(userId);
    }

    @Test
    void signIn_Normal_user() {
        String accessToken = authService.signIn(userId, password);
        String userId = jwtService.getUserId(accessToken);

        assertThat(userId).isEqualTo(this.userId);
    }


}
