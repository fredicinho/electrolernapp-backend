package ch.hslu.springbootbackend.springbootbackend.Entity.Sets;

import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ExamSet {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer examSetId;

    @ManyToMany
    @JoinTable(
            name = "examSet_categories",
            joinColumns = @JoinColumn(name = "examSetId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private List<Category> categoriesInExamSet = new LinkedList<>();
    private String title;

    @ManyToMany
    @JoinTable(
            name = "examSet_question",
            joinColumns = @JoinColumn(name = "examSetId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionsInExamSet = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "examSet_schoolClass",
            joinColumns = @JoinColumn(name = "examSetId"),
            inverseJoinColumns = @JoinColumn(name = "schoolClassId"))
    private List<SchoolClass> schoolClassesInExamSet = new LinkedList<>();

    private boolean isOpen;

    public Integer getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(Integer examSetId) {
        this.examSetId = examSetId;
    }

    public List<Category> getCategoriesInExamSet() {
        return categoriesInExamSet;
    }

    public void setCategoriesInExamSet(List<Category> category) {
        this.categoriesInExamSet = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestionsInExamSet() {
        return questionsInExamSet;
    }

    public void setQuestionsInExamSet(List<Question> questionsInExamSet) {
        this.questionsInExamSet = questionsInExamSet;
    }

    public void insertSchoolClass(SchoolClass schoolClass){
        this.schoolClassesInExamSet.add(schoolClass);
    }
    public void insertQuestion(Question question){
        this.questionsInExamSet.add(question);
    }
    public void insertCategory(Category category){
        this.categoriesInExamSet.add(category);
    }


}
