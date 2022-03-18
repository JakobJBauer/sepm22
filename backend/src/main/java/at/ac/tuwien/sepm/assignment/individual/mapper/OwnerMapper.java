package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public Owner dtoToEntity(OwnerDto ownerDto) {
        return new Owner(
                ownerDto.firstName(),
                ownerDto.lastName(),
                ownerDto.email()
        );
    }

    public OwnerDto entityToDto(Owner owner) {
        return new OwnerDto(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );
    }
}
