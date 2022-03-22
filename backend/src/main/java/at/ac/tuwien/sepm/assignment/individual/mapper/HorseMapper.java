package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseInputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseOutputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.FullHorseOutputDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {

    private final OwnerMapper ownerMapper;
    private final OwnerService ownerService;

    public HorseMapper(
            OwnerMapper ownerMapper,
            OwnerService ownerService
    ) {
        this.ownerMapper = ownerMapper;
        this.ownerService = ownerService;
    }

    public BasicHorseOutputDto entityToBasicDto(Horse horse) {
        return new BasicHorseOutputDto(
                horse.getId(),
                horse.getName(),
                horse.getDescription(),
                horse.getBirthdate(),
                horse.getSex(),
                horse.getOwner() != null ? horse.getOwner().getFirstName() + " " + horse.getOwner().getLastName(): null
        );
    }

    public FullHorseOutputDto entityToFullDto(Horse horse) {
        return new FullHorseOutputDto(
                horse.getId(),
                horse.getName(),
                horse.getDescription(),
                horse.getBirthdate(),
                horse.getSex(),
                ownerMapper.entityToBasicDto(horse.getOwner())
        );
    }

    public Horse dtoToEntity(BasicHorseInputDto basicHorseInputDto, long id) {
        return new Horse(
                id,
                basicHorseInputDto.name(),
                basicHorseInputDto.description(),
                basicHorseInputDto.birthdate(),
                basicHorseInputDto.sex(),
                ownerService.getOwnerById(basicHorseInputDto.ownerId())
        );
    }

    public Horse dtoToEntity(BasicHorseInputDto basicHorseInputDto) {
        return new Horse(
                basicHorseInputDto.name(),
                basicHorseInputDto.description(),
                basicHorseInputDto.birthdate(),
                basicHorseInputDto.sex(),
                ownerService.getOwnerById(basicHorseInputDto.ownerId())
        );
    }
}
