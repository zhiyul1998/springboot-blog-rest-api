package com.springboot.blog.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // whenever we load user entity, its roles will also be loaded.
                                                                  // whenever we perform on the parent(users), its children(roles) will all be affected.
  @JoinTable(name = "users_roles", // gives the table name of the third table
          joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // gives the column name of the third table and which column in the users table it is referencing to.
          inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) // gives the column name of the third table and which column in the roles table it is referencing to.
  private Set<Role> roles;
}
