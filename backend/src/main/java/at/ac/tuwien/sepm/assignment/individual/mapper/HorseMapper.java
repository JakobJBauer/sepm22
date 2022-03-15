package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {

    public HorseDto entityToDto(Horse horse) {
        return new HorseDto(
                horse.getId(),
                horse.getName(),
                horse.getDescription(),
                horse.getBirthdate(),
                horse.getSex(),
                horse.getOwner()
        );
    }

    public Horse dtoToEntity(HorseDto horseDto, long id) {
        return new Horse(
                id,
                horseDto.name(),
                horseDto.description(),
                horseDto.birthdate(),
                horseDto.sex(),
                horseDto.owner()
        );
    }

    public Horse dtoToEntity(HorseDto horseDto) {
        return new Horse(
                horseDto.name(),
                horseDto.description(),
                horseDto.birthdate(),
                horseDto.sex(),
                horseDto.owner()
        );
    }
}
