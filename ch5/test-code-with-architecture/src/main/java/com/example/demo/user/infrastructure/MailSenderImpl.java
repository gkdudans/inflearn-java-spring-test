package com.example.demo.user.infrastructure;

import com.example.demo.user.service.port.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSenderImpl implements MailSender {
    private JavaMailSender JavaMailSender;

  @Override
  public void send(String email, String title, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject(title);
    message.setText(content);
    JavaMailSender.send(message);
  }
}
