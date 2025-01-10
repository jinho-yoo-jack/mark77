package jack.labs.mark77.dto;

import jack.labs.mark77.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfo {
    private final String userId;
    private String password;
    private final String nickname;
    private final String role;

    public void encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .id(userId)
                .password(encodedPassword)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
