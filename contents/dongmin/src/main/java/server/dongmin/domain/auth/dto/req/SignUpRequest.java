package server.dongmin.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Role;

@Schema(description = "회원가입 요청 DTO")
public record SignUpRequest(

        @Schema(description = "회원 이름", example = "son dongmin")
        @NotBlank(message = "이름이 비어있습니다.")
        String userName,

        @Schema(description = "비밀번호", example = "q1w2e3r4@")
        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password,

        @Schema(description = "이메일", example = "dongmin@inu.ac.kr")
        @NotBlank(message = "이메일이 비어있습니다.")
        @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$",
                message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Schema(description = "별명", example = "SDM")
        @NotBlank(message = "별명이 비어있습니다.")
        String nickName,

        @Schema(description = "휴대폰 번호", example = "01012345678")
        @NotBlank(message = "번호가 비어있습니다.")
        @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대전화번호: 10~11자 숫자만 입력해주세요.")
        String phoneNumber,

        @Schema(description = "나이", example = "23")
        @NotNull(message = "나이가 비어있습니다.")
        Integer age,

        @Schema(description = "성별", example = "MALE")
        @NotNull(message = "성별이 비어있습니다.")
        Gender gender,

        @Schema(description = "권한", example = "ADMIN")
        @NotNull(message = "권한이 비어있습니다.")
        Role role,

        @Schema(description = "주소", example = "인천광역시 서구 비즈니스로10")
        @NotBlank(message = "주소가 비어있습니다.")
        String address
) {

}
