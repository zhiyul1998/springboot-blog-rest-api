package com.springboot.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// generate encoded passwords to put in the database
public class PasswordGeneratorEncoder {

  public static void main(String[] args) {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    System.out.println(passwordEncoder.encode("hazel"));
    System.out.println(passwordEncoder.encode("admin"));
    System.out.println(passwordEncoder.encode("hugo"));
  }
}

