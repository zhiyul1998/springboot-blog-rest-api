package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  // need to declare this method to let Spring Data JPA generate the query
  // if only method already implemented are used in service layer, no functions need to be declared here
  List<Comment> findByPostId(long postId);
}
