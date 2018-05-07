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

}
