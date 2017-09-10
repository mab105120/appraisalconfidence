package edu.grenoble.em.bourji.db.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Moe on 9/9/2017.
 */
@Entity
@Table(name = "USER_DEMOGRAPHIC")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDemographic {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String user;
    @Column(name = "AGE", nullable = false, length = 10)
    private int age;
    @Column(name = "GENDER", nullable = false, length = 10)
    private String gender;
    @Column(name = "EDUCATION", nullable = false, length = 64)
    private String education;
    @Column(name = "DIVISION", nullable = false, length = 64)
    private String division;

    public UserDemographic() {
        // no-arg constructor for hibernate
    }

    public UserDemographic(String user, int age, String gender, String education, String division) {
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.education = education;
        this.division = division;
    }

    public String getUser() {
        return user;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEducation() {
        return education;
    }

    public String getDivision() {
        return division;
    }

    public void setUser(String user) {
        this.user = user;
    }
}