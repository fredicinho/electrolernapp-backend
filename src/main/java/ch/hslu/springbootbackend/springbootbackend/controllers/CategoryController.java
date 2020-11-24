package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategoryDTO;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categories")
@PreAuthorize("hasAnyRole()")
public class CategoryController {
    private final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable(value = "id") Integer categoryId){
            CategoryDTO categoryDTO = categoryService.getOneCategory(categoryId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryDTO);
    }

    @GetMapping("")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

}
