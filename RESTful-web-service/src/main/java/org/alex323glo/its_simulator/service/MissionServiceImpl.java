package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.ValidationException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.*;
import org.alex323glo.its_simulator.repository.MissionRepository;
import org.alex323glo.its_simulator.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of MissionService interface. See more in abstraction.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see MissionService
 */
@Service
public class MissionServiceImpl implements MissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionServiceImpl.class);

    private final Validator validator;
    private final MissionRepository missionRepository;
    private final SpaceShipService spaceShipService;
    private final PlanetService planetService;
    private final Environment environment;

    @Autowired
    public MissionServiceImpl(Validator validator, MissionRepository missionRepository,
                              SpaceShipService spaceShipService, PlanetService planetService, Environment environment) {

        this.validator = validator;
        this.missionRepository = missionRepository;
        this.spaceShipService = spaceShipService;
        this.planetService = planetService;
        this.environment = environment;
    }

    /**
     * Searches for existent Mission in System.
     *
     * @param username  unique and valid username of registered User.
     * @param missionId unique identifier of existent mission.
     * @return needed Mission, if it was created before, or null, if it wasn't.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public Mission findMission(String username, Long missionId) throws AppException {
        LOGGER.info("Trying to find Mission by ID and User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't find Mission by ID and User's username. " + e.getMessage(), e);
            throw new AppException(e);
        }

        Optional<Mission> mission = missionRepository.findById(missionId);
        if (!mission.isPresent()) {
            LOGGER.warn("No Mission with such ID was saved to System.");
            return null;
        }

        String storedUsername = mission.get().getUserGameProfile().getUser().getUsername();
        if (!username.equals(storedUsername)) {     // TODO replace with more special Exception type!!!
            AppException exception = new AppException(
                    "Attempt to access private data (Mission) by User, who is not its owner at all");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        LOGGER.info("Successfully found Mission by ID and User's username.");
        return mission.get();
    }

    /**
     * Starts User's (created) Mission.
     *
     * @param username unique and valid username of registered User.
     * @param mission  needed Mission instance.
     * @return (not null) updated Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public Mission startMission(String username, Mission mission) throws AppException {
        LOGGER.info("Trying to start Mission by User's username and Mission instance...");

        if (mission == null || mission.getId() == null) {
            AppException exception =
                    new AppException("Can't user Mission instance: Mission instance is null of Mission.id is null.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't start Mission by User's username and Mission instance. " + e.getMessage(), e);
            throw new AppException(e);
        }

        Optional<Mission> storedMission = missionRepository.findById(mission.getId());
        if (!storedMission.isPresent()) {
            LOGGER.warn("No Mission with such ID was saved to System.");
            return null;
        }

        String storedUsername = storedMission.get().getUserGameProfile().getUser().getUsername();
        if (!username.equals(storedUsername)) {     // TODO replace with more special Exception type!!!
            AppException exception = new AppException(
                    "Attempt to access private data (Mission) by User, who is not its owner at all");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        MissionStatus missionStatus = storedMission.get().getMissionStatus();
        if (missionStatus != MissionStatus.CREATED) {
            AppException exception = new AppException("Attempt to start Mission, which status is " +
                    missionStatus.name() + " (not CREATED, as required).");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        storedMission.get().setMissionStatus(MissionStatus.STARTED);
//        missionRepository.flush();

        LOGGER.info("Successfully started Mission by User's username and Mission instance.");
        return storedMission.get();
    }

    /**
     * Cancels User's (started) Mission.
     *
     * @param username unique and valid username of registered User.
     * @param mission  needed Mission instance.
     * @return (not null) updated Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public Mission cancelMission(String username, Mission mission) throws AppException {
        LOGGER.info("Trying to cancel Mission by User's username and Mission instance...");

        if (mission == null || mission.getId() == null) {
            AppException exception =
                    new AppException("Can't user Mission instance: Mission instance is null of Mission.id is null.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            LOGGER.error("Can't cancel Mission by User's username and Mission instance. " + e.getMessage(), e);
            throw new AppException(e);
        }

        Optional<Mission> storedMission = missionRepository.findById(mission.getId());
        if (!storedMission.isPresent()) {
            LOGGER.warn("No Mission with such ID was saved to System.");
            return null;
        }

        String storedUsername = storedMission.get().getUserGameProfile().getUser().getUsername();
        if (!username.equals(storedUsername)) {     // TODO replace with more special Exception type!!!
            AppException exception = new AppException(
                    "Attempt to access private data (Mission) by User, who is not its owner at all.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        MissionStatus missionStatus = storedMission.get().getMissionStatus();
        if (missionStatus != MissionStatus.CREATED && missionStatus != MissionStatus.STARTED) {
            AppException exception = new AppException("Attempt to start Mission, which status is " +
                    missionStatus.name() + " (not CREATED or STARTED, as required).");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        storedMission.get().setMissionStatus(MissionStatus.CANCELED);
//        missionRepository.flush();

        LOGGER.info("Successfully canceled Mission by User's username and Mission instance.");
        return storedMission.get();
    }

    /**
     * Calculates Mission metrics (MissionMetrics object) according to proposed params.
     *
     * @param username              unique and valid username of registered User.
     * @param startPlanetName       start point of mission.
     * @param destinationPlanetName destination point of mission.
     * @param spaceShipName         target SpaceShip, which will serve the Mission.
     * @param payload               cargo, which will be transported during the Mission.
     * @return (not null) MissionMetrics, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public MissionMetrics generateMissionMetrics(String username, String startPlanetName,
                                                 String destinationPlanetName, String spaceShipName,
                                                 Double payload) throws AppException {
        LOGGER.info("Trying to generate MissionMetrics by User's username and Mission details(...) ...");

        try {
            validator.validateUsername(username).ifNull(startPlanetName).ifNull(destinationPlanetName)
                    .ifNull(spaceShipName).validatePayload(payload);
        } catch (ValidationException e) {
            LOGGER.error("Can't generate MissionMetrics by User's username and Mission details(...). " +
                    e.getMessage(), e);
            throw new AppException(e);
        }

        SpaceShip spaceShip = spaceShipService.findSpaceShip(username, spaceShipName);

        if (spaceShip.getMaxCargoCapacity() < payload) {
            AppException exception =
                    new AppException("Wrong payload: payload is greater the SpaceShip's max cargo capacity.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        Planet startPlanet = planetService.findPlanet(startPlanetName);
        Planet destinationPlanet = planetService.findPlanet(destinationPlanetName);

        if (startPlanet.getPositionX().equals(destinationPlanet.getPositionX()) &&
                startPlanet.getPositionY().equals(destinationPlanet.getPositionY())) {
            AppException exception =
                    new AppException("Wrong start and/or destination planet(s): their positions are equal.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        MissionMetrics missionMetrics = MissionMetrics.builder()
                .shipName(spaceShip.getName())
                .shipLevel(spaceShip.getLevel())
                .shipSpeed(spaceShip.getSpeed())
                .shipMaxPayload(spaceShip.getMaxCargoCapacity())
                .actualPayload(payload)
                .startPlanetName(startPlanet.getName())
                .startPositionX(startPlanet.getPositionX())
                .startPositionY(startPlanet.getPositionY())
                .destinationPlanetName(destinationPlanet.getName())
                .destinationPositionX(destinationPlanet.getPositionX())
                .destinationPositionY(destinationPlanet.getPositionY())
                .build();

        Double shipLevelCoefficient = null;
        Double timeCoefficient = null;
        try {
            shipLevelCoefficient = Double.valueOf(environment.getProperty("game.mechanics.ship_level_coefficient"));
            timeCoefficient = Double.valueOf(environment.getProperty("game.mechanics.time_coefficient_seconds"));
        } catch (NumberFormatException e) {
            AppException exception =
                    new AppException("Can't generate MissionMetrics by User's username and Mission details(...). " +
                            e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        missionMetrics.refreshDistanceAndDuration(shipLevelCoefficient, timeCoefficient);

        LOGGER.info("Successfully generated MissionMetrics by User's username and Mission details(...).");
        return missionMetrics;
    }

    /**
     * Constructs and saves new Mission to System.
     *
     * @param username              unique and valid username of registered User.
     * @param startPlanetName       start point of mission.
     * @param destinationPlanetName destination point of mission.
     * @param spaceShipName         target SpaceShip, which will serve the Mission.
     * @param payload               cargo, which will be transported during the Mission.
     * @return (not null) constructed and saved to Mission object, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public Mission constructNewMission(String username, String startPlanetName, String destinationPlanetName,
                                       String spaceShipName, Double payload) throws AppException {
        LOGGER.info("Trying to construct new Mission by User's username and Mission details(...) ...");

        try {
            validator.validateUsername(username).ifNull(startPlanetName).ifNull(destinationPlanetName)
                    .ifNull(spaceShipName).validatePayload(payload);
        } catch (ValidationException e) {
            LOGGER.error("Can't construct new Mission by User's username and Mission details(...). " +
                    e.getMessage(), e);
            throw new AppException(e);
        }

        SpaceShip spaceShip = spaceShipService.findSpaceShip(username, spaceShipName);

        if (spaceShip.getSpaceShipStatus().equals(SpaceShipStatus.BUSY)) {
            AppException exception =
                    new AppException("Attempt to attach to new Mission BUSY SpaceShip (must be FREE).");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (spaceShip.getMaxCargoCapacity() < payload) {
            AppException exception =
                    new AppException("Wrong payload: payload is greater the SpaceShip's max cargo capacity.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        Planet startPlanet = planetService.findPlanet(startPlanetName);
        Planet destinationPlanet = planetService.findPlanet(destinationPlanetName);

        if (startPlanet.getPositionX().equals(destinationPlanet.getPositionX()) &&
                startPlanet.getPositionY().equals(destinationPlanet.getPositionY())) {
            AppException exception =
                    new AppException("Wrong start and/or destination planet(s): their positions are equal.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        Double shipLevelCoefficient = null;
        Double timeCoefficient = null;
        try {
            shipLevelCoefficient = Double.valueOf(environment.getProperty("game.mechanics.ship_level_coefficient"));
            timeCoefficient = Double.valueOf(environment.getProperty("game.mechanics.time_coefficient_seconds"));
        } catch (NumberFormatException e) {
            AppException exception =
                    new AppException("Can't construct new Mission by User's username and Mission details(...). " +
                            e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        Mission savedMission = missionRepository.save(Mission.builder()
                .userGameProfile(spaceShip.getUserGameProfile())
                .spaceShip(spaceShip)
                .startPoint(startPlanet)
                .destinationPoint(destinationPlanet)
                .registrationTime(LocalDateTime.now())
                .payload(payload)
                .duration(MissionMetrics.calculateDuration(
                        MissionMetrics.calculateDistance(
                                startPlanet.getPositionX(),
                                startPlanet.getPositionY(),
                                destinationPlanet.getPositionX(),
                                destinationPlanet.getPositionY()),
                        spaceShip.getSpeed(),
                        spaceShip.getLevel(),
                        shipLevelCoefficient,
                        timeCoefficient))
                .missionStatus(MissionStatus.CREATED)
                .build());

        LOGGER.info("Successfully constructed new Mission by User's username and Mission details(...).");
        return savedMission;
    }

    /**
     * Lists all User's existent Missions.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of target User's Missions, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public List<Mission> findAllMissions(String username) throws AppException {
        LOGGER.info("Trying to list all Missions by User's game profile...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception =
                    new AppException("Can't list all Missions by User's username. " + e.getMessage(), e);
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        List<Mission> allMissions = missionRepository.findAllByUserGameProfile_User_Username(username);

        LOGGER.info("Successfully listed all Missions by User's username");
        return allMissions;
    }
}
