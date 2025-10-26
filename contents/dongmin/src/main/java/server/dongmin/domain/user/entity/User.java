package server.dongmin.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Grade;
import server.dongmin.domain.user.enums.Role;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String address;

    public static User create(SignUpRequest signUpRequest, String cryptedPassword) {
        return User.builder()
                .userName(signUpRequest.getUserName())
                .password(cryptedPassword)
                .email(signUpRequest.getEmail())
                .nickName(signUpRequest.getNickName())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .age(signUpRequest.getAge())
                .gender(signUpRequest.getGender())
                .grade(Grade.Bronze)
                .role(signUpRequest.getRole())
                .address(signUpRequest.getAddress())
                .build();
    }
}
