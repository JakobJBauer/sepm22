package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exception.*;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HorseServiceImpl implements HorseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorseServiceImpl.class);

    private final HorseDao dao;
    private final Validator validator;

    public HorseServiceImpl(HorseDao dao, Validator validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public List<Horse> allHorses(HorseSearchParams horseSearchParams) {
        LOGGER.trace("allHorses({})", horseSearchParams);
        validator.horseSearchParamsValidation(horseSearchParams);
        try {
            return dao.getAll(horseSearchParams);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<SearchHorse> parentOptions(ParentSearchParams parentSearchParams) {
        LOGGER.trace("parentOptions({})", parentSearchParams);
        validator.parentSearchParamsValidation(parentSearchParams);
        try {
            return dao.parentOptions(parentSearchParams);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<AncestorTreeHorse> getAncestorTree(Integer maxGenerations) {
        LOGGER.trace("getAncestorTree({})", maxGenerations);
        validator.ancestorTreeMaxGenerationValidation(maxGenerations);
        if (maxGenerations == null) maxGenerations = 5;
        try {
            return dao.getAncestorTree(maxGenerations);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse getHorseById(Long id) {
        LOGGER.trace("getHorseById({})", id);
        validator.idValidation(id);
        try {
            return dao.getHorseById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse createHorse(Horse horse) {
        LOGGER.trace("createHorse({})", horse);
        validator.horseValidation(horse);
        validateParent(horse);

        try {
            return dao.createHorse(horse);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Horse updateHorse(Horse horse) {
        LOGGER.trace("updateHorse({})", horse);
        validator.horseValidation(horse);
        validateParent(horse);

        try {
            if (dao.hasCriticalSex(horse) && dao.getHorseById(horse.getId()).getSex() != horse.getSex())
                throw new ConflictException("horse is in an parent relationship not allowing sex changes");

            return dao.updateHorse(horse);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteHorseById(Long id) {
        LOGGER.trace("deleteHorseById({})", id);
        validator.idValidation(id);
        try {
            dao.deleteHorseById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void validateParent(Horse horse) {
        LOGGER.trace("validateParent({})", horse);
        var parents = getParents(horse);
        validateParentAge(horse, parents);
        validateParentSex(parents);
    }

    private void validateParentAge(Horse horse, Horse[] parents) {
        LOGGER.trace("validateParentAge({}, {})", horse, parents);
        for (Horse parent : parents) {
            if (!parent.getBirthdate().isBefore(horse.getBirthdate()))
                throw new ValidationException("at least one parent is younger than child");
        }
    }

    private void validateParentSex(Horse[] parents) {
        LOGGER.trace("validateParentSex({})", parents);
        if (parents.length > 2)
            throw new ValidationException("only a maximum of two parents are allowed");
        if (parents.length == 2 && parents[0].getSex() == parents[1].getSex())
            throw new ConflictException("parents can not have same sex");
    }

    private Horse[] getParents(Horse horse) {
        LOGGER.trace("getParents({})", horse);
        if (horse.getParentIds() == null) {
            horse.setParentIds(new Long[0]);
            return new Horse[0];
        }

        return Arrays.stream(horse.getParentIds()).map(this::getHorseById).toArray(Horse[]::new);
    }
}
