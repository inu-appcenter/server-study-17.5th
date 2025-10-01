package server.Heeyoung.domain.User.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.Heeyoung.domain.Order.entity.Order;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, name = "login_id")
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "phone_num")
    private String phoneNum;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @Builder
    private User(String loginId, String password, String email, String phoneNum, String address, String nickname, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
        this.nickname = nickname;
        this.name = name;
    }


}
