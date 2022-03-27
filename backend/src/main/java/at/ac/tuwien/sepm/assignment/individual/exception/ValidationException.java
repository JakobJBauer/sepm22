package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * ValidationException signals that an entity did not comply with preconditions
 * and therefore can not be computed further
 */
public class ValidationException extends RuntimeException{
    public ValidationException() { super(); }
    public ValidationException(String message) { super(message); }
    public ValidationException(Throwable cause) { super(cause); }
    public ValidationException(String message, Throwable cause) { super(message, cause); }
}
