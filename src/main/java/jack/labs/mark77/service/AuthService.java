package jack.labs.mark77.service;

import jack.labs.mark77.dto.JwtUserInfoDto;
import jack.labs.mark77.dto.UserInfo;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public String signIn(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        JwtUserInfoDto info = new JwtUserInfoDto(user.getId(), user.getRole());
        return jwtService.createToken(info);
    }

    @Transactional
    public User signUp(UserInfo userInfo) {
        String encryptedPassword = passwordEncoder.encode(userInfo.getPassword());
        return userRepository.save(userInfo.toEntity(encryptedPassword));
    }
}
