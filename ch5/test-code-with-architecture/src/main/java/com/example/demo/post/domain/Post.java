package com.example.demo.post.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.user.domain.User;
import java.time.Clock;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

  private Long id;
  private String content;
  private Long createdAt;
  private Long modifiedAt;
  private User writer;

  @Builder
  public Post(Long id, String content, Long createdAt, Long modifiedAt, User writer) {
    this.id = id;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.writer = writer;
  }

  public static Post from(User writer, ClockHolder clockHolder, PostCreate postCreate) {
    return Post.builder()
        .content(postCreate.getContent())
        .writer(writer)
        .createdAt(clockHolder.millis())
        .build();
  }

  public Post update(ClockHolder clockHolder, PostUpdate postUpdate) {
    return Post.builder()
        .id(id)
        .content(postUpdate.getContent())
        .createdAt(createdAt)
        .modifiedAt(clockHolder.millis())
        .writer(writer)
        .build();
  }
}
