package server.dongmin.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import server.dongmin.domain.auth.dto.req.SignUpRequest;
import server.dongmin.domain.user.enums.Gender;
import server.dongmin.domain.user.enums.Grade;
import server.dongmin.domain.user.enums.Role;
import server.dongmin.global.BaseTimeEntity;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private User(String userName, String password, String email, String nickName, String phoneNumber,
                 int age,  Gender gender, Grade grade, Role role, String address) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
        this.role = role;
        this.address = address;
    }

    public static User create(SignUpRequest signUpRequest, String cryptedPassword) {
        return new User(
                signUpRequest.userName(),
                cryptedPassword,
                signUpRequest.email(),
                signUpRequest.nickName(),
                signUpRequest.phoneNumber(),
                signUpRequest.age(),
                signUpRequest.gender(),
                Grade.BRONZE,
                signUpRequest.role(),
                signUpRequest.address()
        );
    }
}
