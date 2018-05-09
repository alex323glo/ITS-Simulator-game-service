package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.ValidationException;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.model.game.SpaceShipStatus;
import org.alex323glo.its_simulator.repository.SpaceShipRepository;
import org.alex323glo.its_simulator.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of SpaceShipService interface. See more in abstraction.
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see SpaceShipService
 */
@Service
public class SpaceShipServiceImpl implements SpaceShipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceShipServiceImpl.class);

    private final Validator validator;
    private final SpaceShipRepository spaceShipRepository;
    private final UserService userService;

    @Autowired
    public SpaceShipServiceImpl(Validator validator, SpaceShipRepository spaceShipRepository, UserService userService) {
        this.validator = validator;
        this.spaceShipRepository = spaceShipRepository;
        this.userService = userService;
    }

    /**
     * Saves new User's SpaceShip to System.
     *
     * @param username         unique and valid username of registered User.
     * @param shipName         unique and valid name of new SpaceShip.
     * @param maxCargoCapacity maximum payload of new SpaceShip.
     * @param level            initial level of new SpaceShip.
     * @param speed            initial speed of new SpaceShip.
     * @return (not null) saved SpaceShip, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public SpaceShip createSpaceShip(String username, String shipName, Double maxCargoCapacity,
                                     Integer level, Double speed) throws AppException {
        LOGGER.info("Trying to create new SpaceShip...");

        try {
            validator.validateUsername(username).validateSpaceShipName(shipName).validatePayload(maxCargoCapacity)
                    .validateSpaceShipLevel(level).validateSpaceShipSpeed(speed);
        } catch (ValidationException e) {
            AppException exception = new AppException(
                    "Can't create new SpaceShip. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        UserGameProfile userGameProfile = userService.findUserGameProfile(username);
        if (userGameProfile == null) {
            AppException exception = new AppException(
                    "Attempt to edit personal data (create new SpaceShip) of non-existent User.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        if (spaceShipRepository.findByNameAndUserGameProfile_User_Username(shipName, username) != null) {
            AppException exception = new AppException("Attempt to save new SpaceShip with duplicate name.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        SpaceShip spaceShip = SpaceShip.builder()
                .userGameProfile(userGameProfile)
                .name(shipName)
                .maxCargoCapacity(maxCargoCapacity)
                .creationTime(LocalDateTime.now())
                .spaceShipStatus(SpaceShipStatus.FREE)
                .level(level)
                .speed(speed)
                .build();

        spaceShip.getUserGameProfile().setShipsNumber(
                spaceShip.getUserGameProfile().getShipsNumber() + 1);

        SpaceShip savedSpaceShip = spaceShipRepository.save(spaceShip);

        LOGGER.info("Successfully created new SpaceShip.");
        return savedSpaceShip;
    }

    /**
     * Searches for existent User's SpaceShip.
     *
     * @param username unique and valid username of registered User.
     * @param shipName unique and valid name of existent SpaceShip.
     * @return needed SpaceShip, if it was created before, or null, if it wasn't.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public SpaceShip findSpaceShip(String username, String shipName) throws AppException {
        LOGGER.info("Trying to find SpaceShip by User's username and SpaceShip's name...");

        try {
            validator.validateUsername(username).validateSpaceShipName(shipName);
        } catch (ValidationException e) {
            AppException exception = new AppException(
                    "Can't find SpaceShip by User's username and SpaceShip's name. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        SpaceShip spaceShip = spaceShipRepository.findByNameAndUserGameProfile_User_Username(shipName, username);

        if (spaceShip == null) {
            LOGGER.warn("No ship with such name was saved to Data Base.");
            return null;
        }

        String storedUsername = spaceShip.getUserGameProfile().getUser().getUsername();
        if (!username.equals(storedUsername)) {     // TODO replace with more special Exception type!!!
            AppException exception = new AppException(
                    "Attempt to access private data (SpaceShip) by User, who is not its owner at all.");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        LOGGER.info("Successfully found SpaceShip by User's username and SpaceShip's name.");
        return spaceShip;
    }

    /**
     * Lists all User's existent SpaceShips.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of User's SpaceShip's, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public List<SpaceShip> findAllShips(String username) throws AppException {
        LOGGER.info("Trying to list all SpaceShips by User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception =
                    new AppException("Can't list all SpaceShips by User's username. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        List<SpaceShip> spaceShipList = spaceShipRepository.findAllByUserGameProfile_User_Username(username);

        LOGGER.info("Successfully listed all SpaceShips by User's username.");
        return spaceShipList;
    }

    /**
     * Lists all User's existent SpaceShips with FREE status.
     *
     * @param username unique and valid username of registered User.
     * @return (not null) List of User's FREE SpaceShip's, if operation was successful.
     * @throws AppException if System can't carry out this operation in some reasons
     *                      (see more in method's realisation).
     */
    @Transactional
    @Override
    public List<SpaceShip> findAllFreeShips(String username) throws AppException {
        LOGGER.info("Trying to list all free SpaceShips by User's username...");

        try {
            validator.validateUsername(username);
        } catch (ValidationException e) {
            AppException exception =
                    new AppException("Can't list all free SpaceShips by User's username. " + e.getMessage());
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }

        List<SpaceShip> spaceShipList = spaceShipRepository
                .findAllByUserGameProfile_User_UsernameAndSpaceShipStatus(username, SpaceShipStatus.FREE);

        LOGGER.info("Successfully listed all free SpaceShips by User's username.");
        return spaceShipList;
    }
}
