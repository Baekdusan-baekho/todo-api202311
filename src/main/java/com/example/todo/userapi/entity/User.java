package com.example.todo.userapi.entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Setter // 엔터티는 세터가 존재하지 않을 수도 있다 신중하게 써야 한다.
@Getter
@ToString @EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id; // 계정명이 아니라 식별 코드 프라이머리키로 활용

    @Column(nullable = false, unique = true)
    private String email; //로그인 정보 유저 아이디 처럼 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @CreationTimestamp
    private LocalDateTime joinDate;

    @Enumerated(EnumType.STRING)
    //@ColumnDefault("'COMMON'") // 이넘타입이라 '' 넣음  문자면 안해도 됨
    @Builder.Default
    private Role role = Role.COMMON; // 유저 권한

    private String profileImg; // 이렇게 쓰면 colume 명은 profile_img로 작성된다   프로필이미지 경로


    public void changeRole(Role role){
        this.role = role;
    }
}
