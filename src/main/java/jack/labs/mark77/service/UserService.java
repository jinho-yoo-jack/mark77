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

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User getUserInfo(String id) throws NotFoundUserException {
/*        try {
            Optional<User> u = userRepository.findById(id);
            if(u.isPresent()) { // Optional 박스에 User 엔티티를 담았는데,
                // User 엔티티가 Null이야? isPresent() -> User 엔티티가 Null이면 false / true
                return u.get();
            }
        }catch (NoSuchElementException e) {
            throw new NotFoundUserException();
        }*/
        return userRepository
            .findById(id).orElseThrow(NotFoundUserException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저는 없습니다."));

        // DB에서 UserId 컬럼으로 Client가 전달해준 ID 값으로 조회를 했는데,
        // select * from USER where id = 'jhq7342';
        // null or empty
        // throw new UsernameNotFoundException

        return AuthenticationToken.of(user);
    }

}
