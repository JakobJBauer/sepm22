package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;

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

    Horse getHorseById(long id);

    Horse createHorse(Horse horse);

    Horse updateHorse(Horse horse);

    void deleteHorseById(long id);
}
