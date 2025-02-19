package jack.labs.mark77.service;

import jack.labs.mark77.entity.User;
import jack.labs.mark77.global.exception.NotFoundUserException;
import jack.labs.mark77.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }

}
