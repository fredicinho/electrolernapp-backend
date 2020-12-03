package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategorySetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.CategoryController;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategorySetService {

    private final CategorySetRepository categorySetRepository;

    public CategorySetService(CategorySetRepository categorySetRepository) {
        this.categorySetRepository = categorySetRepository;
    }

    public CategorySetDTO getCategorySetDTOById(Integer id) throws ResourceNotFoundException {
        CategorySet categorySet = categorySetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Question not found for this id :: " + id )
        );
        CategorySetDTO categorySetDTO = new CategorySetDTO(categorySet.getCategorySetId(), categorySet.getTitle(), categorySet.getCategorySetNumber());
        categorySetDTO.add(linkTo(methodOn(CategoryController.class).getCategoryById(id)).withRel("questionsInSet"));
        categorySetDTO.add(linkTo(methodOn(QuestionController.class).getQuestionsByCategorySet(id)).withRel("questionsInSet"));
        return categorySetDTO;
    }

    public List<CategorySetDTO> getCategorySetByCategoryId (Integer categoryId){
        List<CategorySet> categorySets = categorySetRepository.findByCategoryId(categoryId);
        List<CategorySet> filteredCategorySets = this.filterEmptyCategorySets(categorySets);
        System.out.println("Filtered CategorySets");
        return generateCategorySetDTOFromCatagorySet(filteredCategorySets);
    }

    private List<CategorySetDTO> generateCategorySetDTOFromCatagorySet(List<CategorySet> list){
        List<CategorySetDTO> categorySetDTOS = new ArrayList<>();
        for(CategorySet categorySet:list) {
            CategorySetDTO categorySetDTO = new CategorySetDTO(categorySet.getCategorySetId(), categorySet.getTitle(), categorySet.getCategorySetNumber());
            categorySetDTO.add(linkTo(methodOn(CategoryController.class).getCategoryById(categorySet.getCategorySetId())).withRel("category"));
            categorySetDTO.add(linkTo(methodOn(QuestionController.class).getQuestionsByCategorySet(categorySet.getCategorySetId())).withRel("questionsInSet"));
            categorySetDTOS.add(categorySetDTO);
        }
        return categorySetDTOS;
    }

    public List<CategorySetDTO> getAllCategorySets() {
        List<CategorySet> allCategorySets = categorySetRepository.findAll();
        List<CategorySet> filteredCategorySets = this.filterEmptyCategorySets(allCategorySets);
        return this.generateCategorySetDTOFromCatagorySet(filteredCategorySets);
    }

    private List<CategorySet> filterEmptyCategorySets(List<CategorySet> categorySets) {
        List<CategorySet> filteredCategorySet = new ArrayList<>();
        for (CategorySet categorySet : categorySets
             ) {
            if (!categorySet.getQuestionsInSet().isEmpty()) {
                filteredCategorySet.add(categorySet);
            }
        }
        return filteredCategorySet;
    }

}
