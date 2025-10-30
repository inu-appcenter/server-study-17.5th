package server.dongmin.domain.auth.dto.req;

import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Role;

public record SignUpRequest(
        String userName,
        String password,
        String email,
        String nickName,
        String phoneNumber,
        int age,
        Gender gender,
        Role role,
        String address
) {

}
