package com.winanalytics.java.sample.models;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaUser4 {

    private String firstName;
    private String lastName;
    private String email;
    private JavaUser javaUser;

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

    public JavaUser getJavaUser() {
        return javaUser;
    }

    public void setJavaUser(JavaUser javaUser) {
        this.javaUser = javaUser;
    }
}
