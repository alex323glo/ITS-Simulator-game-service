package org.alex323glo.its_simulator.exception;

public class SpaceShipDuplicationException extends AppException {
    public SpaceShipDuplicationException(String message) {
        super(message);
    }

    public SpaceShipDuplicationException(Throwable cause) {
        super(cause);
    }

    public SpaceShipDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
