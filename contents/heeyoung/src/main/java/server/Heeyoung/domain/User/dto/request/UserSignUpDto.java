package server.Heeyoung.domain.User.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.web.PageableDefault;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해 8자 이상이어야 합니다."
    )
    private String password;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호 형식은 010-XXXX-XXXX 이어야 합니다."
    )
    private String phoneNum;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @Size(max = 20, message = "닉네임은 20자를 넘을 수 없습니다.")
    private String nickname;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;
}
