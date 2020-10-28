package ch.hslu.springbootbackend.springbootbackend.DTO;

import org.springframework.hateoas.RepresentationModel;

public class CategorySetDTO extends RepresentationModel<CategorySetDTO> {
    public CategorySetDTO(Integer categorySetId, String title, String categorySetNumber){
        this.categorySetId = categorySetId;
        this.title = title;
    }
    private Integer categorySetId;

    //private Category category;

    private String title;

    private String categorySetNumber;

    //private List<Long> questionsInSet = new LinkedList<>();

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
