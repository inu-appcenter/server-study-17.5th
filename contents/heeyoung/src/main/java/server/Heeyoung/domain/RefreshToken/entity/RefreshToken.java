package server.Heeyoung.domain.RefreshToken.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.User.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private String userLoginId;

    @Builder
    private RefreshToken(User user, String refreshToken, Boolean valid) {
        this.userLoginId = user.getLoginId();
        this.refreshToken = refreshToken;
    }

}
