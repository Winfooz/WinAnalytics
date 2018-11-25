package com.winanalytics.java.sample.models;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaUser2 {

    private String firstName;
    private String lastName;
    private String email;
    private JavaUser3 javaUser3;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public JavaUser3 getJavaUser3() {
        return javaUser3;
    }

    public void setJavaUser3(JavaUser3 javaUser3) {
        this.javaUser3 = javaUser3;
    }
}
