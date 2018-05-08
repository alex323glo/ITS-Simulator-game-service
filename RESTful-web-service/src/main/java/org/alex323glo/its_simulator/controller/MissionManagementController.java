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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Mission Management REST Controller.
 *
 * Uses MissionService and UserService to carry out business logic.
 *
 * Serves such endpoints:
 *  1) '/private/mission-management/missions'
 *      - method: GET;
 *      - params: no;
 *      - response: OK (200) with List of Missions as body;
 *      - must be authenticated!
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
@RequestMapping("/private/mission-management")
public class MissionManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MissionManagementController.class);

    private final MissionService missionService;

    @Autowired
    public MissionManagementController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/missions")
    public ResponseEntity<List<Mission>> getMissionsList(Principal principal) {
        LOGGER.info("Serving '/private/mission-management/missions' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info (List of User's Missions)!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Mission> allMissions = missionService.findAllMissions(principal.getName());
            allMissions.forEach(mission -> mission.setUserGameProfile(
                    CircularityResolver.resolveLazyGameProfile(
                            mission.getUserGameProfile())));

            LOGGER.info("Successfully served '/private/mission-management/missions' endpoint " +
                    "(send List of Missions to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(allMissions, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // TODO add more endpoints...

}
