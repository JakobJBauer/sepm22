package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.*;

import java.util.List;

/**
 * Service for working with horses.
 */
public interface HorseService {
    /**
     * Get all horses stored in the persistent data store.
     * @param horseSearchParams contains the filter values. null matches all
     * @return a list of all stored horses that match the filter
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     */
    List<Horse> allHorses(HorseSearchParams horseSearchParams);

    /**
     * @param parentSearchParams contains the optional filter keyword and max result size
     * @return a list of options for parents
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     */
    List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams);

    /**
     * @param maxGenerations defines the max depth of the resulting horse tree
     * @return a list of horses in a tree structure, defined by their parental relations
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     */
    List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations);

    /**
     * @param id the id of the horse to get
     * @return a horse with the matching id
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.NoResultException
     */
    Horse getHorseById(Long id);

    /**
     * @param horse the entity to insert into the database
     * @return the inserted entity including the generated id
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ConflictException
     */
    Horse createHorse(Horse horse);

    /**
     * @param horse the entity to update in the database
     * @return the updated entity
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ConflictException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.NoResultException
     */
    Horse updateHorse(Horse horse);

    /**
     * @param id the id of the horse to delete
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ServiceException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.ValidationException
     * @throws at.ac.tuwien.sepm.assignment.individual.exception.NoResultException
     */
    void deleteHorseById(Long id);
}
