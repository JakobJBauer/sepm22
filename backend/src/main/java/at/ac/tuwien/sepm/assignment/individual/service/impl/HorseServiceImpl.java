package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exception.NoResultException;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
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
        try {
            return dao.getAll(horseSearchParams);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams) {
        try {
            return dao.parentOptions(parentSearchParams);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations) {
        if (maxGenerations == null) maxGenerations = 5;
        try {
            return dao.getAncestorTree(maxGenerations);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse getHorseById(Long id) {
        if (id == null) return null;
        try {
            return dao.getHorseById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse createHorse(Horse horse) {
        try {
            return dao.createHorse(horse);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse updateHorse(Horse horse) {
        try {
            return dao.updateHorse(horse);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteHorseById(Long id) {
        try {
            dao.deleteHorseById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
