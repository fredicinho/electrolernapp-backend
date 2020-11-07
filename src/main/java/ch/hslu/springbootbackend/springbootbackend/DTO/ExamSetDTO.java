package ch.hslu.springbootbackend.springbootbackend.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExamSetDTO extends RepresentationModel<ExamSetDTO> {


    private Integer examSetId;
    private String title;
    private boolean isOpen;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;

    private List<Integer> categoriesInExamSet = new LinkedList<>();
    private List<Integer> questionsInExamSet = new LinkedList<>();
    private List<Integer> schoolClassesInExamSet = new LinkedList<>();

    public ExamSetDTO(){}
    public ExamSetDTO(Integer examSetId, String title, boolean isOpen, Date startDate, Date endDate) {
        this.examSetId = examSetId;
        this.title = title;
        this.isOpen = isOpen;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public Integer getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(Integer examSetId) {
        this.examSetId = examSetId;
    }

    public List<Integer> getCategoriesInExamSet() {
        return categoriesInExamSet;
    }

    public void setCategoriesInExamSet(List<Integer> categoriesInExamSet) {
        this.categoriesInExamSet = categoriesInExamSet;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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



}
