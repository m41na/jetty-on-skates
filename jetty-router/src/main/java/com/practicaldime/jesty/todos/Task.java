package com.practicaldime.jesty.todos;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
