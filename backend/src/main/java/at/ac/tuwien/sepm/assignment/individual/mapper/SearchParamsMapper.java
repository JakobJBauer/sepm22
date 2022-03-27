package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.ParentSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.dto.SearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import at.ac.tuwien.sepm.assignment.individual.entity.ParentSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SearchParamsMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchParamsMapper.class);

    public OwnerSearchParams dtoToEntity(SearchParamsDto searchParamsDto) {
        LOGGER.trace("Mapping SearchParamsDto to OwnerSearchParams");
        return new OwnerSearchParams(
                searchParamsDto.searchTerm(),
                searchParamsDto.resultSize()
        );
    }

    public ParentSearchParams dtoToEntity(ParentSearchParamsDto parentSearchParamsDto) {
        LOGGER.trace("Mapping ParentSearchParamsDto to ParentSearchParams");
        return new ParentSearchParams(
                parentSearchParamsDto.searchTerm(),
                parentSearchParamsDto.sex(),
                parentSearchParamsDto.resultSize()
        );
    }

}
