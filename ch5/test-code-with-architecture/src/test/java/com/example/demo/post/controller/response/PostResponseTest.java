package com.example.demo.post.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

public class PostResponseTest {

  @Test
  public void Post로_응답을_생성할_수_있다() {
    // given
    Post post = Post.builder()
        .content("helloworld")
        .writer(User.builder()
            .email("chrismhy1027@naver.com")
            .nickname("gkdudans")
            .address("Seoul")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
            .status(UserStatus.ACTIVE)
            .build())
        .build();

    // when
    PostResponse postResponse = PostResponse.from(post);

    // then
    assertThat(postResponse.getContent()).isEqualTo("helloworld");
    assertThat(postResponse.getWriter().getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(postResponse.getWriter().getNickname()).isEqualTo("gkdudans");
    assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
  }


}
