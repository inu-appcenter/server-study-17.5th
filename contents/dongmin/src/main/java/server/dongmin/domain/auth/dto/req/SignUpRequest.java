package server.dongmin.domain.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Role;

public record SignUpRequest(

        @NotBlank(message = "이름이 비어있습니다.")
        String userName,

        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password,

        @NotBlank(message = "이메일이 비어있습니다.")
        @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$",
                message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "별명이 비어있습니다.")
        String nickName,

        @NotBlank(message = "번호가 비어있습니다.")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대전화번호: 10~11자 숫자만 입력해주세요.")
        String phoneNumber,

        @NotNull(message = "나이가 비어있습니다.")
        Integer age,

        @NotNull(message = "성별이 비어있습니다.")
        Gender gender,

        @NotNull(message = "권한이 비어있습니다.")
        Role role,

        @NotBlank(message = "주소가 비어있습니다.")
        String address
) {

}
