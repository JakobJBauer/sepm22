package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.*;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.SearchHorse;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HorseMapper {

    private final OwnerMapper ownerMapper;
    private final OwnerService ownerService;
    private final HorseService horseService;

    public HorseMapper(
            OwnerMapper ownerMapper,
            OwnerService ownerService,
            HorseService horseService
    ) {
        this.ownerMapper = ownerMapper;
        this.ownerService = ownerService;
        this.horseService = horseService;
    }

    public SearchHorseDto entityToSearchHorseDto(SearchHorse searchHorse) {
        return new SearchHorseDto(searchHorse.getId(), searchHorse.getName(), searchHorse.getSex());
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
        var parents = horse.getParentIds() != null ?
                Arrays.stream(horse.getParentIds())
                        .map(horseService::getHorseById)
                        .map(this::entityToMinimalDto)
                        .toArray(MinimalHorseDto[]::new)
                : new MinimalHorseDto[0];

        return new FullHorseOutputDto(
                horse.getId(),
                horse.getName(),
                horse.getDescription(),
                horse.getBirthdate(),
                horse.getSex(),
                ownerMapper.entityToBasicDto(horse.getOwner()),
                parents
        );
    }

    public Horse dtoToEntity(BasicHorseInputDto basicHorseInputDto, long id) {
        return new Horse(
                id,
                basicHorseInputDto.name(),
                basicHorseInputDto.description(),
                basicHorseInputDto.birthdate(),
                basicHorseInputDto.sex(),
                ownerService.getOwnerById(basicHorseInputDto.ownerId()),
                basicHorseInputDto.parentIds()
        );
    }

    public Horse dtoToEntity(BasicHorseInputDto basicHorseInputDto) {
        return new Horse(
                basicHorseInputDto.name(),
                basicHorseInputDto.description(),
                basicHorseInputDto.birthdate(),
                basicHorseInputDto.sex(),
                ownerService.getOwnerById(basicHorseInputDto.ownerId()),
                basicHorseInputDto.parentIds()
        );
    }

    private MinimalHorseDto entityToMinimalDto(Horse horse) {
        return new MinimalHorseDto(
                horse.getId(),
                horse.getName(),
                horse.getBirthdate(),
                horse.getSex()
        );
    }
}
