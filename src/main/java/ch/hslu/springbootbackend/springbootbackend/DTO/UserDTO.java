package ch.hslu.springbootbackend.springbootbackend.DTO;

import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;


public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String profession;
    private List<String> roles = new ArrayList<>();
    private List<Integer> schoolClassesIn = new ArrayList<>();

    public UserDTO(Long id, String username, String email, List<String> roles, List<Integer> schoolClassesIn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.schoolClassesIn = schoolClassesIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Integer> getSchoolClassesIn() {
        return schoolClassesIn;
    }

    public void setSchoolClassesIn(List<Integer> schoolClassesIn) {
        this.schoolClassesIn = schoolClassesIn;
    }


}
