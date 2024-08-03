package com.springboot.blog.service;

import com.springboot.blog.dataTransferObject.LoginDto;
import com.springboot.blog.dataTransferObject.RegisterDto;

public interface AuthService {

  String login(LoginDto loginDto);
  String register(RegisterDto registerDto);
}
