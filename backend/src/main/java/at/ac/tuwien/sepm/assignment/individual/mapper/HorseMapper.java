package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.*;
import at.ac.tuwien.sepm.assignment.individual.entity.AncestorTreeHorse;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.SearchHorse;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class HorseMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseMapper.class);

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
        LOGGER.trace("Mapping SearchHorse to SearchHorseDto");
        return new SearchHorseDto(searchHorse.getId(), searchHorse.getName(), searchHorse.getSex());
    }

    public BasicHorseOutputDto entityToBasicDto(Horse horse) {
        LOGGER.trace("Mapping Horse to BasicHorseOutputDto");
        return new BasicHorseOutputDto(
                horse.getId(),
                horse.getName(),
                horse.getDescription(),
                horse.getBirthdate(),
                horse.getSex(),
                horse.getOwner() != null ? horse.getOwner().getFirstName() + " " + horse.getOwner().getLastName() : null
        );
    }

    public AncestorTreeHorseDto entityToDto(AncestorTreeHorse ancestorTreeHorse) {
        LOGGER.trace("Mapping AncestorTreeHorse to AncestorTreeHorseDto");
        return new AncestorTreeHorseDto(
                ancestorTreeHorse.getId(),
                ancestorTreeHorse.getName(),
                ancestorTreeHorse.getBirthdate(),
                Stream.of(ancestorTreeHorse.getParents())
                        .map(this::entityToDto)
                        .toArray(AncestorTreeHorseDto[]::new)
        );
    }

    public FullHorseOutputDto entityToFullDto(Horse horse) {
        LOGGER.trace("Mapping Horse to FullHorseOutputDto");
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
        LOGGER.trace("Mapping BasicHorseInputDto to Horse");
        try {
            return new Horse(
                    id,
                    basicHorseInputDto.name(),
                    basicHorseInputDto.description(),
                    basicHorseInputDto.birthdate(),
                    basicHorseInputDto.sex(),
                    ownerService.getOwnerById(basicHorseInputDto.ownerId()),
                    basicHorseInputDto.parentIds()
            );
        } catch (NoResultException e) {
            LOGGER.debug("No owner in InputDto");
            return new Horse(
                    id,
                    basicHorseInputDto.name(),
                    basicHorseInputDto.description(),
                    basicHorseInputDto.birthdate(),
                    basicHorseInputDto.sex(),
                    null,
                    basicHorseInputDto.parentIds()
            );
        }
    }

    public Horse dtoToEntity(BasicHorseInputDto basicHorseInputDto) {
        LOGGER.trace("Mapping BasicHorseInputDto to Horse");
        try {
            return new Horse(
                    basicHorseInputDto.name(),
                    basicHorseInputDto.description(),
                    basicHorseInputDto.birthdate(),
                    basicHorseInputDto.sex(),
                    ownerService.getOwnerById(basicHorseInputDto.ownerId()),
                    basicHorseInputDto.parentIds()
            );
        } catch (NoResultException e) {
            LOGGER.debug("No owner in InputDto");
            return new Horse(
                    basicHorseInputDto.name(),
                    basicHorseInputDto.description(),
                    basicHorseInputDto.birthdate(),
                    basicHorseInputDto.sex(),
                    null,
                    basicHorseInputDto.parentIds()
            );
        }
    }

    private MinimalHorseDto entityToMinimalDto(Horse horse) {
        LOGGER.trace("Mapping Horse to MinimalHorseDto");
        return new MinimalHorseDto(
                horse.getId(),
                horse.getName(),
                horse.getBirthdate(),
                horse.getSex()
        );
    }
}
