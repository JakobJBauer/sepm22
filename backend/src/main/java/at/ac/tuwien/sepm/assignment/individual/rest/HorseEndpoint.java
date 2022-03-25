package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.*;
import at.ac.tuwien.sepm.assignment.individual.entity.ParentSearchParams;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseSearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.SearchParamsMapper;
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
    private final SearchParamsMapper parentSearchMapper;

    public HorseEndpoint(HorseService service, HorseMapper mapper, HorseSearchParamsMapper searchMapper, SearchParamsMapper parentSearchMapper) {
        this.service = service;
        this.mapper = mapper;
        this.searchMapper = searchMapper;
        this.parentSearchMapper = parentSearchMapper;
    }

    @GetMapping
    public Stream<BasicHorseOutputDto> allHorses(HorseSearchParamsDto horseSearchParamsDto) {
        return service.allHorses(searchMapper.dtoToEntity(horseSearchParamsDto)).stream()
                .map(mapper::entityToBasicDto);
    }

    @GetMapping(path = "/parentsearch")
    public Stream<SearchHorseDto> getParentOptions(ParentSearchParamsDto parentSearchParamsDto) {
        return service.parentOptions(parentSearchMapper.dtoToEntity(parentSearchParamsDto))
                .stream().map(mapper::entityToSearchHorseDto);
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
