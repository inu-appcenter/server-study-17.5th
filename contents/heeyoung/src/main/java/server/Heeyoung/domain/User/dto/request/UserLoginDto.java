package server.Heeyoung.domain.User.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {
    private String loginId;
    private String password;
}
