package de.pajowu.donate;


public class Person {
    public String name;
    public String city;
    public String email;
    public String profileImage;
    public String[] offers;
    public String phone;
    public String url;

    public Person(String name, String city, String email, String phone, String url, String profileImage, String[] offers){
        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.profileImage = profileImage;
        this.offers = offers;
    }
    public Person(String name, String city, String email, String phone, String url, String profileImage){
        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.profileImage = profileImage;
    }
    public Person(){
    }

}
