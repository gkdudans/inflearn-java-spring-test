package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;



public class UserCreateControllerTest {

  @Test
  void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_pending_상태이다() throws Exception {
    // given
    TestContainer testContainer = TestContainer.builder()
        .uuidHolder(() -> "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .build();
    UserCreate userCreateDto = UserCreate.builder()
        .email("chrismhy1027@kakao.com")
        .nickname("gkdudans-test")
        .address("Pangyo")
        .build();

    // when
    ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreateDto);

    // then
    assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().getId()).isEqualTo(1);
    assertThat(result.getBody().getEmail()).isEqualTo("chrismhy1027@kakao.com");
    assertThat(result.getBody().getNickname()).isEqualTo("gkdudans-test");
    assertThat(result.getBody().getLastLoginAt()).isNull();
    assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
    assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  }

}
