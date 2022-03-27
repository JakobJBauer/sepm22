package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.BasicOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.FullOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerMapper.class);

    public Owner dtoToEntity(FullOwnerDto fullOwnerDto) {
        LOGGER.trace("Mapping FullOwnerDto to Owner");
        return new Owner(
                fullOwnerDto.firstName(),
                fullOwnerDto.lastName(),
                fullOwnerDto.email()
        );
    }

    public FullOwnerDto entityToFullDto(Owner owner) {
        LOGGER.trace("Mapping Owner to FullOwnerDto");
        return new FullOwnerDto(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );
    }

    public BasicOwnerDto entityToBasicDto(Owner owner) {
        LOGGER.trace("Mapping Owner to BasicOwnerDto");
        return owner != null ? new BasicOwnerDto(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName()
        ) : null;
    }
}
