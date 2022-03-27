package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * NoResultException signals that an operation did not return any result
 * despite being expected to do so
 */
public class NoResultException extends RuntimeException {
    public NoResultException(String message) { super(message); }
}
