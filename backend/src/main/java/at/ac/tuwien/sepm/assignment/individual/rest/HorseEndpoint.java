package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.*;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseSearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.SearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/horses")
public class HorseEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorseEndpoint.class);

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
        LOGGER.info("GET Request on '/horses' with Params {}", horseSearchParamsDto);
        try {
            return service.allHorses(searchMapper.dtoToEntity(horseSearchParamsDto)).stream()
                    .map(mapper::entityToBasicDto);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/parentsearch")
    @ResponseStatus(HttpStatus.OK)
    public Stream<SearchHorseDto> getParentOptions(ParentSearchParamsDto parentSearchParamsDto) {
        LOGGER.info("GET Request on '/horses/parentsearch' with Params {}", parentSearchParamsDto);
        try {
            return service.parentOptions(parentSearchMapper.dtoToEntity(parentSearchParamsDto))
                    .stream().map(mapper::entityToSearchHorseDto);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/ancestor-tree")
    @ResponseStatus(HttpStatus.OK)
    public Stream<AncestorTreeHorseDto> getAncestorTree(@RequestParam(value = "maxGeneration", required = false) Integer maxGenerations) {
        LOGGER.info("GET Request on '/horses/ancestor-tree' with Params {}", maxGenerations);
        try {
            return service.getAncestorTree(maxGenerations).stream().map(mapper::entityToDto);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
    public FullHorseOutputDto getHorseById(@PathVariable("horseId") long horseId) {
        LOGGER.info("GET Request on '/horses/{}'", horseId);
        try {
            return mapper.entityToFullDto(service.getHorseById(horseId));
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BasicHorseOutputDto createHorse(@RequestBody BasicHorseInputDto basicHorseInputDto) {
        LOGGER.info("POST Request on '/horses' with Body {}", basicHorseInputDto);
        try {
            return mapper.entityToBasicDto(
                    service.createHorse(
                            mapper.dtoToEntity(
                                    basicHorseInputDto
                            )
                    )
            );
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
    public FullHorseOutputDto updateHorse(
            @RequestBody BasicHorseInputDto basicHorseInputDto,
            @PathVariable("horseId") long horseId
    ) {
        LOGGER.info("PUT Request on '/horses/{}' with Body {}", horseId, basicHorseInputDto);
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
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @DeleteMapping(path = "/{horseId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteHorseById(
            @PathVariable("horseId") long horseId
    ) {
        LOGGER.info("DELETE Request on '/horses/{}'", horseId);
        try {
            service.deleteHorseById(horseId);
        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
