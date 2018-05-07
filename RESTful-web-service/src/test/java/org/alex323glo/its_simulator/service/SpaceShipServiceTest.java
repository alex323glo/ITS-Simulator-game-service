package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.model.game.SpaceShipStatus;
import org.alex323glo.its_simulator.repository.SpaceShipRepository;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpaceShipServiceTest {

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_SPACE_SHIP_NAME = "Dragon-1";
    private static final Integer TEST_SPACE_SHIP_LEVEL = 1;
    private static final Double TEST_SPACE_SHIP_SPEED = 15.50;
    private static final Double TEST_SPACE_SHIP_MAX_CARGO_CAPACITY = 1.0;

    @Autowired
    private SpaceShipService spaceShipService;

    @Autowired
    private SpaceShipRepository spaceShipRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();

        SpaceShip testSpaceShip = SpaceShip.builder()
                .name(TEST_SPACE_SHIP_NAME)
                .level(TEST_SPACE_SHIP_LEVEL)
                .speed(TEST_SPACE_SHIP_SPEED)
                .maxCargoCapacity(TEST_SPACE_SHIP_MAX_CARGO_CAPACITY)
                .spaceShipStatus(SpaceShipStatus.FREE)
                .creationTime(LocalDateTime.now())
                .build();
        List<SpaceShip> spaceShipList = new ArrayList<>();
        spaceShipList.add(testSpaceShip);

        User testUser = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .userExtension(
                        UserExtension.builder()
                                .registrationTime(LocalDateTime.now())
                                .email(TEST_EMAIL)
                                .build()
                )
                .userGameProfile(
                        UserGameProfile.builder()
                                .missions(new ArrayList<>())
                                .ships(spaceShipList)
                                .build()
                )
                .build();
        testUser.getUserGameProfile().setUser(testUser);
        testUser.getUserExtension().setUser(testUser);
        testSpaceShip.setUserGameProfile(testUser.getUserGameProfile());

        userRepository.save(testUser);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void findSpaceShip() throws AppException {
        SpaceShip expectedSpaceShip = spaceShipRepository.findByName(TEST_SPACE_SHIP_NAME);
        SpaceShip actualSpaceShip = spaceShipService.findSpaceShip(TEST_USERNAME, TEST_SPACE_SHIP_NAME);

        expectedSpaceShip.setUserGameProfile(null);
        actualSpaceShip.setUserGameProfile(null);
        assertEquals(expectedSpaceShip, actualSpaceShip);
    }

    @Test
    public void findAllFreeShips() throws AppException {
        SpaceShip expectedSpaceShip = spaceShipRepository.findByName(TEST_SPACE_SHIP_NAME);
        List<SpaceShip> allFreeShips = spaceShipService.findAllFreeShips(TEST_USERNAME);

        expectedSpaceShip.setUserGameProfile(null);
        allFreeShips.get(0).setUserGameProfile(null);
        assertEquals(
                Stream.builder().add(expectedSpaceShip).build().collect(Collectors.toList()),
                allFreeShips);
    }
}