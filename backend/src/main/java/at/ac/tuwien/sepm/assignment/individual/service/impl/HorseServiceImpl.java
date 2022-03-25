package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.HorseSearchParams;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {
    private final HorseDao dao;

    public HorseServiceImpl(HorseDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Horse> allHorses(HorseSearchParams horseSearchParams) {
        return dao.getAll(horseSearchParams);
    }

    @Override
    public Horse getHorseById(Long id) {
        if (id == null) return null;
        return dao.getHorseById(id);
    }

    @Override
    public Horse createHorse(Horse horse) {
        return dao.createHorse(horse);
    }

    @Override
    public Horse updateHorse(Horse horse) {
        return dao.updateHorse(horse);
    }

    @Override
    public void deleteHorseById(Long id) {
        dao.deleteHorseById(id);
    }
}
