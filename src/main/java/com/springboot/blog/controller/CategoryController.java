package com.springboot.blog.controller;

import com.springboot.blog.dataTransferObject.CategoryDto;
import com.springboot.blog.service.CategoryService;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  // Build Add Category REST API
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
    CategoryDto savedCategory = categoryService.addCategory(categoryDto);
    return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
  }

  // Build Get Category REST API
  @GetMapping("{id}")
  public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId) {
    CategoryDto categoryDto = categoryService.getCategory(categoryId);
    return new ResponseEntity<>(categoryDto, HttpStatus.OK);
  }

  // Build Get ALL Category REST API
  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
  }

  // Build Update Category REST API
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("{id}")
  public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                    @PathVariable("id") Long categoryId) {
    return new ResponseEntity<>(categoryService.updateCategory(categoryDto, categoryId), HttpStatus.OK);
  }

  // Build Delete Category REST API
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId) {
    categoryService.deleteCategory(categoryId);
    return new ResponseEntity<>("Category deleted successfully!", HttpStatus.OK);
  }

}
