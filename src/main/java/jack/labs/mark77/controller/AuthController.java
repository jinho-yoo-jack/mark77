package jack.labs.mark77.controller;

import jack.labs.mark77.dto.SignInDto;
import jack.labs.mark77.dto.SignUpDto;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.global.ApiResponse;
import jack.labs.mark77.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return ResponseEntity.ok(ApiResponse.success(authService.signIn(signInDto.getUserId(), signInDto.getPassword())));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<User>> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(ApiResponse.success(authService.signUp(signUpDto.toService())));
    }
}
