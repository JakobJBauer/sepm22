package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.Sex;

public record ParentSearchParamsDto(
        Long id,
        String searchTerm,
        Sex sex,
        Integer resultSize
) {
}
