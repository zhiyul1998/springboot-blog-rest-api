package com.springboot.blog.dataTransferObject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// we need to send client more information instead of just posts on the required page
// this class represents what we need to send in a response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private List<PostDto> content;
  private int pageNo;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean last;
}
