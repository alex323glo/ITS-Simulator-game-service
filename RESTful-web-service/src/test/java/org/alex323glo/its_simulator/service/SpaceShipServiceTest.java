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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpaceShipServiceTest {

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_SPACE_SHIP_NAME = "Dragon-1";
    private static final String ANOTHER_TEST_SPACE_SHIP_NAME = "Dragon-2";
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
        SpaceShip anotherTestSpaceShip = SpaceShip.builder()
                .name(ANOTHER_TEST_SPACE_SHIP_NAME)
                .level(TEST_SPACE_SHIP_LEVEL)
                .speed(TEST_SPACE_SHIP_SPEED)
                .maxCargoCapacity(TEST_SPACE_SHIP_MAX_CARGO_CAPACITY)
                .spaceShipStatus(SpaceShipStatus.BUSY)
                .creationTime(LocalDateTime.now())
                .build();

        List<SpaceShip> spaceShipList = new ArrayList<>();
        spaceShipList.add(testSpaceShip);
        spaceShipList.add(anotherTestSpaceShip);

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
                                .experience(0L)
                                .completedMissionsNumber(0)
                                .shipsNumber(1)
                                .build()
                )
                .build();
        testUser.getUserGameProfile().setUser(testUser);
        testUser.getUserExtension().setUser(testUser);

        testSpaceShip.setUserGameProfile(testUser.getUserGameProfile());
        anotherTestSpaceShip.setUserGameProfile(testUser.getUserGameProfile());

        userRepository.save(testUser);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void createSpaceShip() throws AppException {
        String anotherTestSpaceShipName = "another_" + TEST_SPACE_SHIP_NAME;

        SpaceShip anotherTestSpaceShip = spaceShipService.createSpaceShip(
                TEST_USERNAME,
                anotherTestSpaceShipName,
                TEST_SPACE_SHIP_MAX_CARGO_CAPACITY,
                TEST_SPACE_SHIP_LEVEL,
                TEST_SPACE_SHIP_SPEED);

        SpaceShip expectedSpaceShip = spaceShipRepository.findByNameAndUserGameProfile_User_Username(
                anotherTestSpaceShipName, TEST_USERNAME);

        anotherTestSpaceShip.getUserGameProfile().getUser().setUserGameProfile(null);
        anotherTestSpaceShip.getUserGameProfile().getUser().setUserExtension(null);
        anotherTestSpaceShip.getUserGameProfile().setShips(null);
        anotherTestSpaceShip.getUserGameProfile().setMissions(null);

        expectedSpaceShip.getUserGameProfile().getUser().setUserGameProfile(null);
        expectedSpaceShip.getUserGameProfile().getUser().setUserExtension(null);
        expectedSpaceShip.getUserGameProfile().setShips(null);
        expectedSpaceShip.getUserGameProfile().setMissions(null);

        assertEquals(expectedSpaceShip, anotherTestSpaceShip);
    }

    @Test
    public void findSpaceShip() throws AppException {
        SpaceShip expectedSpaceShip = spaceShipRepository.findByNameAndUserGameProfile_User_Username(
                TEST_SPACE_SHIP_NAME, TEST_USERNAME);
        SpaceShip actualSpaceShip = spaceShipService.findSpaceShip(TEST_USERNAME, TEST_SPACE_SHIP_NAME);

        expectedSpaceShip.setUserGameProfile(null);
        actualSpaceShip.setUserGameProfile(null);
        assertEquals(expectedSpaceShip, actualSpaceShip);
    }

    @Test
    public void findAllShips() throws AppException {
        List<SpaceShip> allShips = spaceShipService.findAllShips(TEST_USERNAME);
        allShips.forEach(ship -> {
            ship.getUserGameProfile().getUser().setUserExtension(null);
            ship.getUserGameProfile().getUser().setUserGameProfile(null);
            ship.getUserGameProfile().setShips(null);
            ship.getUserGameProfile().setMissions(null);
        });

        List<SpaceShip> expectedShips = spaceShipRepository.findAll();
        expectedShips.forEach(expectedShip -> {
            expectedShip.getUserGameProfile().setShips(null);
            expectedShip.getUserGameProfile().setMissions(null);
            expectedShip.getUserGameProfile().getUser().setUserExtension(null);
            expectedShip.getUserGameProfile().getUser().setUserGameProfile(null);
        });

        assertEquals(expectedShips, allShips);
    }

    @Test
    public void findAllFreeShips() throws AppException {
        List<SpaceShip> expectedSpaceShipList =
                spaceShipRepository.findAllByUserGameProfile_User_UsernameAndSpaceShipStatus(
                        TEST_USERNAME, SpaceShipStatus.FREE);
        List<SpaceShip> allFreeShips = spaceShipService.findAllFreeShips(TEST_USERNAME);

        expectedSpaceShipList.get(0).setUserGameProfile(null);
        allFreeShips.get(0).setUserGameProfile(null);
        assertEquals(expectedSpaceShipList, allFreeShips);
    }
}