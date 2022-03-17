package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.Sex;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SearchParamsDto(
        String name,
        String description,
        @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate birthdate,
        Sex sex,
        String ownerName
) {
}
