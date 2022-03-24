package at.ac.tuwien.sepm.assignment.individual.dto;

public record SearchParamsDto(
        String searchTerm,
        Integer resultSize
) {
}
