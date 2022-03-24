package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.ParentSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.dto.SearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import at.ac.tuwien.sepm.assignment.individual.entity.ParentSearchParams;
import org.springframework.stereotype.Component;

@Component
public class SearchParamsMapper {
    public OwnerSearchParams dtoToEntity(SearchParamsDto searchParamsDto) {
        return new OwnerSearchParams(
                searchParamsDto.searchTerm(),
                searchParamsDto.resultSize()
        );
    }

    public ParentSearchParams dtoToEntity(ParentSearchParamsDto parentSearchParamsDto) {
        return new ParentSearchParams(
                parentSearchParamsDto.searchTerm(),
                parentSearchParamsDto.sex(),
                parentSearchParamsDto.resultSize()
        );
    }

}
