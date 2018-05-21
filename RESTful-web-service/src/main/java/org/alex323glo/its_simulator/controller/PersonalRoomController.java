package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.service.UserService;
import org.alex323glo.its_simulator.util.CircularityResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Personal Room REST Controller.
 *
 * Uses UserService to carry out business logic.
 *
 * Serves such endpoints:
 *  1) '/private/personal-room/user-data'
 *      - method: GET;
 *      - params: no;
 *      - response: OK (200) with UserGameProfile object as body;
 *      - must be authenticated!
 *
 *  2) '/private/personal-room/edit'
 *      - method: POST;
 *      - params: UserExtension object;
 *      - response: OK (200) with UserExtension object as body;
 *      - must be authenticated!
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
@RequestMapping("/private/personal-room")
public class PersonalRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalRoomController.class);

    private final UserService userService;

    @Autowired
    public PersonalRoomController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user-data")
    public ResponseEntity<UserGameProfile> sendUserData(Principal principal) {
        LOGGER.info("Serving '/private/personal-room/user-extension' endpoint (GET request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to access private info (User object data)!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {

            try {
                UserGameProfile userGameProfile = userService.findUserGameProfile(principal.getName());

                if (userGameProfile == null) {
                    LOGGER.error("Can't find needed User in Data Base.");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                userGameProfile = CircularityResolver.resolveLazyGameProfile(userGameProfile);
                userGameProfile.getUser().setPassword(null);

                LOGGER.info("Successfully served '/private/personal-room/user-extension' endpoint " +
                        "(send User object data to '" + principal.getName() + "' user).");
                return new ResponseEntity<>(userGameProfile, HttpStatus.OK);
            } catch (AppException e) {
                LOGGER.error(e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }


    @PostMapping("/edit")
    public ResponseEntity<UserExtension> editUserExtension(
            @RequestBody UserExtension newUserExtension, Principal principal) {

        LOGGER.info("Serving '/private/personal-room/edit' endpoint (POST request)...");
        if (principal == null) {
            LOGGER.warn("Non-authorized User tries to edit private info (UserExtension object data)!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {

            try {
                UserExtension userExtension = userService.findUserExtension(principal.getName());

                if (userExtension == null) {
                    LOGGER.error("Can't find needed User in Data Base.");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                UserExtension changedUserExtension = userService
                        .changeUserExtension(principal.getName(), newUserExtension);

                changedUserExtension = CircularityResolver
                        .resolveUserExtensionWithLazyGameProfile(changedUserExtension);

                LOGGER.info("Successfully served '/private/personal-room/user-extension' endpoint " +
                        "(edited UserExtension object data of '" + principal.getName() + "' user).");
                return new ResponseEntity<>(changedUserExtension, HttpStatus.OK);
            } catch (AppException e) {
                LOGGER.error(e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

}
