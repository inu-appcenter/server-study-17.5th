package server.dongmin.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
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
    private String address;

    public static User create(String userName, String password, String email, String nickName,
                              String phoneNumber, int age, Gender gender, String address) {
        return User.builder()
                .userName(userName)
                .password(password)
                .email(email)
                .nickName(nickName)
                .phoneNumber(phoneNumber)
                .age(age)
                .gender(gender)
                .grade(Grade.Bronze)
                .address(address)
                .build();
    }
}
