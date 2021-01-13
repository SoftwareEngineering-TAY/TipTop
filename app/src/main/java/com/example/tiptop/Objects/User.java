package com.example.tiptop.Objects;

import java.io.Serializable;

public class User implements Serializable {

    //Fields
    private String name;
    private String email;
    private String password;
    private String birthday;
    private String type;
    private String currFamilyId;
    private long points;

    //Default constructor
    public User() {
    }

    //Getter
    public long getPoints() {
        return points;
    }

    public String getCurrFamilyId() {
        return currFamilyId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }


    //Setter
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setCurrFamilyId(String currFamilyId) {
        this.currFamilyId = currFamilyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //methods
    @Override
    public String toString() {
        return "User{" +
                "Email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                ", type='" + type + '\'' +
                ", currFamilyId='" + currFamilyId + '\'' +
                '}';
    }
}
