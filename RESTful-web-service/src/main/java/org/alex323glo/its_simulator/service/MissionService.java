package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.MissionMetrics;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;

import java.util.List;

/**
 * General Mission Service, which covers business logic of search,
 * creation and management of User's Missions.
 *
 * @author Alexey_O
 * @version 0.1
 */
public interface MissionService {

    /**
     * Searches for existent Mission in System.
     *
     * @param username unique and valid username of registered User.
     * @param missionId unique identifier of existent mission.
     * @return needed Mission, if it was created before, or null, if it wasn't.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Mission findMission(String username, Long missionId) throws AppException;

    /**
     * Starts User's (created) Mission.
     *
     * @param username unique and valid username of registered User.
     * @param mission needed Mission instance.
     * @return (not null) updated Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Mission startMission(String username, Mission mission) throws AppException;

    /**
     * Cancels User's (started) Mission.
     *
     * @param username unique and valid username of registered User.
     * @param mission needed Mission instance.
     * @return (not null) updated Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Mission cancelMission(String username, Mission mission) throws AppException;

    /**
     * Calculates Mission metrics (MissionMetrics object) according to proposed params.
     *
     * @param username unique and valid username of registered User.
     * @param startPlanetName start point of mission.
     * @param destinationPlanetName destination point of mission.
     * @param spaceShipName target SpaceShip, which will serve the Mission.
     * @param payload cargo, which will be transported during the Mission.
     * @return (not null) MissionMetrics, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    MissionMetrics generateMissionMetrics(String username, String startPlanetName, String destinationPlanetName,
                                                 String spaceShipName, Double payload) throws AppException;

    /**
     * Constructs and saves new Mission to System.
     *
     * @param username unique and valid username of registered User.
     * @param startPlanetName start point of mission.
     * @param destinationPlanetName destination point of mission.
     * @param spaceShipName target SpaceShip, which will serve the Mission.
     * @param payload cargo, which will be transported during the Mission.
     * @return (not null) constructed and saved to Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    Mission constructNewMission(String username, String startPlanetName, String destinationPlanetName,
                                String spaceShipName, Double payload) throws AppException;

    /**
     * Lists all User's existent Missions.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of target User's Missions, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     * (see more in method's realisation).
     */
    List<Mission> findAllMissions(String username) throws AppException;

}
