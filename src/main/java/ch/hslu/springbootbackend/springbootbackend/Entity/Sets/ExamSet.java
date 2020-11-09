package ch.hslu.springbootbackend.springbootbackend.Entity.Sets;

import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ExamSet {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer examSetId;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "examSet_question",
            joinColumns = @JoinColumn(name = "examSetId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionsInExamSet = new LinkedList<>();


    @ManyToMany
    @JoinTable(
            name = "examSet_schoolClass",
            joinColumns = @JoinColumn(name = "examSetId"),
            inverseJoinColumns = @JoinColumn(name = "schoolClassId"))
    private List<SchoolClass> schoolClassesInExamSet = new LinkedList<>();

    private Date startDate;
    private Date endDate;

    public ExamSet(){}

    public ExamSet(String title, List<Question> questionsInExamSet, List<SchoolClass> schoolClassesInExamSet, Date startDate, Date endDate) {
        this.title = title;
        this.questionsInExamSet = questionsInExamSet;
        this.schoolClassesInExamSet = schoolClassesInExamSet;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @PostPersist
    private void setFks(){
        for(int i =0; i < questionsInExamSet.size(); i++){
            questionsInExamSet.get(i).insertIntoExamSet(this);
        }
        for(int i =0; i < schoolClassesInExamSet.size(); i++){
            schoolClassesInExamSet.get(i).insertExamSet(this);
        }
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

    public List<Question> getQuestionsInExamSet() {
        return questionsInExamSet;
    }

    public void setQuestionsInExamSet(List<Question> questionsInExamSet) {
        this.questionsInExamSet = questionsInExamSet;
    }

    public void insertSchoolClass(SchoolClass schoolClass){
        this.schoolClassesInExamSet.add(schoolClass);
    }
    public void insertQuestion(Question question){
        this.questionsInExamSet.add(question);
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

    public List<SchoolClass> getSchoolClassesInExamSet() {
        return schoolClassesInExamSet;
    }


    public void setSchoolClassesInExamSet(List<SchoolClass> schoolClassesInExamSet) {
        this.schoolClassesInExamSet = schoolClassesInExamSet;
    }


}
