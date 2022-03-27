package at.ac.tuwien.sepm.assignment.individual.exception;

/**
 * ServiceException signals that a fundamental error happened in the service
 * layer, which can not be resolved on its own
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String message, Throwable cause) { super(message, cause); }
}
