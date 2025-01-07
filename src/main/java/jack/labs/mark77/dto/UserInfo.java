package jack.labs.mark77.dto;

import jack.labs.mark77.entity.User;
import lombok.Data;

@Data
public class UserInfo {
    private final String userId;
    private final String password;
    private final String nickname;
    private final String role;

    public User toEntity() {
        return User.builder()
                .id(userId)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
