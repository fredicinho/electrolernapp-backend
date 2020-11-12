package ch.hslu.springbootbackend.springbootbackend.DTO;

import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedList;
import java.util.List;

public class InstitutionDTO extends RepresentationModel<InstitutionDTO> {
    private int id;
    private String name;
    private List<Integer> schoolClassIds = new LinkedList<>();

    public InstitutionDTO(String name){
        this.name = name;
    }
    public InstitutionDTO(int id, String name) {
        this.id = id;
        this.name = name;
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


}
