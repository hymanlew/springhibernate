package com.hyman.entity;

public class IdCard {
    private int id;
    private String idententy;
    private Emploee emploee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdententy() {
        return idententy;
    }

    public void setIdententy(String idententy) {
        this.idententy = idententy;
    }

    public Emploee getEmploee() {
        return emploee;
    }

    public void setEmploee(Emploee emploee) {
        this.emploee = emploee;
    }
}
