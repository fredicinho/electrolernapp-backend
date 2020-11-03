package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategoryDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.CategorySetController;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        List<CategoryDTO> allCategoryDTOs = new ArrayList<>();
        for (Category category : allCategories) {
            CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName(), category.getDescription());
            categoryDTO.add(linkTo(methodOn(CategorySetController.class).getCategorySetByCategoryId(category.getId())).withRel("categorySetInCategory"));
            allCategoryDTOs.add(categoryDTO);
        }
        return allCategoryDTOs;

     }
}
