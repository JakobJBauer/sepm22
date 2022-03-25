package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDate;

public class Horse {
    private Long id;
    private String name;
    private String description;
    private LocalDate birthdate;
    private Sex sex;
    private Owner owner;
    private Long[] parentIds;

    public Horse() {}

    public Horse(String name, String description, LocalDate birthdate, Sex sex, Owner owner, Long[] parentIds) {
        this.name =name;
        this.description = description;
        this.birthdate = birthdate;
        this.sex = sex;
        this.owner = owner;
        this.parentIds = parentIds;
    }

    public Horse(Long id, String name, String description, LocalDate birthdate, Sex sex, Owner owner, Long[] parentIds) {
        this.id = id;
        this.name =name;
        this.description = description;
        this.birthdate = birthdate;
        this.sex = sex;
        this.owner = owner;
        this.parentIds = parentIds;
    }

    public Long[] getParentIds() {
        return parentIds;
    }

    public void setParentIds(Long[] parentIds) {
        this.parentIds = parentIds;
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

    public Owner getOwner() { return owner; }

    public void setOwner(Owner owner) { this.owner = owner; }
}

