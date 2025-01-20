package com.example.demo.user.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

public class UserResponseTest {

  @Test
  public void User으로_응답을_생성할_수_있다(){
    // given
    User user = User.builder()
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .build();

    // when
    UserResponse userResponse = UserResponse.from(user);

    // then
    assertThat(userResponse.getNickname()).isEqualTo("gkdudans");
    assertThat(userResponse.getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

}
