package com.github.socialc0de.gsw.android.models;


public class Person {
    private String name;
    private String city;
    private String email;
    private String profileImage;
    private String[] offers;
    private String phone;
    private String url;

    public Person(String name, String city, String email, String phone, String url, String profileImage, String[] offers) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.profileImage = profileImage;
        this.offers = offers;
    }

    public Person(String name, String city, String email, String phone, String url, String profileImage) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.url = url;
        this.profileImage = profileImage;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String[] getOffers() {
        return offers;
    }

    public void setOffers(String[] offers) {
        this.offers = offers;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
