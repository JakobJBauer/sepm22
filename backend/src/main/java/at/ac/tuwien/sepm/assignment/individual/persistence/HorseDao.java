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
     * @return a list of all stored horses
     */
    List<Horse> getAll(HorseSearchParams horseSearchParams);

    List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams);

    Horse getHorseById(Long id);

    Horse createHorse(Horse horse);

    Horse updateHorse(Horse horse);

    void deleteHorseById(Long id);
}
