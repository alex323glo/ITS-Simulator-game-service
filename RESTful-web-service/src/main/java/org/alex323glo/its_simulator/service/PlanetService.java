package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.Planet;

import java.util.List;

public interface PlanetService {

    Planet createNewPlanet(String name) throws AppException;
    Planet findPlanet(String planet) throws AppException;
    Planet deletePlanet(Planet planet) throws AppException;

    List<Planet> findAllPlanets() throws AppException;

}
