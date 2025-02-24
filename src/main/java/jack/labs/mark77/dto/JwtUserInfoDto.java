package jack.labs.mark77.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtUserInfoDto {
    private String userId;
    private Authority role;
}
