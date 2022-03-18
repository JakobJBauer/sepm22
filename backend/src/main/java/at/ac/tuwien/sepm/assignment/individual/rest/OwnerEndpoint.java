package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.mapper.OwnerMapper;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/owners")
public class OwnerEndpoint {
    private final OwnerService service;
    private final OwnerMapper mapper;
    public OwnerEndpoint(OwnerService service, OwnerMapper mapper, OwnerSearchParamsMapper ownerSearchParamsMapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public OwnerDto createOwner(@RequestBody OwnerDto ownerDto) {
        return mapper.entityToDto(
                service.createOwner(
                        mapper.dtoToEntity(
                                ownerDto
                        )
                )
        );
    }
}
