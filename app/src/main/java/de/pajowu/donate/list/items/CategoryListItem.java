package de.pajowu.donate.list.items;


public class CategoryListItem {
    private String imageUrl;
    private String name;
    private String description;
    private String id;

    public CategoryListItem(String imageUrl, String name, String description, String id) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
