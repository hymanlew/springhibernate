package com.hyman.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cache(
        usage =  CacheConcurrencyStrategy.READ_WRITE,
        region = "manual-cache",
        include = "all"
        )
public class Emploee {
    private int id;
    private String name;
    private Department department;

    @Cache(
            usage =  CacheConcurrencyStrategy.READ_WRITE,
            region = "manual-cache")
    private IdCard card;

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public IdCard getCard() {
        return card;
    }

    public void setCard(IdCard card) {
        this.card = card;
    }
}
