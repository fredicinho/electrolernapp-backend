package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategorySetDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.CategorySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categorySet")
public class CategorySetController {

@Autowired
    CategorySetService categorySetService;

    @GetMapping("/category/{id}")
    public List<CategorySetDTO> getCategorySetByCategoryId(@PathVariable(value = "id") Integer categoryId){
        return categorySetService.getCategorySetByCategoryId(categoryId);
    }



    @GetMapping("/{id}")
    public ResponseEntity<CategorySetDTO> getCategorySetById(@PathVariable(value = "id") Integer categorySetId){
        try {

            CategorySetDTO categorySetDTO = categorySetService.getCategorySetDTOById(categorySetId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(categorySetDTO);

        } catch (ResourceNotFoundException ex) {
            System.out.println("Resource was not found: " + ex.getMessage());
            return ResponseEntity.
                    notFound()
                    .build();
        }
    }
}
