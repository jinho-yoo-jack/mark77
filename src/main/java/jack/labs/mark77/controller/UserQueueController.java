package jack.labs.mark77.controller;

import jack.labs.mark77.service.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class UserQueueController {
    private final UserQueueService userQueueService;

    @GetMapping("/register")
    public boolean registerUser(@RequestParam(name = "user_id") Long userId) {
        return userQueueService.registerUser(userId);
    }
}
