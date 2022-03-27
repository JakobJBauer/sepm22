package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;

import java.util.List;

/**
 * Service for working with owners.
 */
public interface OwnerService {
    /**
     * @param ownerSearchParams contains the filter values. null matches all
     * @return a list of all stored owners that match the filter
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     */
    List<Owner> getAllOwners(OwnerSearchParams ownerSearchParams);

    /**
     * @param id the id of the horse to get
     * @return a horse with the matching id
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.NoResultException
     */
    Owner getOwnerById(Long id);

    /**
     * @param owner the entity to insert into the database
     * @return the inserted entity including the generated id
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     */
    Owner createOwner(Owner owner);
}
