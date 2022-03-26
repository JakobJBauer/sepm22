package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.*;

import java.util.List;

/**
 * Service for working with horses.
 */
public interface HorseService {
    /**
     * Lists all horses stored in the system.
     * @return list of all stored horses
     */
    List<Horse> allHorses(HorseSearchParams horseSearchParams);

    List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams);

    List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations);

    Horse getHorseById(Long id);

    Horse createHorse(Horse horse);

    Horse updateHorse(Horse horse);

    void deleteHorseById(Long id);
}
