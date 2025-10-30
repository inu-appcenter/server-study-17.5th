package server.dongmin.domain.auth.dto.req;

public record LoginRequest(
        String email,
        String password
) {

}
