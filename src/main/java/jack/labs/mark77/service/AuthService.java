package jack.labs.mark77.service;

import jack.labs.mark77.dto.JwtUserInfoDto;
import jack.labs.mark77.dto.UserInfo;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;

    public String signInV2(Authentication auth) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
        // userId, password
        // 신원 인증
        Authentication authentication =  authenticationManager.authenticate(auth);
        // DB에 가서 userId를 확인하고,
        // 암호화되서 저장한 Password를 복호화 해서 입력받은 Passwod와 동일한지 비교하고
        // 문제가 없다면, 로그인 허락하는 유저 정보를
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.createAccessToken(authentication);
    }

    @Transactional
    public String signIn(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("등록되지 않은 사용자 입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // Access Token을 발급해준다.
        JwtUserInfoDto info = new JwtUserInfoDto(user.getId(), user.getRole());
        return jwtService.createToken(info);
    }

    @Transactional
    public User signUp(UserInfo userInfo) {
        String encryptedPassword = passwordEncoder.encode(userInfo.getPassword());
        return userRepository.save(userInfo.toEntity(encryptedPassword));
    }
}
