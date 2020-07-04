package Model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

public class Pet implements Serializable {

    public Pet() {

    }

    public Pet(Integer id) {
        super();
        this.id = id;
    }

    private int id;
    private Category category;
    private String name;
    private String[] photoUrls;
    private List<Tags> tags;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String[] photoUrls) {
        this.photoUrls = photoUrls;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
