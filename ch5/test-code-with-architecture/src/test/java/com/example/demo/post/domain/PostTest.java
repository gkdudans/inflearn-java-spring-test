package com.example.demo.post.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.TestClockHolder;
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
    Post post = Post.from(writer, new TestClockHolder(1678530673958L), postCreate);

    // then
    assertThat(post.getContent()).isEqualTo("helloworld");
    assertThat(post.getWriter().getEmail()).isEqualTo("chrismhy1027@naver.com");
    assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
    assertThat(post.getWriter().getNickname()).isEqualTo("gkdudans");
    assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    assertThat(post.getCreatedAt()).isEqualTo("1678530673958L");
    assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
  }

  @Test
  public void PostUpdate로_게시물을_수정할_수_있다(){
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("footbar")
        .build();
    User writer = User.builder()
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .build();
    Post post = Post.builder()
        .id(1L)
        .content("helloworld")
        .createdAt(1678530673958L)
        .modifiedAt(0L)
        .writer(writer)
        .build();

    // when
    post = post.update(new TestClockHolder(1678530673958L), postUpdate);

    // then
    assertThat(post.getContent()).isEqualTo("footbar");
    assertThat(post.getModifiedAt()).isEqualTo("1678530673958L");

  }


}
