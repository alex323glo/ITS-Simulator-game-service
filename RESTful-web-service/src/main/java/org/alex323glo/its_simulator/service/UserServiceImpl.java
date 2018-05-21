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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserService and UserDetailsService interfaces. See more in abstraction.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see UserService
 * @see UserDetailsService
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Validator validator;
    private final UserRepository userRepository;
    private final UserExtensionRepository userExtensionRepository;
    private final UserGameProfileRepository userGameProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(Validator validator, UserRepository userRepository, UserExtensionRepository userExtensionRepository, UserGameProfileRepository userGameProfileRepository, PasswordEncoder passwordEncoder) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.userExtensionRepository = userExtensionRepository;
        this.userGameProfileRepository = userGameProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers new User to System.
     *
     * @param username unique and valid username of new User.
     * @param password valid password of new User.
     * @param email    unique and valid email address of new User.
     * @return (not null) registered User object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public User registerUser(String username, String password, String email) throws AppException {
        LOGGER.info("Trying to register new User to System...");

        try {
            validator.validateUsername(username).validatePassword(password).validateEmail(email);
        } catch (ValidationException e) {
            AppException exception = new AppException("Can't register new User to System. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (userRepository.findByUsername(username) != null) {
            AppException exception =
                    new UserDuplicationException("User with such username was already registered before");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (userRepository.findByUserExtension_Email(email) != null) {
            AppException exception =
                    new UserDuplicationException("User with such email was already registered before.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserGameProfile userGameProfile = UserGameProfile.builder()
                .ships(new ArrayList<>())
                .missions(new ArrayList<>())
                .experience(0L)
                .completedMissionsNumber(0)
                .shipsNumber(0)
                .build();
        UserExtension userExtension = UserExtension.builder()
                .email(email)
                .registrationTime(LocalDateTime.now())
                .build();
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .userGameProfile(userGameProfile)
                .userExtension(userExtension)
                .build();

        userGameProfile.setUser(user);
        userExtension.setUser(user);

        User savedUser = userRepository.save(user);

        LOGGER.info("Successfully registered new User to System.");
        return savedUser;
    }

    /**
     * Searches for User's Game Profile (personal game data).
     *
     * @param username unique and valid username of registered User.
     * @return (not null) needed User's UserGameProfile, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public UserGameProfile findUserGameProfile(String username) throws AppException {
        LOGGER.info("Trying to find UserGameProfile by User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception =
                    new AppException("Can't find UserGameProfile by User's username. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserGameProfile userGameProfile = userGameProfileRepository.findByUser_Username(username);

        LOGGER.info("Successfully found UserGameProfile by User's username.");
        return userGameProfile;
    }

    /**
     * Searches for User's Extension (additional personal data).
     *
     * @param username unique and valid username of registered User.
     * @return (not null) needed User's UserExtension, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public UserExtension findUserExtension(String username) throws AppException {
        LOGGER.info("Trying to find UserExtension by User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception =
                    new AppException("Can't find UserExtension by User's username. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserExtension userExtension = userExtensionRepository.findByUser_Username(username);

        LOGGER.info("Successfully found UserExtension by User's username.");
        return userExtension;
    }

    /**
     * Edits User's Extension (additional personal data).
     *
     * @param username         unique and valid username of registered User.
     * @param newUserExtension new UserExtension object. Fields, not needed to be changed,
     *                         must be null. Also, key fields (such as id, or email) will not be
     *                         updated in any case!
     * @return actual
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public UserExtension changeUserExtension(String username, UserExtension newUserExtension) throws AppException {
        LOGGER.info("Trying to edit UserExtension by User's username with new UserExtension instance...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception = new AppException(
                    "Can't edit UserExtension by User's username with new UserExtension instance. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (newUserExtension == null) {
            AppException exception = new AppException(
                    "Attempt to use null (instead of UserExtension instance) to update User data to System.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (newUserExtension.getEmail() != null &&
                userExtensionRepository.findByEmail(newUserExtension.getEmail()) != null) {
            UserDuplicationException exception =
                    new UserDuplicationException("Attempt to update User data with duplicate email.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserExtension storedUserExtension = userExtensionRepository.findByUser_Username(username);

        new UserExtensionEditor(validator)
                .email(newUserExtension.getEmail())
                .merge(storedUserExtension);


        LOGGER.info("Successfully edited UserExtension by User's username with new UserExtension instance.");
        return storedUserExtension;
    }

    /**
     * Lists all registered Users.
     *
     * @return (not null) List of registered Users, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public List<User> listAllUsers() throws AppException {
        LOGGER.info("Trying to list all Users...");

        List<User> userList = null;
        try {
            userList = userRepository.findAll();
        } catch (Exception e) {
            AppException exception = new AppException("Can't list all Users. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        LOGGER.info("Successfully listed all Users.");
        return userList;
    }

    /**
     * Deletes all personal data of single registered User.
     * <p>
     * WARNING! Missions data and SpaceShips data also belong to User data (and will be deleted too)!
     *
     * @param username unique and valid username of registered User.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public void deleteUserData(String username) throws AppException {
        LOGGER.info("Trying to delete personal data of single User by User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception = new AppException(
                    "Can't delete personal data of single User by User's username. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            AppException exception = new AppException("Attempt to delete personal data of non-existent User.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        userRepository.delete(user);

        LOGGER.info("Successfully deleted personal data of single User by User's username.");
    }

    /**
     * Deletes personal data of all registered Users.
     * <p>
     * WARNING! Missions data and SpaceShips data also belong to User data (and will be deleted too)!
     *
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public void deleteAllUserData() throws AppException {
        LOGGER.info("Trying to delete personal data of all Users...");

        try {
            userRepository.deleteAll();
        } catch (Exception e) {
            AppException exception =
                    new AppException("Can't delete personal data of all Users. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        LOGGER.info("Successfully deleted personal data of all Users.");
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     *
     * @see UserDetailsService
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Trying to load User (UserDetails) by username (for Security)...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            UsernameNotFoundException exception = new UsernameNotFoundException(
                    "Can't load User (UserDetails) by username (for Security). " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserDetails userDetails = userRepository.findByUsername(username).generateUserDetails();

        LOGGER.info("Successfully loaded User (UserDetails) by username (for Security).");
        return userDetails;
    }
}
