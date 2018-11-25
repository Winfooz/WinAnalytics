package com.winanalytics.java.sample.models;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaUser1 {

    private String firstName;
    private String lastName;
    private String email;
    private JavaUser2 javaUser2;

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

    public JavaUser2 getJavaUser2() {
        return javaUser2;
    }

    public void setJavaUser2(JavaUser2 javaUser2) {
        this.javaUser2 = javaUser2;
    }
}
