package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedList;
import java.util.List;

public class InstitutionDTO extends RepresentationModel<InstitutionDTO> {
    private int id;
    private String name;
    private String description;
    private String place;
    private String postcode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> schoolClassIds = new LinkedList<>();

    public InstitutionDTO(int id, String name, String description, String place, String postcode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.postcode = postcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getSchoolClassIds() {
        return schoolClassIds;
    }

    public void setSchoolClassIds(List<Integer> schoolClassIds) {
        this.schoolClassIds = schoolClassIds;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

}
