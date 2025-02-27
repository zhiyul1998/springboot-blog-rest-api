package com.springboot.blog.dataTransferObject;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

// can either send an entity back to client or send DTO back to client
// DTO is preferred to hide implementation details of entities
@Data
@Schema( // provide schema/model information to the swagger ui
        description = "PostDto Model Information"
)
public class PostDto {
  private long id;

  @Schema(
          description = "Blog Post Title"
  )
  // title should not be null or empty
  // title should have at least 2 characters
  @NotEmpty
  @Size(min = 2, message = "Post title should have at least 2 characters.")
  private String title;

  @Schema(
          description = "Blog Post Description"
  )
  @NotEmpty
  @Size(min = 10, message = "Post description should have at least 10 characters.")
  private String description;

  @Schema(
          description = "Blog Post Content"
  )
  @NotEmpty
  private String content;

  private Set<CommentDto> comments;

  @Schema(
          description = "Blog Post Category"
  )
  private Long categoryId;
}
