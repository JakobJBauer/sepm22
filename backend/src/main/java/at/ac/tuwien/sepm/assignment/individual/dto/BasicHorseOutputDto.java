package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.Sex;

import java.time.LocalDate;

/**
 * Class for Horse DTOs
 * Contains all common properties
 */
public record BasicHorseOutputDto(
        Long id,
        String name,
        String description,
        LocalDate birthdate,
        Sex sex,
        String ownerName
) { }
