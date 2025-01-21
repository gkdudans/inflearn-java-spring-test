package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserCreateService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer { // Spring의 IOC를 흉내냄
  public final MailSender mailSender;
  public final UserRepository userRepository;
  public final UserReadService userReadService;
  public final UserCreateService userCreateService;
  public final UserUpdateService userUpdateService;
  public final AuthenticationService authenticationService;
  public final PostService postService;
  public final CertificationService certificationService;
  public final PostRepository postRepository;
  public final UserController userController;
  public UserCreateController userCreateController;

  @Builder
  public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
    this.mailSender = new FakeMailSender();
    this.userRepository = new FakeUserRepository();
    this.postRepository = new FakePostRepository();
    this.postService = PostServiceImpl.builder()
        .postRepository(this.postRepository)
        .userRepository(this.userRepository)
        .clockHolder(clockHolder)
        .build();
    this.certificationService = new CertificationService(this.mailSender);
    UserServiceImpl userService = UserServiceImpl.builder()
        .uuidHolder(uuidHolder)
        .clockHolder(clockHolder)
        .userRepository(this.userRepository)
        .certificationService(this.certificationService)
        .build();
    this.userReadService = userService;
    this.userCreateService = userService;
    this.userUpdateService = userService;
    this.authenticationService = userService;
    this.userController = UserController.builder()
        .userReadService(userReadService)
        .userCreateService(userCreateService)
        .userUpdateService(userUpdateService)
        .authenticationService(authenticationService)
        .build();
  }
}
