package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.SpaceShip;

import java.util.List;

/**
 * General Space Ship Service, which covers business logic
 * of search and management of User's SpaceShips.
 *
 * @author Alexey_O
 * @version 0.1
 */
public interface SpaceShipService {

    /**
     * Saves new User's SpaceShip to System.
     *
     * @param username unique and valid username of registered User.
     * @param shipName unique and valid name of new SpaceShip.
     * @param maxCargoCapacity maximum payload of new SpaceShip.
     * @param level initial level of new SpaceShip.
     * @param speed initial speed of new SpaceShip.
     * @return (not null) saved SpaceShip, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    SpaceShip createSpaceShip(String username, String shipName, Double maxCargoCapacity,
                              Integer level, Double speed) throws AppException;

    /**
     * Searches for existent User's SpaceShip.
     *
     * @param username unique and valid username of registered User.
     * @param shipName unique and valid name of existent SpaceShip.
     * @return needed SpaceShip, if it was created before, or null, if it wasn't.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    SpaceShip findSpaceShip(String username, String shipName) throws AppException;

    /**
     * Lists all User's existent SpaceShips.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of User's SpaceShip's, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    List<SpaceShip> findAllShips(String username) throws AppException;

    /**
     * Lists all User's existent SpaceShips with FREE status.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of User's FREE SpaceShip's, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    List<SpaceShip> findAllFreeShips(String username) throws AppException;
}
