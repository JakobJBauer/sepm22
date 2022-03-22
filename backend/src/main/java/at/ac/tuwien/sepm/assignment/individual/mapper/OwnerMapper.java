package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.BasicOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.FullOwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public Owner dtoToEntity(FullOwnerDto fullOwnerDto) {
        return new Owner(
                fullOwnerDto.firstName(),
                fullOwnerDto.lastName(),
                fullOwnerDto.email()
        );
    }

    public FullOwnerDto entityToFullDto(Owner owner) {
        return new FullOwnerDto(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );
    }

    public BasicOwnerDto entityToBasicDto(Owner owner) {
        return owner != null ? new BasicOwnerDto(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName()
        ) : null;
    }
}
