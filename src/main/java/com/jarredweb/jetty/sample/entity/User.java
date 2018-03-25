package com.jarredweb.jetty.sample.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    
    public String username;
    public Address address;
    public List<Phone> phones = new ArrayList<>();
}
