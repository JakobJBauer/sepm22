package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;

import java.util.List;

/**
 * Data Access Object for owners.
 * Implements access functionality to the application's persistent data store regarding owners.
 */
public interface OwnerDao {
    /**
     * @param ownerSearchParams contains the filter values. null matches all
     * @return a list of all stored owners that match the filter
     */
    List<Owner> getAllOwners(OwnerSearchParams ownerSearchParams);

    /**
     * @param owner the entity to insert into the database
     * @return the inserted entity including the generated id
     */
    Owner createOwner(Owner owner);

    /**
     * @param id the id of the horse to get
     * @return a horse with the matching id
     */
    Owner getOwnerById(Long id);
}
