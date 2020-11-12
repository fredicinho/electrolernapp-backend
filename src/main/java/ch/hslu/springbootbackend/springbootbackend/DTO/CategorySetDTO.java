package ch.hslu.springbootbackend.springbootbackend.DTO;

import org.springframework.hateoas.RepresentationModel;

public class CategorySetDTO extends RepresentationModel<CategorySetDTO> {
    public CategorySetDTO(Integer categorySetId, String title, String categorySetNumber){
        this.categorySetId = categorySetId;
        this.title = title;
        this.categorySetNumber = categorySetNumber;
    }
    private Integer categorySetId;

    private String title;

    private final String categorySetNumber;

    public Integer getCategorySetId() {
        return categorySetId;
    }

    public void setCategorySetId(Integer categorySetId) {
        this.categorySetId = categorySetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
