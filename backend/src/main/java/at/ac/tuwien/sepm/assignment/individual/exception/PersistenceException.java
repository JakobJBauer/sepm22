package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * PersistenceException signals that a fundamental error happened in the persistence
 * layer, which can not be resolved on its own
 */
public class PersistenceException extends RuntimeException {
    public PersistenceException() { super(); }
    public PersistenceException(String message) { super(message); }
    public PersistenceException(Throwable cause) { super(cause); }
    public PersistenceException(String message, Throwable cause) { super(message, cause); }
}
