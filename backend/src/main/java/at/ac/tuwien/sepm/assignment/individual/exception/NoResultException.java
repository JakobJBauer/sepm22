package at.ac.tuwien.sepm.assignment.individual.exception;

public class NoResultException extends PersistenceException {
    public NoResultException() { super(); }
    public NoResultException(String message) { super(message); }
    public NoResultException(Throwable cause) { super(cause); }
    public NoResultException(String message, Throwable cause) { super(message, cause); }
}
