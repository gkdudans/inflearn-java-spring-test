package com.example.demo.user.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

public class MyProfileResponseTest {

  @Test
  public void User로_응답을_생성할_수_있다(){
    // given
    User user = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .lastLoginAt(100L)
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .build();

    // when
    MyProfileResponse myProfileResponse = MyProfileResponse.from(user);


    // then
    assertThat(myProfileResponse.getNickname()).isEqualTo("gkdudans");
    assertThat(myProfileResponse.getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
    assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
  }
}
