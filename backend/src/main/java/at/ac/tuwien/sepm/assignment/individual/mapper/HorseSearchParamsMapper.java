package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HorseSearchParamsMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorseSearchParamsMapper.class);

    public HorseSearchParams dtoToEntity(HorseSearchParamsDto horseSearchParamsDto) {
        LOGGER.trace("Mapping HorseSearchParamsDto to HorseSearchParams");
        return new HorseSearchParams(
                horseSearchParamsDto.name(),
                horseSearchParamsDto.description(),
                horseSearchParamsDto.birthdate(),
                horseSearchParamsDto.sex(),
                horseSearchParamsDto.ownerName()
        );
    }
}
