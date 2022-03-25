package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.Sex;

import java.time.LocalDate;

public record MinimalHorseDto(Long id, String name, LocalDate birthdate, Sex sex) {
}
