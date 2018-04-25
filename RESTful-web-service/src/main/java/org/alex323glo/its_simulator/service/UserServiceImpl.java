package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.UserDuplicationException;
import org.alex323glo.its_simulator.exception.ValidationException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.alex323glo.its_simulator.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * General User Service implementation.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Validator validator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(Environment environment, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.validator = new Validator(environment);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers new User to System.
     *
     * @param username unique not-null username.
     * @param password not-null password.
     * @param email    unique not-null email.
     * @return registered User.
     * @throws AppException if System can't register User in some reasons. Possible reasons:
     *                      - User with such username and/or email was registered
     *                      to system before (UserDuplicationException)
     *                      - username, password, or email didn't pass validation
     *                      via Validator class logic (ValidationException)
     * @see Validator
     */
    @Transactional
    @Override
    public User registerUser(String username, String password, String email) throws AppException {
        LOGGER.trace("Trying to register new User to System...");
        try {
            validator
                    .validateUsername(username)
                    .validatePassword(password)
                    .validateEmail(email);
        } catch (ValidationException e) {
            LOGGER.error("Can't register new User to System. " + e.getMessage(), e);
            throw new AppException(e);
        }

        if (userRepository.findUserByUsername(username) != null) {
            throw new UserDuplicationException("Was caught an attempt to save new User with duplicate username.");
        }
        if (userRepository.findUserByUserExtension_Email(username) != null) {
            throw new UserDuplicationException("Was caught an attempt to save new User with duplicate email.");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        user.setUserExtension(
                UserExtension.builder()
                        .user(user)
                        .email(email)
                        .registrationTime(LocalDateTime.now())
                        .build()
        );

        user.setUserGameProfile(
                UserGameProfile.builder()
                        .user(user)
                        .build()
        );

        LOGGER.trace("Successfully registered new User to System.");
        return userRepository.save(user);
    }

    /**
     * Searches for User record by its username.
     *
     * @param username unique not-null username.
     * @return needed User, if it was registered to System before, or null, if it wasn't.
     * @throws AppException if System can't search for User in some reasons.
     */
    @Override
    public User findUser(String username) throws AppException {
        LOGGER.trace("Trying to find User by username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find User by username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        LOGGER.trace("Successfully found User by username.");
        return userRepository.findUserByUsername(username);
    }

    /**
     * Removes User from System.
     * WARNING! This operation will delete all records, dependent on this User (cascade operation)!!!
     *
     * @param user User, which will be removed.
     * @return removed User.
     * @throws AppException if System can't delete User in some reasons.
     */
    @Override
    public User deleteUser(User user) throws AppException {
        LOGGER.trace("Trying to delete User from System...");

        if (user == null) {
            AppException exception = new AppException("Was caught an attempt to use null (instead of User object) " +
                    "to delete User from UserRepository.", new NullPointerException());
            LOGGER.error("Can't delete User from System. " + exception.getMessage(), exception);
            throw exception;
        }

        userRepository.delete(user);
        LOGGER.trace("Successfully deleted User from System.");
        return user;
    }

    /**
     * Changes UserExtension of needed User
     * (will simply replace old extension with new one).
     *
     * @param user             target User.
     * @param newUserExtension new instance of User extension.
     * @return replaced (old) UserExtension.
     * @throws AppException if System can't change User's Extension in some reasons.
     */
    @Override
    public UserExtension changeUserExtension(User user, UserExtension newUserExtension) throws AppException {
        // TODO 'changeUserExtension()' - complete implementation
        throw new UnsupportedOperationException();
    }

    /**
     * TODO finish doc (UserService.findUserExtension())
     *
     * @param username
     * @return
     * @throws AppException
     */
    @Override
    public UserExtension findUserExtension(String username) throws AppException {
        // TODO 'changeUserExtension()' - complete implementation
        throw new UnsupportedOperationException();
    }

    /**
     * TODO finish doc (UserService.findUserGameProfile())
     *
     * @param username
     * @return
     * @throws AppException
     */
    @Override
    public UserGameProfile findUserGameProfile(String username) throws AppException {
        // TODO 'changeUserExtension()' - complete implementation
        throw new UnsupportedOperationException();
    }

    /**
     * Lists all Users.
     *
     * @return List of all Users.
     * @throws AppException if System can't list all Users in some reasons.
     */
    @Override
    public List<User> findAllUsers() throws AppException {
        // TODO 'changeUserExtension()' - complete implementation
        throw new UnsupportedOperationException();
    }
}
