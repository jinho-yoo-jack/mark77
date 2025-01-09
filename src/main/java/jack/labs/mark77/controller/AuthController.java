package jack.labs.mark77.controller;

import jack.labs.mark77.dto.SignInDto;
import jack.labs.mark77.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public String signIn(@RequestBody SignInDto signInDto) {
        log.info("UserId ::: {}", signInDto.getUserId());
        log.info("Password ::: {}", signInDto.getPassword());
        return authService.signIn(signInDto.getUserId(), signInDto.getPassword());
    }

    @PostMapping("/join")
    public String signUp() {
        return "";
    }
}
