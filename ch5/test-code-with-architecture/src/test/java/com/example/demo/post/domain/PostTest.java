package com.example.demo.post.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class PostTest {

  @Test
  public void PostCreate로_게시물을_만들_수_있다(){
    // given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("helloworld")
        .build();

    User writer = User.builder()
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .build();

    // when
    Post post = Post.from(writer, postCreate);

    // then
    assertThat(post.getContent()).isEqualTo("helloworld");
    assertThat(post.getWriter().getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
    assertThat(post.getWriter().getNickname()).isEqualTo("gkdudans");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
  }

}
