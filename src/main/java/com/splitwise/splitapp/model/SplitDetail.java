package com.splitwise.splitapp.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class SplitDetail {

    private String person;
    private double value;

    public SplitDetail() {}

    public SplitDetail(String person, double value) {
        this.person = person;
        this.value = value;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
