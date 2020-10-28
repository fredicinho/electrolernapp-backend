package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.CategorySetDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.CategorySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/categorySet")
public class CategorySetController {

@Autowired
    CategorySetService categorySetService;

    @GetMapping("/{id}")
    public ResponseEntity<CategorySetDTO> getCategorySetById(@PathVariable(value = "id") Integer categorySetId){
        try {

            CategorySetDTO categorySetDTO = categorySetService.getCategorySetDTOById(categorySetId);
            //CategorySet categorySet = categorySetService.getCategorySetId(categorySetId);
           /* if (categorySetService.getCategorySetId(categorySetId).getQuestionsInSet().size() > 0) {
                Link ordersLink = linkTo(methodOn(QuestionController.class)
                        .getQuestionsByCategorySet(categorySetId)).withRel("allQuestions");
                categorySet.add(ordersLink);
            }*/
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
