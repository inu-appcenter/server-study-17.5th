package server.dongmin.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtToken {
    private String userName; // 로그인한 유저의 이름
    private String grantType; //JWT의 인증 타입을 나타내는 문자열 필드
    private String accessToken; // 접근 토큰을 나타내는 문자열 필드
    private String refreshToken; // 갱신 토큰을 나타내는 문자열 필드
}
