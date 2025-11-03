package server.Heeyoung.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenResponseDto {
    private final String type;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private JwtTokenResponseDto(String type, String accessToken, String refreshToken) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
