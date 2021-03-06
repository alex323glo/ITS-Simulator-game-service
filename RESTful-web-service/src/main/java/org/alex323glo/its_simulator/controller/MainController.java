package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.service.SpaceShipService;
import org.alex323glo.its_simulator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

/**
 * Main REST Controller.
 *
 * Uses UserService to carry out business logic.
 *
 * Serves such endpoints:                           <br>
 *  1) '/register'                                  <br>
 *      - method: POST;                             <br>
 *      - params: username, password, email;        <br>
 *      - response: REDIRECT (308) to '/login');    <br>
 *
 *  2) '/get-authenticated-username'                <br>
 *      - method: GET;                              <br>
 *      - params: no;                               <br>
 *      - response: OK (200) with username as body; <br>
 *      - must be authenticated!                    <br>
 *
 * @author Alexey_O
 * @version 0.1
 *
 * @see UserService
 */
@RestController
public class MainController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MainController.class);

    private final UserService userService;

    private final SpaceShipService spaceShipService;

    @Autowired
    public MainController(UserService userService, SpaceShipService spaceShipService) {
        this.userService = userService;
        this.spaceShipService = spaceShipService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email) {
        LOGGER.info("Serving '/register' endpoint (POST request)...");
        try {

            userService.registerUser(username, password, email);

            spaceShipService.createSpaceShip(username, "Dragon-1",
                    50.0, 1, 30.0);

            spaceShipService.createSpaceShip(username, "Dragon-2",
                    70.0, 2, 15.0);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/login"));
            LOGGER.info("Successfully served '/register' endpoint (user {username: '" +
                    username + "', password: '" +
                    password + "', email: '" +
                    email + "'} was registered to system).");
            return new ResponseEntity<>(httpHeaders, HttpStatus.PERMANENT_REDIRECT);
        } catch (AppException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-authenticated-username")
    public ResponseEntity<String> getAuthenticatedUsername(Principal principal) {
        LOGGER.info("Serving '/get-authenticated-username' endpoint (GET request)...");

        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to get authorized principal (username)!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            LOGGER.info("Successfully served '/get-authenticated-username' endpoint (for user '" +
                    principal.getName() + "').");

            return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
        }
    }

}
