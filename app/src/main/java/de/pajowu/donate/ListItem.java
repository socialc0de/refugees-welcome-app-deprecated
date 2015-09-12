package de.pajowu.donate;


public class ListItem {
    public String resourceImage;
    public int image;
    public String title;
    public String subtitle;
    public String category;
    public long primaryKey;

    public ListItem(String imageUrl, String title, String subtitle, String category, long primaryKey){
        this.resourceImage = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.primaryKey = primaryKey;
    }

    public ListItem(int imageUrl, String title, String subtitle, String category, long primaryKey){
        this.image = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.primaryKey = primaryKey;
    }


}
