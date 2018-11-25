package com.winanalytics.java.sample.models;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaUser3 {

    private String firstName;
    private String lastName;
    private String email;
    private JavaUser4 javaUser4;

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

    public JavaUser4 getJavaUser4() {
        return javaUser4;
    }

    public void setJavaUser4(JavaUser4 javaUser4) {
        this.javaUser4 = javaUser4;
    }
}
