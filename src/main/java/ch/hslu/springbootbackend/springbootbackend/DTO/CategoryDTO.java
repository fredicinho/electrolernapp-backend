package ch.hslu.springbootbackend.springbootbackend.DTO;

import org.springframework.hateoas.RepresentationModel;

public class CategoryDTO extends RepresentationModel<CategoryDTO> {

    private String name;
    private String description;
    private Integer categoryId;

    public CategoryDTO(Integer categoryId, String name, String description){
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
