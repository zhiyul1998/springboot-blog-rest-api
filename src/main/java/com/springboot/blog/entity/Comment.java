package com.springboot.blog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // equivalent to @Getter, @Setter, @RequiredArgsConstructor, @ToString, and @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;
  private String email;
  private String body;

  @ManyToOne(fetch = FetchType.LAZY) // one post has multiple comments (FetchType tells Hibernate to only fetch the related entities from the database when use the relationship)
  @JoinColumn(name = "post_id", nullable = false) // identify foreign key
  private Post post;
}
