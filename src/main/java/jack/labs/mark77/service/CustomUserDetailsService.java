package jack.labs.mark77.service;

import jack.labs.mark77.dto.CustomUserDetails;
import jack.labs.mark77.dto.UserInfo;
import jack.labs.mark77.entity.User;
import jack.labs.mark77.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저는 없습니다."));

        UserInfo userInfo = modelMapper.map(user, UserInfo.class);
        return new CustomUserDetails(userInfo);
    }
}
