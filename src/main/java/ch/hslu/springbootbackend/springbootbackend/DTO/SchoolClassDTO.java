package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

public class SchoolClassDTO extends RepresentationModel<SchoolClassDTO> {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private final int writeInCode = this.hashCode();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> examSetsForSchoolClass = new LinkedList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> usersInClass = new LinkedList<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer institutionId;

    public SchoolClassDTO(){}
    public SchoolClassDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public SchoolClassDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SchoolClassDTO(String name, String description, Integer institutionId) {
        this.name = name;
        this.description = description;
        this.institutionId = institutionId;
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

    public int getWriteInCode() {
        return writeInCode;
    }

    public List<Integer> getExamSetsForSchoolClass() {
        return examSetsForSchoolClass;
    }

    public void setExamSetsForSchoolClass(List<Integer> examSetsForSchoolClass) {
        this.examSetsForSchoolClass = examSetsForSchoolClass;
    }

    public List<Long> getUsersInClass() {
        return usersInClass;
    }

    public void setUsersInClass(List<Long> usersInClass) {
        this.usersInClass = usersInClass;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }


}
