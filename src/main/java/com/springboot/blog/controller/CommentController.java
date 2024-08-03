package com.springboot.blog.controller;

import com.springboot.blog.dataTransferObject.CommentDto;
import com.springboot.blog.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class CommentController {

  private CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                  @Valid @RequestBody CommentDto commentDto) {
    return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
  }

  @GetMapping("/posts/{postId}/comments")
  public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
    return commentService.getCommentsByPostId(postId);
  }

  @GetMapping("/posts/{postId}/comments/{commentId}")
  public CommentDto getCommentById(@PathVariable(value = "postId") long postId,
                                   @PathVariable(value = "commentId") long commentId) {
    return commentService.getCommentById(postId, commentId);
  }

  @PutMapping("/posts/{postId}/comments/{id}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long postId,
                                                  @PathVariable(value = "id") long commentId,
                                                  @Valid @RequestBody CommentDto commentDto) {
    CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
    return new ResponseEntity<>(updatedComment, HttpStatus.OK);
  }

  @DeleteMapping("/posts/{postId}/comments/{id}")
  public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                              @PathVariable(value = "id") long commentId) {
    commentService.deleteComment(postId, commentId);
    return new ResponseEntity<>("Comment deleted successfully.", HttpStatus.OK);
  }

}
