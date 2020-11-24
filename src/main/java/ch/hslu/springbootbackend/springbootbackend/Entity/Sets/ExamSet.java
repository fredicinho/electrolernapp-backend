package ch.hslu.springbootbackend.springbootbackend.Entity.Sets;

import ch.hslu.springbootbackend.springbootbackend.Entity.ExamResult;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.SchoolClass;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;

import javax.persistence.*;
import java.util.*;

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

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User createdByUser;

    private Date startDate;
    private Date endDate;

    private String description;

    @OneToMany(targetEntity = ExamResult.class, cascade = CascadeType.ALL)
    private Set<ExamResult> examResults = new HashSet<>();

    public ExamSet(){}

    public ExamSet(String title, List<Question> questionsInExamSet, List<SchoolClass> schoolClassesInExamSet, Date startDate, Date endDate, User user, String description) {
        this.title = title;
        this.questionsInExamSet = questionsInExamSet;
        this.schoolClassesInExamSet = schoolClassesInExamSet;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdByUser = user;
        this.description = description;
    }

    @PostPersist
    private void setFks(){
        for(int i =0; i < questionsInExamSet.size(); i++){
            questionsInExamSet.get(i).insertIntoExamSet(this);
        }
        for(int i =0; i < schoolClassesInExamSet.size(); i++){
            schoolClassesInExamSet.get(i).insertExamSet(this);
        }
        this.createdByUser.getCreatedExamSets().add(this);
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

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Set<ExamResult> getExamResults() {
        return examResults;
    }

    public void setExamResults(Set<ExamResult> examResults) {
        this.examResults = examResults;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
