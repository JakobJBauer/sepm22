package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * ConflictException signals that the transferred entity is correct, but
 * can not be processed due to the state of the backend
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
