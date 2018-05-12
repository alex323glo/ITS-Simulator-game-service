package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.MissionMetrics;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.service.MissionService;
import org.alex323glo.its_simulator.service.PlanetService;
import org.alex323glo.its_simulator.service.SpaceShipService;
import org.alex323glo.its_simulator.util.CircularityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Mission Constructor REST Controller.
 *
 * Uses PlanetService, SpaceShipService and MissionService
 * to carry out business logic.
 *
 * Serves such endpoints:
 *  1) '/private/mission-constructor/planet-list'
 *      - method: GET;
 *      - params: no;
 *      - response: OK (200) with List of Planets as body;
 *      - must be authenticated!
 *
 *  2) '/private/mission-constructor/free-ship-list'
 *      - method: GET;
 *      - params: no;
 *      - response: OK (200) with List of SpaceShips (with FREE status) as body;
 *      - must be authenticated!
 *
 *  3) '/private/mission-constructor/analyze'
 *      - method: GET;
 *      - params: start, destination, ship, payload (see analyzeMission() method for details);
 *      - response: OK (200) with MissionMetrics object as body;
 *      - must be authenticated!
 *
 *  4) '/private/mission-constructor/construct'
 *      - method: POST;
 *      - params: start, destination, ship, payload (see analyzeMission() method for details);
 *      - response: OK (200) with Mission object as body;
 *      - must be authenticated!
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
@RequestMapping("/private/mission-constructor")
public class MissionConstructorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionConstructorController.class);

    private final PlanetService planetService;
    private final SpaceShipService spaceShipService;
    private final MissionService missionService;

    @Autowired
    public MissionConstructorController(PlanetService planetService, SpaceShipService spaceShipService, MissionService missionService) {
        this.planetService = planetService;
        this.spaceShipService = spaceShipService;
        this.missionService = missionService;
    }

    @GetMapping("/planet-list")
    public ResponseEntity<?> getAllPlanets(Principal principal) {
        LOGGER.info("Serving '/private/mission-constructor/planet-list' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Planet> planetList = planetService.findAllPlanets();

            LOGGER.info("Successfully served '/private/mission-constructor/planet-list' endpoint " +
                    "(send List of Planets to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(planetList, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/free-ship-list")
    public ResponseEntity<?> getAllFreeShips(Principal principal) {
        LOGGER.info("Serving '/private/mission-constructor/planet-list' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {

            List<SpaceShip> freeSpaceShipList = spaceShipService.findAllFreeShips(principal.getName());

            freeSpaceShipList.forEach(ship -> {
                ship.setUserGameProfile(null);
            });

            LOGGER.info("Successfully served '/private/mission-constructor/planet-list' endpoint " +
                    "(send List of free SpaceShips to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(freeSpaceShipList, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/analyze")
    public ResponseEntity<?> analyzeMission(
            @RequestParam(name = "start") String startPlanetName,
            @RequestParam(name = "destination") String destinationPlanetName,
            @RequestParam(name = "ship") String spaceShipName,
            @RequestParam(name = "payload") String payload,
            Principal principal) {
        LOGGER.info("Serving '/private/mission-constructor/analyze' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Double convertedPayload = null;
        try {
            convertedPayload = Double.valueOf(payload);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>("Wrong mission payload was sent.", HttpStatus.BAD_REQUEST);
        }

        try {

            MissionMetrics missionMetrics = missionService.generateMissionMetrics(
                    principal.getName(),
                    startPlanetName,
                    destinationPlanetName,
                    spaceShipName,
                    convertedPayload);

            LOGGER.info("Successfully served '/private/mission-constructor/analyze' endpoint " +
                    "(send calculated MissionMetrics to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(missionMetrics, HttpStatus.OK);

        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/construct")
    public ResponseEntity<?> constructNewMission(
            @RequestParam(name = "start") String startPlanetName,
            @RequestParam(name = "destination") String destinationPlanetName,
            @RequestParam(name = "ship") String spaceShipName,
            @RequestParam(name = "payload") String payload,
            Principal principal) {

        LOGGER.info("Serving '/private/mission-constructor/construct' endpoint (POST request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Double convertedPayload = null;
        try {
            convertedPayload = Double.valueOf(payload);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>("Wrong mission payload was sent.", HttpStatus.BAD_REQUEST);
        }

        try {

            Mission mission = missionService.constructNewMission(principal.getName(),
                    startPlanetName, destinationPlanetName, spaceShipName, convertedPayload);

            mission.setUserGameProfile(
                    CircularityResolver.resolveLazyGameProfile(
                            mission.getUserGameProfile()));

            LOGGER.info("Successfully served '/private/mission-constructor/construct' endpoint " +
                    "(construct and send new Mission to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(mission, HttpStatus.OK);

        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
