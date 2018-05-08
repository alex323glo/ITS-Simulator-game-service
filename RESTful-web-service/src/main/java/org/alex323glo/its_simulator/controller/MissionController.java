package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.service.MissionService;
import org.alex323glo.its_simulator.util.CircularityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Mission REST Controller.
 *
 * Uses MissionService to carry out business logic.
 *
 * Serves such endpoints:
 *  1) '/private/mission/details'
 *      - method: GET;
 *      - params: id (of mission);
 *      - response: OK (200) with Mission object as body;
 *      - must be authenticated!
 *
 *  2) '/private/mission/start'
 *      - method: POST;
 *      - params: id (of mission);
 *      - response: OK (200) with Mission object as body;
 *      - must be authenticated!
 *
 *  3) '/private/mission/cancel'
 *      - method: POST;
 *      - params: id (of mission);
 *      - response: OK (200) with Mission object as body;
 *      - must be authenticated!
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
@RequestMapping("/private/mission")
public class MissionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionController.class);

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> getMission(
            @RequestParam(name = "id") String missionId,
            Principal principal) {

        LOGGER.info("Serving '/private/mission/details' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long convertedMissionId = null;
        try {
            convertedMissionId = Long.valueOf(missionId);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.BAD_REQUEST);
        }

        try {
            Mission mission = missionService.findMission(principal.getName(), convertedMissionId);
            mission.setUserGameProfile(
                    CircularityResolver.resolveLazyGameProfile(
                            mission.getUserGameProfile()));

            if (mission == null) {
                LOGGER.warn("Can't find such mission in Data Base.");
                return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.NOT_FOUND);
            }

            LOGGER.info("Successfully served '/private/mission/details' endpoint " +
                    "(send Mission to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(mission, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/start")
    public ResponseEntity<?> startMission(
            @RequestParam(name = "id") String missionId,
            Principal principal) {

        LOGGER.info("Serving '/private/mission/start' endpoint (POST request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long convertedMissionId = null;
        try {
            convertedMissionId = Long.valueOf(missionId);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.BAD_REQUEST);
        }

        try {
            Mission mission = missionService.findMission(principal.getName(), convertedMissionId);
            if (mission == null) {
                LOGGER.warn("Can't find such mission in Data Base.");
                return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.NOT_FOUND);
            }

            Mission startedMission = missionService.startMission(principal.getName(), mission);
            startedMission.setUserGameProfile(
                    CircularityResolver.resolveLazyGameProfile(
                            startedMission.getUserGameProfile()));

            LOGGER.info("Successfully served '/private/mission/start' endpoint " +
                    "(start Mission of '" + principal.getName() + "' user).");
            return new ResponseEntity<>(startedMission, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelMission(
            @RequestParam(name = "id") String missionId,
            Principal principal) {

        LOGGER.info("Serving '/private/mission/cancel' endpoint (POST request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long convertedMissionId = null;
        try {
            convertedMissionId = Long.valueOf(missionId);
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.BAD_REQUEST);
        }

        try {
            Mission mission = missionService.findMission(principal.getName(), convertedMissionId);
            if (mission == null) {
                LOGGER.warn("Can't find such mission in Data Base.");
                return new ResponseEntity<>("Wrong mission ID was sent.", HttpStatus.NOT_FOUND);
            }

            Mission canceledMission = missionService.cancelMission(principal.getName(), mission);
            canceledMission.setUserGameProfile(
                    CircularityResolver.resolveLazyGameProfile(
                            canceledMission.getUserGameProfile()));

            LOGGER.info("Successfully served '/private/mission/cancel' endpoint " +
                    "(cancel Mission of '" + principal.getName() + "' user).");
            return new ResponseEntity<>(canceledMission, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
