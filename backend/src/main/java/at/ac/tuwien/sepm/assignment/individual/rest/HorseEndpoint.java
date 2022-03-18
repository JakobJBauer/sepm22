package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
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
    public Stream<HorseDto> allHorses(HorseSearchParamsDto horseSearchParamsDto) {
        return service.allHorses(searchMapper.dtoToEntity(horseSearchParamsDto)).stream()
                .map(mapper::entityToDto);
    }

    @GetMapping(path = "/{horseId}")
    public HorseDto getHorseById(@PathVariable("horseId") long horseId) {
        try {
            return mapper.entityToDto(service.getHorseById(horseId));
        } catch (NoResultException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PostMapping
    public HorseDto createHorse(@RequestBody HorseDto horseDto) {
        return mapper.entityToDto(
                service.createHorse(
                        mapper.dtoToEntity(
                                horseDto
                        )
                )
        );
    }

    @PutMapping(path = "/{horseId}")
    public HorseDto updateHorse(
            @RequestBody HorseDto horseDto,
            @PathVariable("horseId") long horseId
    ) {
        try {
            return mapper.entityToDto(
                    service.updateHorse(
                            mapper.dtoToEntity(
                                    horseDto,
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
