package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategoryDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.CategorySetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExamSetRepository examSetRepository;


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

     public CategoryDTO getOneCategory(int categoryId){
        CategoryDTO categoryDTO;
        Category category;
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isPresent()){
            category = categoryOptional.get();
            categoryDTO = new CategoryDTO(categoryId, category.getName(), category.getDescription());
            categoryDTO.add(linkTo(methodOn(CategorySetController.class).getCategorySetByCategoryId(category.getId())).withRel("categorySetInCategory"));

        }else{
            categoryDTO = null;
        }
        return categoryDTO;

     }

     public List<CategoryDTO> getCategoriesByExamSet(int examSetId){
        Optional<ExamSet> examSet = examSetRepository.findById(examSetId);
        List<CategoryDTO> categoryDTOS = new LinkedList<>();
        if(examSet.isPresent()){
            List<Category> categories = categoryRepository.findAllByExamSets(examSet.get());
            for(Category category : categories){
                categoryDTOS.add(new CategoryDTO(category.getId(), category.getName(), category.getDescription()));
            }
        }
        return categoryDTOS;
     }



}
