package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.ValidationException;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.model.game.SpaceShipStatus;
import org.alex323glo.its_simulator.repository.SpaceShipRepository;
import org.alex323glo.its_simulator.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public SpaceShipServiceImpl(Validator validator, SpaceShipRepository spaceShipRepository) {
        this.validator = validator;
        this.spaceShipRepository = spaceShipRepository;
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

        SpaceShip spaceShip = spaceShipRepository.findByName(shipName);

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
