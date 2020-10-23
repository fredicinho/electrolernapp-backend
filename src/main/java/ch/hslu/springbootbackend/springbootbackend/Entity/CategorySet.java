package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.*;

@Entity
public class CategorySet {

    public CategorySet(){};

    public CategorySet(int id, Category category, String title, String categorySetNumber) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.categorySetNumber = categorySetNumber;
    }

    @Id
    private Integer id;

    // TODO: Für Migration muss "GeneratedValue" auskommentiert werden da IDs übernommen werden sollen!!!
    @OneToOne(targetEntity = Category.class, cascade = CascadeType.ALL)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Category category;

    private String title;

    private String categorySetNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "CategorySet{" +
                "id=" + id +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", categorySetNumber='" + categorySetNumber + '\'' +
                '}';
    }
}