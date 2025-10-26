package server.dongmin.domain.auth.dto.req;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    private String userName;
    private String password;
    private String email;
    private String nickName;
    private String phoneNumber;
    private int age;
    private Gender gender;
    private Role role;
    private String address;

}
