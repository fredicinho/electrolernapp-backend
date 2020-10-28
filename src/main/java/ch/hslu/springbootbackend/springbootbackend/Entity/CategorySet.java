package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class CategorySet {

    public CategorySet(){};

    public CategorySet(Integer id, Category category, String title, String categorySetNumber) {
        this.categorySetId = id;
        this.category = category;
        this.title = title;
        this.categorySetNumber = categorySetNumber;
    }

    @Id
    private Integer categorySetId;

    // TODO: Für Migration muss "GeneratedValue" auskommentiert werden da IDs übernommen werden sollen!!!
    @OneToOne(targetEntity = Category.class, cascade = CascadeType.ALL)
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Category category;

    private String title;

    private String categorySetNumber;

    @ManyToMany
    @JoinTable(
            name = "categorySet_question",
            joinColumns = @JoinColumn(name = "categorySetId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionsInSet = new LinkedList<>();

    public Integer getCategorySetId() {
        return categorySetId;
    }

    public void setCategorySetId(Integer id) {
        this.categorySetId = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategorySetNumber() {
        return categorySetNumber;
    }

    public void setCategorySetNumber(String categorySetNumber) {
        this.categorySetNumber = categorySetNumber;
    }

    public void insertQuestion(Question question) {
        this.questionsInSet.add(question);
    }

    public void removeQuestion(Question question) {
        this.questionsInSet.remove(question);
    }
    public List<Question> getQuestionsInSet() {
        return questionsInSet;
    }

    public void setQuestionsInSet(List<Question> questions) {
        this.questionsInSet = questions;
    }


    @Override
    public String toString() {
        return "CategorySet{" +
                "id=" + categorySetId +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", categorySetNumber='" + categorySetNumber + '\'' +
                '}';
    }
}
