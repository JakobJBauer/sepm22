package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import org.springframework.stereotype.Component;

@Component
public class HorseSearchParamsMapper {
    public HorseSearchParams dtoToEntity(HorseSearchParamsDto horseSearchParamsDto) {
        return new HorseSearchParams(
                horseSearchParamsDto.name(),
                horseSearchParamsDto.description(),
                horseSearchParamsDto.birthdate(),
                horseSearchParamsDto.sex(),
                horseSearchParamsDto.ownerName()
        );
    }
}
