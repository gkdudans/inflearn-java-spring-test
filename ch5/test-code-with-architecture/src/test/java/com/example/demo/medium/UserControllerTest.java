package com.example.demo.medium;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
// MockMvc를 위한 어노테이션
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({ // sql 데이터 중복 방지
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserJpaRepository userRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();


  @Test
  void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달받을_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nickname").value("gkdudans"))
        .andExpect(jsonPath("$.email").value("chrismhy1027@naver.com"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.address").doesNotExist());
  }

  @Test
  void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/users/1234242434"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Users에서 ID 1234242434를 찾을 수 없습니다."));
  }

  @Test
  void 사용자는_인증_코드로_계정을_활성화시킬_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(
            get("/api/users/2/verify")
                .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaB"))
        .andExpect(status().isFound()); // 302 리다이렉트
    UserEntity userEntity = userRepository.findById(2L).get();
    assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
  }

  @Test
  void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(
            get("/api/users/me")
                .header("EMAIL", "chrismhy1027@naver.com"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nickname").value("gkdudans"))
        .andExpect(jsonPath("$.email").value("chrismhy1027@naver.com"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.address").value("Seoul"));
  }

  @Test
  void 사용자는_내_정보를_수정할_수_있다() throws Exception {
    // given
    UserUpdate userUpdateDto = UserUpdate.builder()
        .nickname("gkdudans-test")
        .address("Pangyo")
        .build();

    // when
    // then
    mockMvc.perform(
            put("/api/users/me")
                .header("EMAIL", "chrismhy1027@naver.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nickname").value("gkdudans-test"))
        .andExpect(jsonPath("$.email").value("chrismhy1027@naver.com"))
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.address").value("Pangyo"));

  }
}
