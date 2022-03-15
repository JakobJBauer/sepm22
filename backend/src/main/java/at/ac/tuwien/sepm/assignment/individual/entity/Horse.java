package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDate;

public class Horse {
    private Long id;
    private String name;
    private String description;
    private LocalDate birthdate;
    private Sex sex;
    private String owner;

    public Horse() {}

    public Horse(Long id, String name, String description, LocalDate birthdate, Sex sex, String owner) {
        this.id = id;
        this.name =name;
        this.description = description;
        this.birthdate = birthdate;
        this.sex = sex;
        this.owner = owner;
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

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDate getBirthdate() { return birthdate; }

    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public Sex getSex() { return sex; }

    public void setSex(Sex sex) { this.sex = sex; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }
}

