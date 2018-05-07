package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;

/**
 * General User Service, which covers business logic of registration,
 * search and management of User data in System.
 *
 * @author Alexey_O
 * @version 0.1
 */
public interface UserService {

    /**
     * Registers new User to System.
     *
     * @param username unique and valid username of new User.
     * @param password valid password of new User.
     * @param email unique and valid email address of new User.
     * @return (not null) registered User object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    User registerUser(String username, String password, String email) throws AppException;

    /**
     * Searches for User's Game Profile (personal game data).
     *
     * @param username unique and valid username of registered User.
     * @return (not null) needed User's UserGameProfile, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    UserGameProfile findUserGameProfile(String username) throws AppException;

    /**
     * Searches for User's Extension (additional personal data).
     *
     * @param username unique and valid username of registered User.
     * @return (not null) needed User's UserExtension, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    UserExtension findUserExtension(String username) throws AppException;

    /**
     * Edits User's Extension (additional personal data).
     *
     * @param username unique and valid username of registered User.
     * @param newUserExtension new UserExtension object. Fields, not needed to be changed,
     *                         must be null. Also, key fields (such as id, or email) will not be
     *                         updated in any case!
     * @return actual
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    UserExtension changeUserExtension(String username, UserExtension newUserExtension) throws AppException;

}
