package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.SearchParams;
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
    public List<Horse> allHorses(SearchParams searchParams) {
        return dao.getAll(searchParams);
    }

    @Override
    public Horse getHorseById(long id) {
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
    public void deleteHorseById(long id) {
        dao.deleteHorseById(id);
    }
}
