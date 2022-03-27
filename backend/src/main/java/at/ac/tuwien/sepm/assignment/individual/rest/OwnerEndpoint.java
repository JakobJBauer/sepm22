package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.FullOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.SearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.SearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/owners")
public class OwnerEndpoint {
    private final OwnerService service;
    private final OwnerMapper mapper;
    private final SearchParamsMapper searchParamsMapper;

    public OwnerEndpoint(OwnerService service, OwnerMapper mapper, SearchParamsMapper searchParamsMapper) {
        this.service = service;
        this.mapper = mapper;
        this.searchParamsMapper = searchParamsMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Stream<FullOwnerDto> getAllOwners(SearchParamsDto searchParamsDto) {
        try {
            return service.getAllOwners(
                    this.searchParamsMapper.dtoToEntity(searchParamsDto)
            ).stream().map(mapper::entityToFullDto);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public FullOwnerDto createOwner(@RequestBody FullOwnerDto fullOwnerDto) {
        try {
            return mapper.entityToFullDto(
                    service.createOwner(
                            mapper.dtoToEntity(
                                    fullOwnerDto
                            )
                    )
            );
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
