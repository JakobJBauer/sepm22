package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
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
    public List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams) {
        return dao.parentOptions(parentSearchParams);
    }

    @Override
    public List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations) {
        if (maxGenerations == null) maxGenerations = 5;
        return dao.getAncestorTree(maxGenerations);
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
