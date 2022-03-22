package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.Sex;

import java.time.LocalDate;

public record FullHorseOutputDto(
        Long id,
        String name,
        String description,
        LocalDate birthdate,
        Sex sex,
        BasicOwnerDto owner
) {
}
