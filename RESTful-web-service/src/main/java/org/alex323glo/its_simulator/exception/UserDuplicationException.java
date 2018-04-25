package org.alex323glo.its_simulator.exception;

/**
 * Duplicate User Exception class kind of Application Exceptions).
 *
 * @author Alexey_O
 * @version 0.1
 */
public class UserDuplicationException extends AppException {

    public UserDuplicationException(String message) {
        super(message);
    }

    public UserDuplicationException(Throwable cause) {
        super(cause);
    }

    public UserDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
