package jack.labs.mark77.dto;

import jack.labs.mark77.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor

public class AuthenticationToken implements UserDetails {
    private final String id;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    public static AuthenticationToken of(User u) {
        String id = u.getId();
        String password = u.getPassword();
        Authority authorities = Authority.USER;

        return new AuthenticationToken(id, password, List.of(authorities));
    }
}
