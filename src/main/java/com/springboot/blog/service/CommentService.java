package com.springboot.blog.service;

import com.springboot.blog.dataTransferObject.CommentDto;

import java.util.List;

public interface CommentService {
  CommentDto createComment(long postId, CommentDto commentDto);
  List<CommentDto> getCommentsByPostId(long postId);
  CommentDto getCommentById(Long postId, Long commentId);
  CommentDto updateComment(long postId, long commentId, CommentDto commentDto);
  void deleteComment(long postId, long commentId);
}
