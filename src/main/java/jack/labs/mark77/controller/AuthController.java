package jack.labs.mark77.controller;

import jack.labs.mark77.dto.SignInDto;
import jack.labs.mark77.dto.SignUpDto;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.global.ApiResponse;
import jack.labs.mark77.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> signIn(@RequestBody SignInDto signInDto) {
        // Frontend에서 Backend로 Request Message를 보낼 때,
        // 모든 Request Message는 Encrypted(암호화) 전달이 된다.
        // -> 은행 서비스는 이런 식으로 동작합니다.
        // Request -> decrypted(복호화) -> 사용자의 정보, 사용자의 요청 메시지
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(authService.signIn(signInDto.getUserId(), signInDto.getPassword())));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpDto signUpDto) {
        try {
            return ResponseEntity.ok(ApiResponse.success(authService.signUp(signUpDto.toService())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
