package at.ac.tuwien.sepm.assignment.individual.validator;

import at.ac.tuwien.sepm.assignment.individual.entity.*;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public void horseSearchParamsValidation(HorseSearchParams horseSearchParams) {
        LOGGER.trace("horseSearchParamsValidation({})", horseSearchParams);
        if (horseSearchParams.getName() != null && horseSearchParams.getName().length() > 255)
            failValidation("Name of horse is to long. Limit is 255 chars");

        if (horseSearchParams.getDescription() != null && horseSearchParams.getDescription().length() > 255)
            failValidation("Description of horse is to long. Limit is 255 chars");

        if (horseSearchParams.getBirthdate() != null && horseSearchParams.getBirthdate().isBefore(LocalDate.of(1900, 1, 1)))
            failValidation("Birthdate is to far back in time. Earliest date is 1900");

        if (horseSearchParams.getSex() != null && (horseSearchParams.getSex() != Sex.MALE && horseSearchParams.getSex() != Sex.FEMALE))
            failValidation("Sex must be 'MALE' or 'FEMALE'");

        if (horseSearchParams.getOwnerName() != null && horseSearchParams.getOwnerName().length() > 255)
            failValidation("OwnerName is to long. Limit is 255 chars");
    }

    public void ownerSearchParamsValidation(OwnerSearchParams ownerSearchParams) {
        LOGGER.trace("ownerSearchParamsValidation({})", ownerSearchParams);
        if (ownerSearchParams.getSearchTerm() != null) {
            if (ownerSearchParams.getSearchTerm().length() > 255)
                failValidation("searchTerm is to long. Limit is 255 chars");
        }

        if (ownerSearchParams.getResultSize() != null) {
            if (ownerSearchParams.getResultSize() < 0)
                failValidation("resultSize must be an positive Integer");
        }
    }

    public void parentSearchParamsValidation(ParentSearchParams parentSearchParams) {
        LOGGER.trace("parentSearchParamsValidation({})", parentSearchParams);
        ownerSearchParamsValidation(
                new OwnerSearchParams(
                        parentSearchParams.getSearchTerm(),
                        parentSearchParams.getResultSize()
                )
        );

        if (parentSearchParams.getSex() != null) {
            if (parentSearchParams.getSex() != Sex.MALE && parentSearchParams.getSex() != Sex.FEMALE)
                failValidation("sex must be 'MALE' or 'FEMALE'");
        }
    }

    public void ancestorTreeMaxGenerationValidation(Integer maxGeneration) {
        LOGGER.trace("ancestorTreeMaxGenerationValidation({})", maxGeneration);
        if (maxGeneration != null) {
            if (maxGeneration < 0)
                failValidation("maxGeneration must be positive");
            if (maxGeneration > 50)
                failValidation("Recursion depth too high. Maximum is 50");
        }
    }

    public void idValidation(Long id) {
        LOGGER.trace("idValidation({})", id);
        if (id == null)
            failValidation("id cannot be null");
    }

    public void horseValidation(Horse horse) {
        LOGGER.trace("horseValidation({})", horse);
        if (horse.getName() == null || horse.getName().isBlank())
            failValidation("Name of horse is not defined");

        if (horse.getName().length() > 255)
            failValidation("Name of horse is to long. Limit is 255 chars");

        if (horse.getDescription() != null) {
            if (horse.getDescription().length() > 255)
                failValidation("Description of horse is to long. Limit is 255 chars");
        }

        if (horse.getBirthdate() == null)
            failValidation("Birthdate is not defined");

        if (horse.getBirthdate().isAfter(LocalDate.now()))
            failValidation("Birthdate is in the future");

        if (horse.getBirthdate().isBefore(LocalDate.of(1900, 1, 1)))
            failValidation("Birthdate is to far back in time. Earliest date is 1900");

        if (horse.getSex() == null)
            failValidation("Sex is not defined");

        if (horse.getSex() != Sex.MALE && horse.getSex() != Sex.FEMALE)
            failValidation("Sex must be 'MALE' or 'FEMALE'");

        if (horse.getOwner() != null) {
            ownerValidation(horse.getOwner());
        }

        if (horse.getParentIds() != null) {
            for (Long id : horse.getParentIds()) {
                idValidation(id);
            }
        }
    }

    public void ownerValidation(Owner owner) {
        LOGGER.trace("ownerValidation({})", owner);
        if (owner.getFirstName() == null || owner.getFirstName().isBlank())
            failValidation("firstName must be set");

        if (owner.getLastName() == null || owner.getLastName().isBlank())
            failValidation("lastName must be set");

        if (owner.getFirstName().length() > 255)
            failValidation("firstName is to long. Limit is 255 chars");

        if (owner.getLastName().length() > 255)
            failValidation("lastName is to long. Limit is 255 chars");

        if (owner.getEmail() != null) {
            if (owner.getEmail().length() > 255)
                failValidation("email is to long. Limit is 255 chars");

            if (!Pattern.compile("^(.+)@(\\S+)$").matcher(owner.getEmail()).matches())
                failValidation("Invalid email format");
        }
    }

    private void failValidation(String errorMsg) {
        LOGGER.trace("Throwing new Validationexception({})", errorMsg);
        throw new ValidationException(errorMsg);
    }
}
