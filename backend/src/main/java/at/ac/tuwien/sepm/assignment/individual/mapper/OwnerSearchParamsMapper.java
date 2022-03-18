package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import org.springframework.stereotype.Component;

@Component
public class OwnerSearchParamsMapper {
    public OwnerSearchParams dtoToEntity(OwnerSearchParamsDto ownerSearchParamsDto) {
        return new OwnerSearchParams(
                ownerSearchParamsDto.searchTerm(),
                ownerSearchParamsDto.resultSize()
        );
    }
}
