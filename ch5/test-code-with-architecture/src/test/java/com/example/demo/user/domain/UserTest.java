package com.example.demo.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHoler;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

public class UserTest{

  @Test
  public void User는_UserCreate_객체로_생성할_수_있다(){
    // given
    UserCreate userCreate = UserCreate.builder()
        .email("chrismhy1027@kakao.com")
        .nickname("gkdudans-test")
        .address("Pangyo")
        .build();

    // when
    User user = User.from(userCreate, new TestUuidHoler("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

    // then
    assertThat(user.getId()).isNull();
    assertThat(user.getEmail()).isEqualTo("chrismhy1027@kakao.com");
    assertThat(user.getNickname()).isEqualTo("gkdudans-test");
    assertThat(user.getAddress()).isEqualTo("Pangyo");
    assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

  }

  @Test
  public void User는_UserUpdate_객체로_수정할_수_있다(){
    // given
    User user = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .build();
    UserUpdate userUpdate = UserUpdate.builder()
        .nickname("gkdudans-test")
        .address("Pangyo")
        .build();

    // when
    user = user.update(userUpdate);

    // then
    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(user.getNickname()).isEqualTo("gkdudans-test");
    assertThat(user.getAddress()).isEqualTo("Pangyo");
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    assertThat(user.getLastLoginAt()).isEqualTo(100L);
  }

  @Test
  public void User는_로그인을_할_수_있고_마지막_로그인_시간이_변경된다(){
    // given
    User user = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .build();

    // when
    user = user.login(new TestClockHolder(1678530673958L));

    // then
    assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

  }

  @Test
  public void User는_유효한_인증_코드로_계정을_활성화_할_수_있다(){
    // given
    User user = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.PENDING)
        .lastLoginAt(100L)
        .build();

    // when
    user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    // then
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  public void User는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다(){
    // given
    User user = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.PENDING)
        .lastLoginAt(100L)
        .build();

    // when
    // then
    assertThatThrownBy(()-> user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
        .isInstanceOf(CertificationCodeNotMatchedException.class);
  }

}
