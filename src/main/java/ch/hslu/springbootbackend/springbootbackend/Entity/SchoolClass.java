package ch.hslu.springbootbackend.springbootbackend.Entity;

import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import net.bytebuddy.utility.RandomString;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class SchoolClass {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;


    private final String writeInCode = RandomString.make(10);

    @ManyToMany(mappedBy = "schoolClassesInExamSet")
    private List<ExamSet> examSetsForSchoolClass = new LinkedList<>();

    @ManyToMany(mappedBy = "inSchoolClasses", cascade = CascadeType.PERSIST)
    private List<User> usersInClass = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name="Fk_institution", nullable=false)
    private Institution institution;

    public SchoolClass(){}

    public SchoolClass(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @PostPersist
    private void assignFKs(){
        for(int i =0; i < examSetsForSchoolClass.size(); i++){
            examSetsForSchoolClass.get(i).insertSchoolClass(this);
        }
        for(int i =0; i < usersInClass.size(); i++){
            usersInClass.get(i).insertSchoolClass(this);
        }
    }

    public void insertUser(User user){
        this.getUsersInClass().add(user);
    }

    public void insertExamSet(ExamSet examSet){
        this.getExamSetsForSchoolClass().add(examSet);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<ExamSet> getExamSetsForSchoolClass() {
        return examSetsForSchoolClass;
    }

    public void setExamSetsForSchoolClass(List<ExamSet> examSetsForSchoolClass) {
        this.examSetsForSchoolClass = examSetsForSchoolClass;
    }

    public List<User> getUsersInClass() {
        return usersInClass;
    }

    public void setUsersInClass(List<User> usersInClass) {
        this.usersInClass = usersInClass;
    }

    public String getWriteInCode() {
        return writeInCode;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

}
