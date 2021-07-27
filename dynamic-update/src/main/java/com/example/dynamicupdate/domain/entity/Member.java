package com.example.dynamicupdate.domain.entity;

import com.example.dynamicupdate.common.converter.PasswordEncryptConverter;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Convert(converter = PasswordEncryptConverter.class)
    private String password;

    private String introduce;

    public Member(String email, String password, String introduce) {
        this.email = email;
        this.password = password;
        this.introduce = introduce;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
