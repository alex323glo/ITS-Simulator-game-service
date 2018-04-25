package org.alex323glo.its_simulator.exception;

import java.security.PrivilegedActionException;

/**
 * General Application Exception class.
 *
 * @author Alexey_O
 * @version 0.1
 */
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
