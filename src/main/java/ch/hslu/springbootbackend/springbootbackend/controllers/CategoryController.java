package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategoryDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {


    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(value = "id") Integer categoryId){
        try{
            Category foundedCategory = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new ResourceNotFoundException("Question not found for this id :: " + categoryId)
            );
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(foundedCategory);
        } catch (ResourceNotFoundException ex) {
            System.out.println("Resource was not found: " + ex.getMessage());
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }

    @GetMapping("")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
