package ch.hslu.springbootbackend.springbootbackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Profession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @JsonIgnore
    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private List<User> usersInProfession = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "profession_question",
            joinColumns = @JoinColumn(name = "professionId"),
            inverseJoinColumns = @JoinColumn(name = "questionId"))
    private List<Question> questionsInProfession = new LinkedList<>();

    public Profession(){}
    public Profession(String name){
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

    public List<User> getUsersInProfession() {
        return usersInProfession;
    }

    public void setUsersInProfession(List<User> usersInProfession) {
        this.usersInProfession = usersInProfession;
    }


    public List<Question> getQuestionsInProfession() {
        return questionsInProfession;
    }

    public void setQuestionsInProfession(List<Question> questionsInProfession) {
        this.questionsInProfession = questionsInProfession;
    }

    public void insertQuestion(Question question){
        this.questionsInProfession.add(question);
    }

}
