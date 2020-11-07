package ch.hslu.springbootbackend.springbootbackend.Strategy;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.CategoryController;
import ch.hslu.springbootbackend.springbootbackend.controllers.QuestionController;
import ch.hslu.springbootbackend.springbootbackend.controllers.SchoolClassController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DTOParserExamSet implements DTOParserStrategy{

    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;


    @Override
    public ExamSetDTO generateDTOFromObject(int id) {
        ExamSet examSet = examSetRepository.findById(id).orElseThrow();
        ExamSetDTO examSetDTO = new ExamSetDTO(examSet.getExamSetId(), examSet.getTitle(), examSet.isOpen(), examSet.getStartDate(), examSet.getEndDate());

        examSetDTO.add(linkTo(methodOn(CategoryController.class).getCategoriesByExamSet(examSet.getExamSetId())).withRel("categories"));
        examSetDTO.add(linkTo(methodOn(QuestionController.class).getQuestionsByExamSet(examSet.getExamSetId())).withRel("questionsInExamSet"));
        examSetDTO.add(linkTo(methodOn(SchoolClassController.class).getSchoolClassesByExamSet(examSet.getExamSetId())).withRel("classesInExamSet"));
        return examSetDTO;
    }

    @Override
    public ExamSet generateObjectFromDTO(Object objectDTO) {
        ExamSet examSet = null;
        ExamSetDTO examSetDTO = (ExamSetDTO) objectDTO;

        examSet = new ExamSet(
                this.getCategoriesFromDatabase(examSetDTO.getCategoriesInExamSet()),
                examSetDTO.getTitle(),
                this.getQuestionsFromDatabase(examSetDTO.getQuestionsInExamSet()),
                this.getSchoolClassesFromDatabase(examSetDTO.getSchoolClassesInExamSet()),
                examSetDTO.isOpen(),
                examSetDTO.getStartDate(),
                examSetDTO.getEndDate()
            );

        return examSet;
    }

    @Override
    public List<ExamSetDTO> generateDTOsFromObjects(List list) {
        List<ExamSetDTO> examSetDTOS = new ArrayList<>();
        for(Object examSetDTO:list){
            ExamSetDTO examSet = (ExamSetDTO) examSetDTO;
            examSetDTOS.add(generateDTOFromObject(examSet.getExamSetId()));
        }
        return examSetDTOS;
    }

    private List<Category> getCategoriesFromDatabase(List<Integer> list){
        List<Category> categoryList = new LinkedList<>();
        for(Integer categoryId :list){
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if(categoryOptional.isPresent()){
                categoryList.add(categoryOptional.get());
            }
        }
        return categoryList;
    }

    private List<Question> getQuestionsFromDatabase(List<Integer> list){
        List<Question> questionList = new LinkedList<>();
        for(Integer questionId :list){
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            if(questionOptional.isPresent()){
                questionList.add(questionOptional.get());
            }
        }
        return questionList;
    }

    private List<SchoolClass> getSchoolClassesFromDatabase(List<Integer> list){
        List<SchoolClass> schoolClasses = new LinkedList<>();
        for(Integer schoolClassId :list){
            Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(schoolClassId);
            if(schoolClassOptional.isPresent()){
                schoolClasses.add(schoolClassOptional.get());
            }
        }
        return schoolClasses;
    }
}
