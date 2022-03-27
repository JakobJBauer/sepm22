package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.*;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
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
    @ResponseStatus(HttpStatus.OK)
    public Stream<BasicHorseOutputDto> allHorses(HorseSearchParamsDto horseSearchParamsDto) {
        try {
            return service.allHorses(searchMapper.dtoToEntity(horseSearchParamsDto)).stream()
                    .map(mapper::entityToBasicDto);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/parentsearch")
    @ResponseStatus(HttpStatus.OK)
    public Stream<SearchHorseDto> getParentOptions(ParentSearchParamsDto parentSearchParamsDto) {
        try {
            return service.parentOptions(parentSearchMapper.dtoToEntity(parentSearchParamsDto))
                    .stream().map(mapper::entityToSearchHorseDto);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/ancestor-tree")
    @ResponseStatus(HttpStatus.OK)
    public Stream<AncestorTreeHorseDto> getAncestorTree(@RequestParam(value = "maxGeneration", required = false) Integer maxGenerations) {
        try {
            return service.getAncestorTree(maxGenerations).stream().map(mapper::entityToDto);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
    public FullHorseOutputDto getHorseById(@PathVariable("horseId") long horseId) {
        try {
            return mapper.entityToFullDto(service.getHorseById(horseId));
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BasicHorseOutputDto createHorse(@RequestBody BasicHorseInputDto basicHorseInputDto) {
        try {
            return mapper.entityToBasicDto(
                    service.createHorse(
                            mapper.dtoToEntity(
                                    basicHorseInputDto
                            )
                    )
            );
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @DeleteMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteHorseById(
            @PathVariable("horseId") long horseId
    ) {
        try {
            service.deleteHorseById(horseId);
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
