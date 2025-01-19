package com.example.demo.repostiory;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.user.domain.UserEntity;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
// given에서 중복되는 데이터 생성을 sql 파일로 통일
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

//  @Test
//  void UserRepository가_제대로_연결되었다(){
//    // given
////    UserEntity userEntity = new UserEntity();
////    userEntity.setEmail("chrismhy1027@naver.com");
////    userEntity.setAddress("Seoul");
////    userEntity.setNickname("gkdudans");
////    userEntity.setStatus(UserStatus.ACTIVE);
////    userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
//
//    // when
//    UserEntity result = userRepository.save(userEntity);
//
//    // then
//    assertThat(result.getId()).isNotNull();
//  }

  @Test
  void findByIdAndStatus로_유저_데이터를_찾아올_수_있다(){
    // given
//    UserEntity userEntity = new UserEntity();
//    userEntity.setId(1L);
//    userEntity.setEmail("chrismhy1027@naver.com");
//    userEntity.setAddress("Seoul");
//    userEntity.setNickname("gkdudans");
//    userEntity.setStatus(UserStatus.ACTIVE);
//    userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // when
//    userRepository.save(userEntity);
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

    // then
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  void findByIdAndStatus는_데이터가_없으면_Optional_empty를_내려준다(){
    // given
//    UserEntity userEntity = new UserEntity();
//    userEntity.setId(1L);
//    userEntity.setEmail("chrismhy1027@naver.com");
//    userEntity.setAddress("Seoul");
//    userEntity.setNickname("gkdudans");
//    userEntity.setStatus(UserStatus.ACTIVE);
//    userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // when
//    userRepository.save(userEntity);
    Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

    // then
    assertThat(result.isEmpty()).isTrue();
  }

  @Test
  void findByEmailAndStatus로_유저_데이터를_찾아올_수_있다(){
    // given
//    UserEntity userEntity = new UserEntity();
//    userEntity.setId(1L);
//    userEntity.setEmail("chrismhy1027@naver.com");
//    userEntity.setAddress("Seoul");
//    userEntity.setNickname("gkdudans");
//    userEntity.setStatus(UserStatus.ACTIVE);
//    userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    // when
//    userRepository.save(userEntity);
    Optional<UserEntity> result = userRepository.findByEmailAndStatus("chrismhy1027@naver.com", UserStatus.ACTIVE);

    // then
    assertThat(result.isPresent()).isTrue();
  }

}
