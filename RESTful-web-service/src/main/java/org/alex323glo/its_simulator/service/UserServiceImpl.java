package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.UserDuplicationException;
import org.alex323glo.its_simulator.exception.ValidationException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.repository.UserExtensionRepository;
import org.alex323glo.its_simulator.repository.UserGameProfileRepository;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.alex323glo.its_simulator.util.UserExtensionEditor;
import org.alex323glo.its_simulator.util.Validator;
import org.hibernate.Hibernate;
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
    private final UserExtensionRepository userExtensionRepository;
    private final UserGameProfileRepository userGameProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(Environment environment,
                           UserRepository userRepository,
                           UserExtensionRepository userExtensionRepository,
                           UserGameProfileRepository userGameProfileRepository,
                           PasswordEncoder passwordEncoder) {
        this.validator = new Validator(environment);
        this.userRepository = userRepository;
        this.userExtensionRepository = userExtensionRepository;
        this.userGameProfileRepository = userGameProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers new User to System.
     *
     * @param username unique not-null username.
     * @param password not-null password.
     * @param email    unique not-null email.
     * @return registered User.
     * @throws AppException if System can't register User in some reasons. Possible reasons: <br>
     *                      - User with such username and/or email was registered
     *                      to system before (UserDuplicationException); <br>
     *                      - username, password, or email didn't pass validation
     *                      via Validator class logic (ValidationException) <br>
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
    @Transactional
    @Override
    public User findLightUser(String username) throws AppException {
        LOGGER.trace("Trying to find User (in light version) by username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find User (in light version) by username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        LOGGER.trace("Successfully found User (in light version) by username.");
        return userRepository.findUserByUsername(username);
    }

    /**
     * Searches for User record by its username.
     * <p>
     * WARNING! This method (as result) gives you User object with all
     * fully initialized fields (and without loop references). If you want to get light-weight
     * User object (with not initialized userExtension and userGameProfile fields),
     * please, use findLightUser() method (referenced below).
     *
     * @param username unique not-null username.
     * @return needed User, if it was registered to System before, or null, if it wasn't.
     * @throws AppException if System can't search for User in some reasons.
     * @see UserService#findUser(String)
     */
    @Transactional
    @Override
    public User findUser(String username) throws AppException {
        LOGGER.trace("Trying to find User by username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find User by username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        User user = userRepository.findUserByUsername(username);

        user.setUserExtension((UserExtension) Hibernate.unproxy(user.getUserExtension()));
        user.getUserExtension().setUser(null);
        user.setUserGameProfile((UserGameProfile) Hibernate.unproxy(user.getUserGameProfile()));
        user.getUserGameProfile().setUser(null);

        LOGGER.trace("Successfully found User by username.");
        return user;
    }

    /**
     * Removes User from System.
     * WARNING! This operation will delete all records, dependent on this User (cascade operation)!!!
     *
     * @param user User, which will be removed.
     * @return removed User.
     * @throws AppException if System can't delete User in some reasons.
     */
    @Transactional
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
     * @param username         unique not-null username.
     * @param newUserExtension new instance of User extension.
     * @return replaced (old) UserExtension.
     * @throws AppException if System can't change User's Extension in some reasons.
     */
    @Transactional
    @Override
    public UserExtension changeUserExtension(String username, UserExtension newUserExtension) throws AppException {
        LOGGER.trace("Trying to change UserExtension of registered User...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't change UserExtension of registered User. " + e.getMessage(), e);
            throw new AppException(e);
        }

        UserExtension userExtension = userExtensionRepository.findUserExtensionByUser_Username(username);

        if (newUserExtension.getEmail() != null &&
                userExtensionRepository.findUserExtensionByEmail(newUserExtension.getEmail()) != null) {

            AppException exception =
                    new AppException("Was caught an attempt to save UserExtension with duplicate (not unique) email.");
            LOGGER.error("Can't change UserExtension of registered User. " + exception.getMessage(), exception);
            throw exception;
        }

        UserExtension mergedUserExtension =
                new UserExtensionEditor().email(newUserExtension.getEmail()).merge(userExtension);
        mergedUserExtension.setUser(null);  // TODO solve JSON recursion (loop relations User->UserExtension->User)
        LOGGER.trace("Successfully changed UserExtension of registered User.");
        return (UserExtension) Hibernate.unproxy(mergedUserExtension);
    }

    /**
     * Searches for UserExtension record by User's username.
     *
     * @param username unique not-null username.
     * @return needed UserExtension, if User with such username was registered to System,
     * or null, if it wasn't.
     * @throws AppException if System can't find UserExtension in some reasons. Possible reasons: <br>
     *                      - username didn't pass validation via Validator class logic (ValidationException) <br>
     */
    @Transactional
    @Override
    public UserExtension findUserExtension(String username) throws AppException {
        LOGGER.trace("Trying to find UserExtension by username.");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find UserExtension by username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        UserExtension userExtension = userExtensionRepository.findUserExtensionByUser_Username(username);

        LOGGER.trace("Successfully found UserExtension by username.");
        return userExtension;
    }

    /**
     * Searches for UserGameProfile record by User's username.
     *
     * @param username unique not-null username.
     * @return needed UserGameProfile, if User with such username was registered to System,
     * or null, if it wasn't.
     * @throws AppException if System can't find UserGameProfile in some reasons. Possible reasons: <br>
     *                      - username didn't pass validation via Validator class logic (ValidationException) <br>
     */
    @Transactional
    @Override
    public UserGameProfile findUserGameProfile(String username) throws AppException {
        LOGGER.trace("Trying to find UserGameProfile by username.");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find UserGameProfile by username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        UserGameProfile userGameProfile = userGameProfileRepository.findUserGameProfileByUser_Username(username);

        LOGGER.trace("Successfully found UserGameProfile by username.");
        return userGameProfile;
    }

    /**
     * Lists all Users.
     *
     * @return List of all Users.
     * @throws AppException if System can't list all Users in some reasons.
     */
    @Transactional
    @Override
    public List<User> findAllUsers() throws AppException {
        // TODO 'findAllUsers()' - complete implementation
        throw new UnsupportedOperationException();
    }
}
