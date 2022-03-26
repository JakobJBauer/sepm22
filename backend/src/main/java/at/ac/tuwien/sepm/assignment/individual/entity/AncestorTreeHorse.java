package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDate;

public class AncestorTreeHorse {
    private Long id;
    private String name;
    private LocalDate birthdate;
    private AncestorTreeHorse[] parents;

    public AncestorTreeHorse(Long id, String name, LocalDate birthdate, AncestorTreeHorse[] parents) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.parents = parents;
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public AncestorTreeHorse[] getParents() {
        return parents;
    }

    public void setParents(AncestorTreeHorse[] parents) {
        this.parents = parents;
    }
}
