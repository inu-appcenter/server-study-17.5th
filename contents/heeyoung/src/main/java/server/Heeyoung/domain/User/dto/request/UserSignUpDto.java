package server.Heeyoung.domain.User.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpDto {
    private String loginId;
    private String password;
    private String email;
    private String phoneNum;
    private String name;
    private String nickname;
    private String address;
}
