package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.SpaceShip;

import java.util.List;

public interface SpaceShipService {

    SpaceShip createShip(UserGameProfile gameProfile, String shipName, Double maxCargoCapacity)
            throws AppException;
    SpaceShip findShip(String shipName) throws AppException;
    Mission deleteShipAndCancelCurrentMission(SpaceShip spaceShip) throws AppException;

    Mission attachMission(SpaceShip spaceShip, Mission mission) throws AppException;
    Mission detachMission(SpaceShip spaceShip) throws AppException;

    List<SpaceShip> findAllShips(UserGameProfile userGameProfile) throws AppException;
    List<SpaceShip> findAllBusyShips(UserGameProfile userGameProfile) throws AppException;
    List<SpaceShip> findAllFreeShips(UserGameProfile userGameProfile) throws AppException;

}
