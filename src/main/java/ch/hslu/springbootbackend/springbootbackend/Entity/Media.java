package ch.hslu.springbootbackend.springbootbackend.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Media {

    // TODO: Für Migration muss "GeneratedValue" auskommentiert werden da IDs übernommen werden sollen!!!
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String path;

    private String type;

    public Media(){}

    public Media(final int id, final String path, final String type){
        this.setId(id);
        this.setPath(path);
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }
}
