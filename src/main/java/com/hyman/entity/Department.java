package com.hyman.entity;

import java.util.Set;

public class Department {
    private int id;
    private String name;
    private Set<Emploee> emploees;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Emploee> getEmploees() {
        return emploees;
    }

    public void setEmploees(Set<Emploee> emploees) {
        this.emploees = emploees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
