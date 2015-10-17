package de.pajowu.donate.models;

/**
 * Created by patricebecker on 12/09/15.
 */
public class CategoryCardItem {
    private String category;
    private String image;
    private String id;

    public CategoryCardItem(String category, String image, String id) {
        this.category = category;
        this.image = image;
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}