package ch.hslu.springbootbackend.springbootbackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
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


    public Profession(){}

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

    @PostPersist
    private void assignFKs() {
        for (int i = 0; i < usersInProfession.size(); i++) {
            usersInProfession.get(i).setProfession(this);
        }
    }

}
