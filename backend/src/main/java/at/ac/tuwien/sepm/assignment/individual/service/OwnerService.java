package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;

import java.util.List;

public interface OwnerService {
    List<Owner> getAllOwners(OwnerSearchParams ownerSearchParams);


    Owner createOwner(Owner owner);
}
