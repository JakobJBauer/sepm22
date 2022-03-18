package at.ac.tuwien.sepm.assignment.individual.dto;

public record OwnerSeachParamsDto(
        String searchTerm,
        Integer resultSize
) {
}
