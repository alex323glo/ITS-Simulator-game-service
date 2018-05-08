package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.service.PlanetService;
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
 * Space Map REST Controller.
 *
 * Uses PlanetService to carry out business logic.
 *
 * Serves such endpoints:
 *  1) '/private/space-map/planets'
 *      - method: GET;
 *      - params: no;
 *      - response: OK (200) with List of Planets as body;
 *      - must be authenticated!
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
@RequestMapping("/private/space-map")
public class SpaceMapController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceMapController.class);

    private final PlanetService planetService;

    @Autowired
    public SpaceMapController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @GetMapping("/planets")
    public ResponseEntity<?> getAllPlanets(Principal principal) {
        LOGGER.info("Serving '/private/space-map/planets' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<Planet> allPlanets = planetService.findAllPlanets();

            LOGGER.info("Successfully served '/private/space-map/planets' endpoint " +
                    "(send all Planets to '" + principal.getName() + "' user).");
            return new ResponseEntity<>(allPlanets, HttpStatus.OK);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
