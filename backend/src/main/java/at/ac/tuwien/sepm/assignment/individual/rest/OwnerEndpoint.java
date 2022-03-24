package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.FullOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.SearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.SearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.web.bind.annotation.*;

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
    public Stream<FullOwnerDto> getAllOwners(SearchParamsDto searchParamsDto) {
        return service.getAllOwners(
            this.searchParamsMapper.dtoToEntity(searchParamsDto)
        ).stream().map(mapper::entityToFullDto);
    }

    @PostMapping
    public FullOwnerDto createOwner(@RequestBody FullOwnerDto fullOwnerDto) {
        return mapper.entityToFullDto(
                service.createOwner(
                        mapper.dtoToEntity(
                                fullOwnerDto
                        )
                )
        );
    }
}
