package com.example.demo.post.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PostServiceTest {

  private PostServiceImpl postService;

  @BeforeEach
  void init(){
    FakeUserRepository fakeUserRepository = new FakeUserRepository();
    FakePostRepository fakePostRepository = new FakePostRepository();
    this.postService = PostServiceImpl.builder()
        .postRepository(fakePostRepository)
        .userRepository(fakeUserRepository)
        .clockHolder(new TestClockHolder(1678530673958L))
        .build();
    User user1 = User.builder()
        .id(1L)
        .email("chrismhy1027@naver.com")
        .nickname("gkdudans")
        .address("Seoul")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(0L)
        .build();
    fakeUserRepository.save(user1);
    User user2 = User.builder()
        .id(2L)
        .email("chrismhy1028@naver.com")
        .nickname("gkdudans2")
        .address("Incheon")
        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
        .status(UserStatus.ACTIVE)
        .lastLoginAt(0L)
        .build();
    fakeUserRepository.save(user2);
    fakePostRepository.save(Post.builder()
        .id(1L)
        .content("helloworld")
        .createdAt(1678530673958L)
        .modifiedAt(0L)
        .writer(user1)
        .build());
  }

  @Test
  void getById는_존재하는_게시물을_내려준다(){
    // given
    // when
    Post result = postService.getPostById(1);

    // then
    assertThat(result.getContent()).isEqualTo("helloWorld");
    assertThat(result.getWriter().getEmail()).isEqualTo("chrismhy1027@naver.com");
  }

  @Test
  void postCreate를_이용하여_게시물을_생성할_수_있다(){
    // given
    PostCreate postCreate = PostCreate.builder()
        .writerId(1)
        .content("helloworld")
        .build();

    // when
    Post result = postService.create(postCreate);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getContent()).isEqualTo("helloworld");
    assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
  }

  @Test
  void postUpdate를_이용하여_게시물을_수정할_수_있다(){
    // given
    PostUpdate postUpdate = PostUpdate.builder()
        .content("helloworld :)")
        .build();

    // when
    postService.update(1, postUpdate);

    // then
    Post post = postService.getPostById(1L);
    assertThat(post.getContent()).isEqualTo("helloworld :)");
    assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);

  }

}
