package jack.labs.mark77.service;

import jack.labs.mark77.dto.AuthenticationToken;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.global.exception.NotFoundUserException;
import jack.labs.mark77.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저는 없습니다."));

        return AuthenticationToken.of(user);
    }

}
