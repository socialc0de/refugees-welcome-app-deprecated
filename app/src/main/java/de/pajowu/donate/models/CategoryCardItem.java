package de.pajowu.donate.models;

/**
 * Created by patricebecker on 12/09/15.
 */
public class CategoryCardItem {
    protected String category;
    protected String image;
    protected String id;

    public CategoryCardItem(String category, String image, String id) {
        this.category = category;
        this.image = image;
        this.id = id;
    }
}