package server.dongmin.domain.auth.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Role;

public record SignUpRequest(

        @NotBlank(message = "이름이 비어있습니다.")
        String userName,

        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password,

        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "별명이 비어있습니다.")
        String nickName,

        @NotBlank(message = "번호가 비어있습니다.")
        String phoneNumber,

        @NotBlank(message = "나이가 비어있습니다.")
        int age,

        @NotBlank(message = "성별이 비어있습니다.")
        Gender gender,

        @NotBlank(message = "권한이 비어있습니다.")
        Role role,

        @NotBlank(message = "주소가 비어있습니다.")
        String address
) {

}
