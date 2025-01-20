package com.example.demo.user.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import java.time.Clock;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

  private final Long id;
  private final String email;
  private final String nickname;
  private final String address;
  private final String certificationCode;
  private final UserStatus status;
  private final Long lastLoginAt;

  @Builder
  public User(Long id, String email, String nickname, String address, String certificationCode,
      UserStatus status, Long lastLoginAt) {
    this.id = id;
    this.email = email;
    this.nickname = nickname;
    this.address = address;
    this.certificationCode = certificationCode;
    this.status = status;
    this.lastLoginAt = lastLoginAt;
  }

  // User가 책임을 가짐
  public static User from(UserCreate userCreate, UuidHolder uuidHolder){
    return User.builder()
        .email(userCreate.getEmail())
        .nickname(userCreate.getNickname())
        .address(userCreate.getAddress())
        .status(UserStatus.PENDING)
        .certificationCode(uuidHolder.random())
        .build();
  }

  public User update(UserUpdate userUpdate) {
    // 불변객체의 반환결과는 새로운 인스턴스
    return User.builder()
        .id(id)
        .email(email)
        .nickname(userUpdate.getNickname())
        .address(userUpdate.getAddress())
        .status(status)
        .certificationCode(certificationCode)
        .build();
  }

  public User login(ClockHolder clockHolder){
    return User.builder()
        .id(id)
        .email(email)
        .nickname(nickname)
        .address(address)
        .status(status)
        .lastLoginAt(clockHolder.millis())
        .certificationCode(certificationCode)
        .build();
  }

  public User certificate(String certificationCode){
    if (!this.certificationCode.equals(certificationCode)) {
      throw new com.example.demo.common.exception.CertificationCodeNotMatchedException();
    }
    return User.builder()
        .id(id)
        .email(email)
        .nickname(nickname)
        .address(address)
        .status(UserStatus.ACTIVE)
        .lastLoginAt(lastLoginAt)
        .certificationCode(certificationCode)
        .build();
  }
}
