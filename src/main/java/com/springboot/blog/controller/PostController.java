package com.springboot.blog.controller;

import com.springboot.blog.dataTransferObject.PostDto;
import com.springboot.blog.dataTransferObject.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController // internally uses @ResponseBody to convert a java object into json
@RequestMapping("api/v1/posts")
@Tag( // provide info for post controller on swagger ui
        name = "CRUD REST APIs for Post Entity"
)
public class PostController {

  private PostService postService; // loose coupling by injecting interface

  public PostController(PostService postService) {
    this.postService = postService;
  }

  // create blog post
  // ResponseEntity is a class that we can use to configure and represent our HTTP response
  // @RequestBody converts json into a java object that we can work with
  @SecurityRequirement( // get authentication header on the swagger ui // refer to SecurityConfig
          name = "Bearer Authentication"
  )
  @Operation( // custom header or title on swagger ui example
          summary = "Create Post REST API",
          description = "Create Post REST API is used to save post into database"
  )
  @ApiResponse(
          responseCode = "201",
          description = "Http Status 201 CREATED"
  )
  @PreAuthorize("hasRole('ADMIN')") // only ADMIN user can access this endpoint
  @PostMapping
  public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
    return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
  }

  // get all posts
  // can omit ResponseEntity when we don't need to explicitly configure response
  @GetMapping
  public PostResponse getAllPosts(
          @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
          @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
          @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
          @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
  ) {
    return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
  }

  // get post by id
  @GetMapping("/{id}")
  public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
    return ResponseEntity.ok(postService.getPostById(id));
  }

  // update post by id
  @SecurityRequirement(
          name = "Bearer Authentication"
  )
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
    PostDto postResponse = postService.updatePost(postDto, id);
    return new ResponseEntity<>(postResponse, HttpStatus.OK);
  }

  // delete post by id
  @SecurityRequirement(
          name = "Bearer Authentication"
  )
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
    postService.deletePostById(id);
    return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
  }

  // get posts by category id
  @GetMapping("/category/{id}")
  public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId) {
    List<PostDto> posts = postService.getPostsByCategory(categoryId);
    return ResponseEntity.ok(posts);
  }
}
