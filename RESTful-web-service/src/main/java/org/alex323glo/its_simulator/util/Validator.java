package org.alex323glo.its_simulator.util;

import org.alex323glo.its_simulator.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Validator util class.
 *
 * @author Alexey_O
 * @version 0.1
 */
@Component
public class Validator {

    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 8;

    private final int minPasswordLength;

    @Autowired
    public Validator(Environment environment) {
        minPasswordLength = environment.containsProperty("validation.password.min-length") ?
                Integer.parseInt(environment.getProperty("validation.password.min-length")) :
                DEFAULT_MIN_PASSWORD_LENGTH;
    }

    public Validator ifNull(Object object) throws ValidationException {
        if (object == null) {
            throw new ValidationException("'is null?' validation failed (validated " +
                    object.getClass().getSimpleName() + " reference is null).");
        }
        return this;
    }

    public Validator validateUsername(String username) throws ValidationException {
        if (username == null) {
            throw new ValidationException("Username validation failed (username is null).");
        }
        if (username.length() < 1) {
            throw new ValidationException("Username validation failed (username is empty).");
        }
        return this;
    }

    public Validator validatePassword(String password) throws ValidationException {
        if (password == null) {
            throw new ValidationException("Password validation failed (password is null).");
        }
        if (password.length() < minPasswordLength) {
            throw new ValidationException("Password validation failed (password is shorter then " +
                    minPasswordLength + " symbols).");
        }
        return this;
    }

    public Validator validateEmail(String email) throws ValidationException {
        if (email == null) {
            throw new ValidationException("Email validation failed (email is null).");
        }
        if (email.length() < 1) {
            throw new ValidationException("Email validation failed (email is empty).");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Email validation failed (email doesn't contain required '@' symbol).");
        }
        return this;
    }

    public Validator validatePayload(Double payload) throws ValidationException {
        if (payload == null) {
            throw new ValidationException("Payload validation failed (payload is null).");
        }
        if (payload <= 0) {
            throw new ValidationException("Payload validation failed (payload is <= 0).");
        }
        return this;
    }

    public Validator validatePlanetName(String planetName) throws ValidationException {
        if (planetName == null) {
            throw new ValidationException("Planet name validation failed (planet is null).");
        }
        if (planetName.length() < 1) {
            throw new ValidationException("Planet name validation failed (planet is empty).");
        }
        return this;
    }

    public Validator validatePlanetCoordinate(Long coordinate) throws ValidationException {
        if (coordinate == null) {
            throw new ValidationException("Planet coordinate validation failed (coordinate is null)");
        }
        if (coordinate < 0) {
            throw new ValidationException("Planet coordinate validation failed (coordinate is negative)");
        }
        return this;
    }

    public Validator validateSpaceShipName(String spaceShipName) throws ValidationException {
        if (spaceShipName == null) {
            throw new ValidationException("SpaceShip name validation failed (planet is null).");
        }
        if (spaceShipName.length() < 1) {
            throw new ValidationException("SpaceShip name validation failed (planet is empty).");
        }
        return this;
    }

    public Validator validateSpaceShipLevel(Integer level) throws ValidationException {
        if (level == null) {
            throw new ValidationException("SpaceShip level validation failed (level is null).");
        }
        if (level < 1) {
            throw new ValidationException("SpaceShip level validation failed (level is < 1).");
        }
        return this;
    }

    public Validator validateSpaceShipSpeed(Double speed) throws ValidationException {
        if (speed == null) {
            throw new ValidationException("SpaceShip speed validation failed (speed is null).");
        }
        if (speed <= 0) {
            throw new ValidationException("SpaceShip speed validation failed (speed is <= 0).");
        }
        return this;
    }
}
