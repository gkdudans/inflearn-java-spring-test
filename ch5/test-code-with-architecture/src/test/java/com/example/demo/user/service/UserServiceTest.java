package com.example.demo.user.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserEntity;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({ // sql 데이터 중복 방지
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {

  @Autowired
  private UserService userService;
  @MockBean // Mock으로 선언된 객체로 덮어쓰기 -> MockBean 주입
  private JavaMailSender javaMailSender;

  @Test
  void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
    // given
    String email = "chrismhy1027@naver.com";

    // when
    UserEntity result = userService.getByEmail(email);

    // then
    assertThat(result.getNickname()).isEqualTo("gkdudans");

  }

  @Test
  void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
    // given
    String email = "chrismhy1028@naver.com";

    // when
    // then
    assertThatThrownBy(() -> {
      UserEntity result = userService.getByEmail(email);
    }).isInstanceOf(com.example.demo.common.exception.ResourceNotFoundException.class);
  }

  @Test
  void getById는_ACTIVE_상태인_유저를_찾아올_수_있다(){
    // given
    // when
    UserEntity result = userService.getById(1);

    // then
    assertThat(result.getNickname()).isEqualTo("gkdudans");

  }

  @Test
  void getById는_PENDING_상태인_유저를_찾아올_수_없다(){
    // given
    // when
    // then
    assertThatThrownBy(() -> {
      UserEntity result = userService.getById(2);
    }).isInstanceOf(com.example.demo.common.exception.ResourceNotFoundException.class);
  }

  @Test
  void UserCreateDto를_이용하여_유저를_생성할_수_있다(){
    // given
    UserCreate userCreateDto = UserCreate.builder()
        .email("chrismhy1029@naver.com")
        .address("Gyeongi")
        .nickname("gkdudans3")
        .build();

    // 이메일 받기 위한 mockito
    BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

    // when
    UserEntity result = userService.create(userCreateDto);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    // assertThat(result.getCertificationCode()).isEqualTo("T.T)";

  }

  @Test
  void UserUpdateDto를_이용하여_유저를_수정할_수_있다(){
    // given
    UserUpdate userUpdateDto = UserUpdate.builder()
        .address("Incheon")
        .nickname("gkdudans-test")
        .build();

    // when
    userService.update(1, userUpdateDto);

    // then
    UserEntity userEntity = userService.getById(1);
    assertThat(userEntity.getId()).isNotNull();
    assertThat(userEntity.getAddress()).isEqualTo("Incheon");
    assertThat(userEntity.getNickname()).isEqualTo("gkdudans-test");

  }

  @Test
  void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
    // given
    // when
    userService.login(1);

    // then
    UserEntity userEntity = userService.getById(1);
    assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
    // assertThat(result.getLastLoginAt()).isEqualTo("T.T)";
  }

  @Test
  void PENDING_상태인_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
    // given
    // when
    userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaB");

    // then
    UserEntity userEntity = userService.getById(1);
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
    // given
    // when
    // then
    assertThatThrownBy(() -> {
      userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }).isInstanceOf(com.example.demo.common.exception.CertificationCodeNotMatchedException.class);
  }


}
