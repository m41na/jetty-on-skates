package com.jarredweb.jesty.todos;

public class User {

    public String email;
    public String fname;
    public String lname;
    public String phone;

    public User() {
        super();
    }

    public User(String email, String fname, String lname, String phone) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }
}
