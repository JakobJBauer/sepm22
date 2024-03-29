package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.entity.OwnerSearchParams;
import at.ac.tuwien.sepm.assignment.individual.exception.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.exception.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.OwnerDao;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import at.ac.tuwien.sepm.assignment.individual.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerDao dao;
    private final Validator validator;

    public OwnerServiceImpl(OwnerDao dao, Validator validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public List<Owner> getAllOwners(OwnerSearchParams ownerSearchParams) {
        LOGGER.trace("getAllOwners({})", ownerSearchParams);
        validator.ownerSearchParamsValidation(ownerSearchParams);
        try {
            return dao.getAllOwners(ownerSearchParams);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Owner getOwnerById(Long id) {
        LOGGER.trace("getOwnerById({})", id);
        validator.idValidation(id);

        try {
            return dao.getOwnerById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Owner createOwner(Owner owner) {
        LOGGER.trace("createOwner({})", owner);
        validator.ownerValidation(owner);
        try {
            return dao.createOwner(owner);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
