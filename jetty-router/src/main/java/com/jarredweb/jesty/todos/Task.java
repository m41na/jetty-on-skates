package com.jarredweb.jesty.todos;

import java.util.Date;

public class Task {
    
    public String name;
    public Boolean completed;
    public Date created;

    public Task() {
        super();
    }

    public Task(String name, Boolean completed, Date created) {
        this.name = name;
        this.completed = completed;
        this.created = created;
    }
}
