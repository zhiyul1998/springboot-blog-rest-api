package com.springboot.blog.service.impl;

import com.springboot.blog.dataTransferObject.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // this class is a spring bean, and we can inject it in other classes
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepository;
  private PostRepository postRepository;
  private ModelMapper mapper;

  @Autowired // can be omitted since this class only has one constructor
  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.mapper = mapper;
  }

  // convert entity to DTO
  private CommentDto mapToDTO(Comment comment) {
    CommentDto commentDto = mapper.map(comment, CommentDto.class);
//    CommentDto commentDto = new CommentDto();
//    commentDto.setId(comment.getId());
//    commentDto.setName(comment.getName());
//    commentDto.setEmail(comment.getEmail());
//    commentDto.setBody(comment.getBody());

    return commentDto;
  }

  // convert DTO to entity
  private Comment mapToEntity(CommentDto commentDto) {
    Comment comment = mapper.map(commentDto, Comment.class);
//    Comment comment = new Comment();
//    comment.setId(commentDto.getId());
//    comment.setName(commentDto.getName());
//    comment.setEmail(commentDto.getEmail());
//    comment.setBody(commentDto.getBody());

    return comment;
  }

  @Override
  public CommentDto createComment(long postId, CommentDto commentDto) {

    Comment comment = mapToEntity(commentDto);

    // retrieve post entity by id
    Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    // set post to comment entity
    comment.setPost(post);
    // save comment entity to DB
    Comment newComment = commentRepository.save(comment);

    return mapToDTO(newComment);
  }

  @Override
  public List<CommentDto> getCommentsByPostId(long postId) {

    // retrieve comments by postId
    List<Comment> comments = commentRepository.findByPostId(postId);

    // convert list of comment entities to list of comment dto
    return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
  }

  @Override
  public CommentDto getCommentById(Long postId, Long commentId) {
    // retrieve post entity by id
    Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    // retrieve comment by id
    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if(!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post.");
    }

    return mapToDTO(comment);
  }

  @Override
  public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
    // retrieve post entity by id
    Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    // retrieve comment by id
    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if(!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post.");
    }

    comment.setName(commentRequest.getName());
    comment.setEmail(commentRequest.getEmail());
    comment.setBody(commentRequest.getBody());

    Comment updatedComment = commentRepository.save(comment);
    return mapToDTO(updatedComment);
  }

  @Override
  public void deleteComment(long postId, long commentId) {
    // retrieve post entity by id
    Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    // retrieve comment by id
    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if(!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post.");
    }

    commentRepository.delete(comment);
  }
}
