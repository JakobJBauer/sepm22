package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.SearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.SearchParams;
import org.springframework.stereotype.Component;

@Component
public class SearchParamsMapper {
    public SearchParams dtoToEntity(SearchParamsDto searchParamsDto) {
        return new SearchParams(
                searchParamsDto.name(),
                searchParamsDto.description(),
                searchParamsDto.birthdate(),
                searchParamsDto.sex(),
                searchParamsDto.ownerName()
        );
    }
}
