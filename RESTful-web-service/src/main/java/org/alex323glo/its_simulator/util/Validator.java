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
public class Validator {

    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 8;

    private final Environment environment;
    private final int minPasswordLength;

    public Validator(Environment environment) {
        this.environment = environment;
        minPasswordLength = environment.containsProperty("validation.password.min-length") ?
                Integer.parseInt(environment.getProperty("validation.password.min-length")) :
                DEFAULT_MIN_PASSWORD_LENGTH;
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
}
