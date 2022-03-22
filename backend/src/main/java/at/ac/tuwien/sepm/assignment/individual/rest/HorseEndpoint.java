package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseInputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.FullHorseOutputDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.BasicHorseOutputDto;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseSearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/horses")
public class HorseEndpoint {
    private final HorseService service;
    private final HorseMapper mapper;
    private final HorseSearchParamsMapper searchMapper;

    public HorseEndpoint(HorseService service, HorseMapper mapper, HorseSearchParamsMapper searchMapper) {
        this.service = service;
        this.mapper = mapper;
        this.searchMapper = searchMapper;
    }

    @GetMapping
    public Stream<BasicHorseOutputDto> allHorses(HorseSearchParamsDto horseSearchParamsDto) {
        return service.allHorses(searchMapper.dtoToEntity(horseSearchParamsDto)).stream()
                .map(mapper::entityToBasicDto);
    }

    @GetMapping(path = "/{horseId}")
    public FullHorseOutputDto getHorseById(@PathVariable("horseId") long horseId) {
        try {
            return mapper.entityToFullDto(service.getHorseById(horseId));
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PostMapping
    public BasicHorseOutputDto createHorse(@RequestBody BasicHorseInputDto basicHorseInputDto) {
        return mapper.entityToBasicDto(
                service.createHorse(
                        mapper.dtoToEntity(
                                basicHorseInputDto
                        )
                )
        );
    }

    @PutMapping(path = "/{horseId}")
    public FullHorseOutputDto updateHorse(
            @RequestBody BasicHorseInputDto basicHorseInputDto,
            @PathVariable("horseId") long horseId
    ) {
        try {
            return mapper.entityToFullDto(
                    service.updateHorse(
                            mapper.dtoToEntity(
                                    basicHorseInputDto,
                                    horseId
                            )
                    )
            );
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found", e
            );
        }
    }

    @DeleteMapping(path="/{horseId}")
    public void deleteHorseById(
            @PathVariable("horseId") long horseId
    ) {
        try {
            service.deleteHorseById(horseId);
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found", e
            );
        }
    }
}
