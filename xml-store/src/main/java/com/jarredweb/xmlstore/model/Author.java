package com.jarredweb.xmlstore.model;

public class Author {
    
    private Long id;
    private String emailAddr;
    private String firstName;
    private String lastName;

    public Author() {
        super();
    }

    public Author(Long id, String emailAddr, String firstName, String lastName) {
        this();
        this.id = id;
        this.emailAddr = emailAddr;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

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
}
