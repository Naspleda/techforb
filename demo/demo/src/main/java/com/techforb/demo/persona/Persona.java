package com.techforb.demo.persona;

import jakarta.persistence.*;

@Entity
@Table
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String numcard;
    private float salary;
    private String pwcard;

    public Persona() {
    }

    public Persona(Long id, String name, String numcard, float salary, String pwcard) {
        this.id = id;
        this.name = name;
        this.numcard = numcard;
        this.salary = salary;
        this.pwcard = pwcard;
    }

    public Persona(String name, String numcard, float salary, String pwcard) {
        this.name = name;
        this.numcard = numcard;
        this.salary = salary;
        this.pwcard = pwcard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumcard() {
        return numcard;
    }

    public void setNumcard(String numcard) {
        this.numcard = numcard;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getPwcard() {
        return pwcard;
    }

    public void setPwcard(String pwcard) {
        this.pwcard = pwcard;
    }
}
