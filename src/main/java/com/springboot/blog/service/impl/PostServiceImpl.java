package com.springboot.blog.service.impl;

import com.springboot.blog.dataTransferObject.PostDto;
import com.springboot.blog.dataTransferObject.PostResponse;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // indicate a spring bean
public class PostServiceImpl implements PostService {

  private PostRepository postRepository;
  private CategoryRepository categoryRepository;
  private ModelMapper mapper;

  public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, ModelMapper mapper) {
    this.postRepository = postRepository;
    this.categoryRepository = categoryRepository;
    this.mapper = mapper;
  }

  // convert entity to DTO
  private PostDto mapToDTO(Post post) {
    PostDto postDto = mapper.map(post, PostDto.class);
//    PostDto postResponse = new PostDto();
//    postResponse.setId(post.getId());
//    postResponse.setTitle(post.getTitle());
//    postResponse.setDescription(post.getDescription());
//    postResponse.setContent(post.getContent());

    return postDto;
  }

  // convert DTO to entity
  private Post mapToEntity(PostDto postDto) {
    Post post = mapper.map(postDto, Post.class);
//    Post post = new Post();
//    post.setTitle(postDto.getTitle());
//    post.setDescription(postDto.getDescription());
//    post.setContent(postDto.getContent());

    return post;
  }

  @Override
  public PostDto createPost(PostDto postDto) {

    // explicitly retrieve the category id and make sure the category exists
    Category category = categoryRepository.findById(postDto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

    Post post = mapToEntity(postDto);
    post.setCategory(category);

    Post newPost = postRepository.save(post);

    PostDto postResponse = mapToDTO(newPost);
    return postResponse;
  }

  @Override
  public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

    // create a sort object
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<Post> posts = postRepository.findAll(pageable);

    // get content for page object
    List<Post> listOfPosts = posts.getContent();
    List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    PostResponse postResponse = new PostResponse();
    postResponse.setContent(content);
    postResponse.setPageNo(posts.getNumber());
    postResponse.setPageSize(posts.getSize());
    postResponse.setTotalElements(posts.getTotalElements());
    postResponse.setTotalPages(posts.getTotalPages());
    postResponse.setLast(posts.isLast());

    return postResponse;
  }

  @Override
  public PostDto getPostById(long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    return mapToDTO(post);
  }

  @Override
  public PostDto updatePost(PostDto postDto, long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    Category category = categoryRepository.findById(postDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

    post.setTitle(postDto.getTitle());
    post.setDescription(postDto.getDescription());
    post.setContent(postDto.getContent());
    post.setCategory(category);

    Post updatedPost = postRepository.save(post);
    return mapToDTO(updatedPost);
  }

  @Override
  public void deletePostById(long id) {
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    postRepository.delete(post);
  }

  @Override
  public List<PostDto> getPostsByCategory(Long categoryId) {
    Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

    List<Post> posts = postRepository.findByCategoryId(categoryId);

    return posts.stream().map((post) -> mapToDTO(post)).collect(Collectors.toList());
  }

}
