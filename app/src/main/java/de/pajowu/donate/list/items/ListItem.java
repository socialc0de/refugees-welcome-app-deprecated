package de.pajowu.donate.list.items;


public class ListItem {
    private String resourceImage;
    private int image;
    private String title;
    private String subtitle;
    private String category;
    private long primaryKey;

    public ListItem(String imageUrl, String title, String subtitle, String category, long primaryKey) {
        this.resourceImage = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.primaryKey = primaryKey;
    }

    public ListItem(int imageUrl, String title, String subtitle, String category, long primaryKey) {
        this.image = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.primaryKey = primaryKey;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }
}
