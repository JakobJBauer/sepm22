package at.ac.tuwien.sepm.assignment.individual.dto;

public record OwnerSearchParamsDto(
        String searchTerm,
        Integer resultSize
) {
}
