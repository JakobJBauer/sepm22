package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.FullOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerSearchParamsDto;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerSearchParamsMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/owners")
public class OwnerEndpoint {
    private final OwnerService service;
    private final OwnerMapper mapper;
    private final OwnerSearchParamsMapper searchParamsMapper;

    public OwnerEndpoint(OwnerService service, OwnerMapper mapper, OwnerSearchParamsMapper searchParamsMapper) {
        this.service = service;
        this.mapper = mapper;
        this.searchParamsMapper = searchParamsMapper;
    }

    @GetMapping
    public Stream<FullOwnerDto> getAllOwners(OwnerSearchParamsDto searchParamsDto) {
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
