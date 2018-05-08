package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.*;
import org.alex323glo.its_simulator.repository.MissionRepository;
import org.alex323glo.its_simulator.repository.PlanetRepository;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MissionServiceTest {

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_SPACE_SHIP_NAME = "Dragon-1";
    private static final Integer TEST_SPACE_SHIP_LEVEL = 1;
    private static final Double TEST_SPACE_SHIP_SPEED = 15.50;
    private static final Double TEST_SPACE_SHIP_MAX_CARGO_CAPACITY = 1.0;

    private static final String ANOTHER_TEST_SPACE_SHIP_NAME = "Dragon-2";

    private static final String TEST_START_PLANET_NAME = "P-001";
    private static final Long TEST_START_PLANET_POSITION_X = 50L;
    private static final Long TEST_START_PLANET_POSITION_Y = 50L;

    private static final String TEST_DESTINATION_PLANET_NAME = "P-002";
    private static final Long TEST_DESTINATION_PLANET_POSITION_X = 300L;
    private static final Long TEST_DESTINATION_PLANET_POSITION_Y = 300L;

    private static final Long TEST_MISSION_DURATION_SECONDS = 30L;
    private static final Double TEST_MISSION_PAYLOAD = 0.5;

    private static final String SPACE_SHIP_LEVEL_COEFFICIENT_PROP_NAME = "game.mechanics.ship_level_coefficient";
    private static final String TIME_COEFFICIENT_SECONDS_PROP_NAME = "game.mechanics.time_coefficient_seconds";

    private static User testUser;
    private static Mission testMission;
    private static SpaceShip testSpaceShip;
    private static Planet testStartPlanet;
    private static Planet testDestinationPlanet;

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private Environment environment;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
        planetRepository.deleteAll();

        testStartPlanet = planetRepository.save(
                Planet.builder()
                        .name(TEST_START_PLANET_NAME)
                        .positionX(TEST_START_PLANET_POSITION_X)
                        .positionY(TEST_START_PLANET_POSITION_Y)
                        .build());
        testDestinationPlanet = planetRepository.save(
                Planet.builder()
                        .name(TEST_DESTINATION_PLANET_NAME)
                        .positionX(TEST_DESTINATION_PLANET_POSITION_X)
                        .positionY(TEST_DESTINATION_PLANET_POSITION_Y)
                        .build());

        testUser = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
        testUser.setUserExtension(
                UserExtension.builder()
                        .email(TEST_EMAIL)
                        .registrationTime(LocalDateTime.now())
                        .user(testUser)
                        .build()
        );
        testUser.setUserGameProfile(
                UserGameProfile.builder()
                        .user(testUser)
                        .ships(new ArrayList<>())
                        .missions(new ArrayList<>())
                        .build()
        );
        testMission = Mission.builder()
                .userGameProfile(testUser.getUserGameProfile())
                .missionStatus(MissionStatus.CREATED)
                .duration(TEST_MISSION_DURATION_SECONDS)
                .startPoint(testStartPlanet)
                .destinationPoint(testDestinationPlanet)
                .payload(TEST_MISSION_PAYLOAD)
                .registrationTime(LocalDateTime.now())
                .startTime(LocalDateTime.now())
                .build();

        testSpaceShip = SpaceShip.builder()
                .userGameProfile(testUser.getUserGameProfile())
                .name(TEST_SPACE_SHIP_NAME)
                .creationTime(LocalDateTime.now())
                .maxCargoCapacity(TEST_SPACE_SHIP_MAX_CARGO_CAPACITY)
                .level(TEST_SPACE_SHIP_LEVEL)
                .speed(TEST_SPACE_SHIP_SPEED)
                .spaceShipStatus(SpaceShipStatus.BUSY)
                .build();
        SpaceShip anotherTestSpaceShip = SpaceShip.builder()
                .userGameProfile(testUser.getUserGameProfile())
                .name(ANOTHER_TEST_SPACE_SHIP_NAME)
                .maxCargoCapacity(TEST_SPACE_SHIP_MAX_CARGO_CAPACITY)
                .creationTime(LocalDateTime.now())
                .spaceShipStatus(SpaceShipStatus.FREE)
                .level(TEST_SPACE_SHIP_LEVEL)
                .speed(TEST_SPACE_SHIP_SPEED)
                .build();

        testMission.setSpaceShip(testSpaceShip);
        testUser.getUserGameProfile().getShips().add(testSpaceShip);
        testUser.getUserGameProfile().getShips().add(anotherTestSpaceShip);
        testUser.getUserGameProfile().getMissions().add(testMission);

        testUser = userRepository.save(testUser);

        List<Mission> allMissions = missionRepository.findAll();
        testMission = allMissions.get(0);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        missionRepository.deleteAll();
    }



    @Test
    public void findMission() throws AppException {
        Mission mission = missionService.findMission(testUser.getUsername(), testMission.getId());

        mission.setUserGameProfile(null);
        testMission.setUserGameProfile(null);
        mission.getSpaceShip().setUserGameProfile(null);
        testMission.getSpaceShip().setUserGameProfile(null);

        assertEquals(testMission, mission);
    }

    @Test
    public void startMission() throws AppException {
        Mission startedMission = missionService.startMission(testUser.getUsername(), testMission);
        testMission.setMissionStatus(MissionStatus.STARTED);

        assertEquals(MissionStatus.STARTED, startedMission.getMissionStatus());

        Optional<Mission> storedMissionOptional = missionRepository.findById(startedMission.getId());
        if (storedMissionOptional.isPresent()) {
            assertEquals(MissionStatus.STARTED, storedMissionOptional.get().getMissionStatus());
        } else {
            fail("mission is not present in DB");
        }
    }

    @Test
    public void cancelMission() throws AppException {
        missionService.startMission(testUser.getUsername(), testMission);

        Mission canceledMission = missionService.cancelMission(testUser.getUsername(), testMission);
        assertEquals(MissionStatus.CANCELED, canceledMission.getMissionStatus());

        Optional<Mission> storedMissionOptional = missionRepository.findById(canceledMission.getId());
        if (storedMissionOptional.isPresent()) {
            assertEquals(MissionStatus.CANCELED, storedMissionOptional.get().getMissionStatus());
        } else {
            fail("mission is not present in DB");
        }
    }

    @Test
    public void completeMission() throws AppException {
        missionService.startMission(testUser.getUsername(), testMission);

        Mission completedMission = missionService.completeMission(testUser.getUsername(), testMission);
        assertEquals(MissionStatus.COMPLETED, completedMission.getMissionStatus());

        Optional<Mission> storedMissionOptional = missionRepository.findById(completedMission.getId());
        if (storedMissionOptional.isPresent()) {
            assertEquals(MissionStatus.COMPLETED, storedMissionOptional.get().getMissionStatus());
        } else {
            fail("mission is not present in DB");
        }
    }

    @Test
    public void generateMissionMetrics() throws AppException {

        MissionMetrics expectedMissionMetrics = MissionMetrics.builder()
                .shipName(TEST_SPACE_SHIP_NAME)
                .shipLevel(TEST_SPACE_SHIP_LEVEL)
                .shipSpeed(TEST_SPACE_SHIP_SPEED)
                .shipMaxPayload(TEST_SPACE_SHIP_MAX_CARGO_CAPACITY)
                .actualPayload(TEST_MISSION_PAYLOAD)
                .startPlanetName(TEST_START_PLANET_NAME)
                .startPositionX(TEST_START_PLANET_POSITION_X)
                .startPositionY(TEST_START_PLANET_POSITION_Y)
                .destinationPlanetName(TEST_DESTINATION_PLANET_NAME)
                .destinationPositionX(TEST_DESTINATION_PLANET_POSITION_X)
                .destinationPositionY(TEST_DESTINATION_PLANET_POSITION_Y)
                .build();
        expectedMissionMetrics.setDistance(MissionMetrics.calculateDistance(
                TEST_START_PLANET_POSITION_X,
                TEST_START_PLANET_POSITION_Y,
                TEST_DESTINATION_PLANET_POSITION_X,
                TEST_DESTINATION_PLANET_POSITION_Y));
        expectedMissionMetrics.setDuration(MissionMetrics.calculateDuration(
                expectedMissionMetrics.getDistance(),
                expectedMissionMetrics.getShipSpeed(),
                expectedMissionMetrics.getShipLevel(),
                Double.valueOf(environment.getProperty(SPACE_SHIP_LEVEL_COEFFICIENT_PROP_NAME)),
                Double.valueOf(environment.getProperty(TIME_COEFFICIENT_SECONDS_PROP_NAME))
        ));

        MissionMetrics missionMetrics = missionService.generateMissionMetrics(testUser.getUsername(),
                testStartPlanet.getName(), testDestinationPlanet.getName(), testSpaceShip.getName(),
                testMission.getPayload());

        assertEquals(expectedMissionMetrics, missionMetrics);
    }

    @Test
    public void constructNewMission() throws AppException {
        Mission constructedMission = missionService.constructNewMission(
                TEST_USERNAME,
                TEST_START_PLANET_NAME,
                TEST_DESTINATION_PLANET_NAME,
                ANOTHER_TEST_SPACE_SHIP_NAME,
                TEST_MISSION_PAYLOAD);

        Optional<Mission> optionalMission = missionRepository.findById(constructedMission.getId());
        assertTrue(optionalMission.isPresent());

        constructedMission.setUserGameProfile(null);
        optionalMission.get().setUserGameProfile(null);
        constructedMission.getSpaceShip().setUserGameProfile(null);
        optionalMission.get().getSpaceShip().setUserGameProfile(null);

        assertEquals(optionalMission.get(), constructedMission);
    }

    @Test
    public void findAllMissions() throws AppException {
        List<Mission> allMissions = missionService.findAllMissions(TEST_USERNAME);

        allMissions.get(0).setUserGameProfile(null);
        testMission.setUserGameProfile(null);
        allMissions.get(0).getSpaceShip().setUserGameProfile(null);
        testMission.getSpaceShip().setUserGameProfile(null);

        assertEquals(Stream.builder().add(testMission).build().collect(Collectors.toList()), allMissions);
    }
}