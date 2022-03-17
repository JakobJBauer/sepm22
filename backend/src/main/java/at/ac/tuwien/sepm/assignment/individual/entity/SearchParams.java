package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class SearchParams {
    private String name;
    private String description;
    private LocalDate birthdate;
    private Sex sex;
    private String ownerName;

    public SearchParams() { }

    public SearchParams(String name, String description, LocalDate birthdate, Sex sex, String ownerName) {
        this.name = name;
        this.description = description;
        this.birthdate = birthdate;
        this.sex = sex;
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
