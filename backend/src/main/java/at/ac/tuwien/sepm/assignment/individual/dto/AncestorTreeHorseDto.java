package at.ac.tuwien.sepm.assignment.individual.dto;

import java.time.LocalDate;

public record AncestorTreeHorseDto(
        Long id,
        String name,
        LocalDate birthdate,
        AncestorTreeHorseDto[] parents
) {
}
