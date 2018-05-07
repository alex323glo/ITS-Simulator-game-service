package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.Planet;

import java.util.List;

/**
 * General Planet Service, which covers business logic
 * of Planet management.
 *
 * @author Alexey_O
 * @version 0.1
 */
public interface PlanetService {

    /**
     * Saves new Planet to System.
     *
     * @param planetName unique and valid name of new Planet.
     * @param positionX valid X coordinate of new Planet.
     * @param positionY valid Y coordinate of new Planet.
     * @return (not null) saved Planet, if operation was successfull.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Planet createPlanet(String planetName, Long positionX, Long positionY) throws AppException;

    /**
     * Searches for existent Planet.
     *
     * @param planetName unique and valid Planet name.
     * @return needed Planet, if it was created before, or null, if it wasn't.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Planet findPlanet(String planetName) throws AppException;

    /**
     * Lists all existent Planets.
     *
     * @return (not null) List of existent Planets, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    List<Planet> findAllPlanets() throws AppException;

    /**
     * Deletes all planets from System.
     *
     * If wasn't thrown any Exception, operation is completely successful.
     *
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    void deleteAllPlanets() throws AppException;
}
