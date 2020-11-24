package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExamSetDTO extends RepresentationModel<ExamSetDTO> {


    private Integer examSetId;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> questionsInExamSet = new LinkedList<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> schoolClassesInExamSet = new LinkedList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    private String description;

    public ExamSetDTO(){}
    public ExamSetDTO(Integer examSetId, String title, Date startDate, Date endDate, String description) {
        this.examSetId = examSetId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }


    public Integer getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(Integer examSetId) {
        this.examSetId = examSetId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getQuestionsInExamSet() {
        return questionsInExamSet;
    }

    public void setQuestionsInExamSet(List<Integer> questionsInExamSet) {
        this.questionsInExamSet = questionsInExamSet;
    }

    public List<Integer> getSchoolClassesInExamSet() {
        return schoolClassesInExamSet;
    }

    public void setSchoolClassesInExamSet(List<Integer> schoolClassesInExamSet) {
        this.schoolClassesInExamSet = schoolClassesInExamSet;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String username) {
        this.createdBy = username;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
