package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Category {

    public Category(final String name, final String description) {
        this.setName(name);
        this.setDescription(description);
    }

    public Category(final int id, final String name, final String description) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
    }

    public Category(){}

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "categoriesInExamSet")
    private List<ExamSet> examSets = new LinkedList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExamSet> getExamSets() {
        return examSets;
    }

    public void setExamSets(List<ExamSet> examSets) {
        this.examSets = examSets;
    }
    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
