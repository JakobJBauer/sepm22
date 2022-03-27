package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.*;

import java.util.List;

/**
 * Data Access Object for horses.
 * Implements access functionality to the application's persistent data store regarding horses.
 */
public interface HorseDao {
    /**
     * Get all horses stored in the persistent data store.
     * @param horseSearchParams contains the filter values. null matches all
     * @return a list of all stored horses that match the filter
     */
    List<Horse> getAll(HorseSearchParams horseSearchParams);

    /**
     * @param parentSearchParams contains the optional filter keyword and max result size
     * @return a list of options for parents
     */
    List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams);

    /**
     * @param maxGenerations defines the max depth of the resulting horse tree
     * @return a list of horses in a tree structure, defined by their parental relations
     */
    List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations);

    /**
     * @param id the id of the horse to get
     * @return a horse with the matching id
     */
    Horse getHorseById(Long id);

    /**
     * @param horse the entity to insert into the database
     * @return the inserted entity including the generated id
     */
    Horse createHorse(Horse horse);

    /**
     * @param horse the entity to update in the database
     * @return the updated entity
     */
    Horse updateHorse(Horse horse);

    /**
     * Checks if the sex of horse cannot be changed without allowing same-sex parents
     * @param horse the entity to check for
     * @return false if the sex of the entity can be changed, true otherwise
     */
    boolean hasCriticalSex(Horse horse);

    /**
     * @param id the id of the horse to delete
     */
    void deleteHorseById(Long id);
}
