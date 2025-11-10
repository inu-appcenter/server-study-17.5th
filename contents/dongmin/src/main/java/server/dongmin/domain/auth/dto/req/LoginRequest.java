package server.dongmin.domain.auth.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호가 비어있습니다.")
        String password

) {

}
