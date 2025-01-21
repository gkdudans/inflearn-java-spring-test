package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


public class UserControllerTest {

  @Test
  void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달받을_수_있다() {
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .status(UserStatus.PENDING)
        .build());

    // when
    ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1);
    assertThat(result.getBody().getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(result.getBody().getNickname()).isEqualTo("gkdudans");
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(100);
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();

    // when

    // then
    Assertions.assertThatThrownBy(() -> {
      UserController.builder()
          .userReadService(testContainer.userReadService).build().getUserById(1);
    }).isInstanceOf(ResourceNotFoundException.class);
  }


  @Test
  void 사용자는_인증_코드로_계정을_활성화시킬_수_있다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .status(UserStatus.ACTIVE)
        .build());

    // when
    ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
    assertThat(testContainer.userRepository.getById(1)).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다(){
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .lastLoginAt(100L)
        .status(UserStatus.ACTIVE)
        .build());
    // when
    // then
    Assertions.assertThatThrownBy(() -> {
      UserController.builder();
      testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
    }).isInstanceOf(CertificationCodeNotMatchedException.class);
  }

  @Test
  void 사용자는_내_정보를_수정할_수_있다(){
    // given
    TestContainer testContainer = TestContainer.builder()
        .build();
    testContainer.userRepository.save(User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(100L)
        .build());

    // when
    ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("kok202@naver.com",
        UserUpdate.builder()
            .nickname("gkdudans-test")
            .address("Pangyo")
            .build());

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1);
    assertThat(result.getBody().getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(result.getBody().getNickname()).isEqualTo("gkdudans-test");
    assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
    assertThat(result.getBody().getLastLoginAt()).isEqualTo(100);
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }
}
