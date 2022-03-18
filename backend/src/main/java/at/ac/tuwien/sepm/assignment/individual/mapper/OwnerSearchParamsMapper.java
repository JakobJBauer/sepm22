package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerSeachParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import org.springframework.stereotype.Component;

@Component
public class OwnerSearchParamsMapper {
    public OwnerSearchParams dtoToEntity(OwnerSeachParamsDto ownerSeachParamsDto) {
        return new OwnerSearchParams(
                ownerSeachParamsDto.searchTerm(),
                ownerSeachParamsDto.resultSize()
        );
    }
}
