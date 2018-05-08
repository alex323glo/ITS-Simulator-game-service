package org.alex323glo.its_simulator.controller;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.MissionStatus;
import org.alex323glo.its_simulator.service.MissionService;
import org.alex323glo.its_simulator.service.PlanetService;
import org.alex323glo.its_simulator.service.SpaceShipService;
import org.alex323glo.its_simulator.service.UserService;
import org.alex323glo.its_simulator.util.CircularityResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MissionControllerTest {

    private static final String GET_MISSION_URI = "/private/mission/details";
    private static final String START_MISSION_URI = "/private/mission/start";
    private static final String CANCEL_MISSION_URI = "/private/mission/cancel";

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_SPACE_SHIP_NAME = "Dragon-1";
    private static final Integer TEST_SPACE_SHIP_LEVEL = 1;
    private static final Double TEST_SPACE_SHIP_SPEED = 15.50;
    private static final Double TEST_SPACE_SHIP_MAX_CARGO_CAPACITY = 1.0;

    private static final String TEST_START_PLANET_NAME = "P-001";
    private static final Long TEST_START_PLANET_POSITION_X = 50L;
    private static final Long TEST_START_PLANET_POSITION_Y = 50L;

    private static final String TEST_DESTINATION_PLANET_NAME = "P-002";
    private static final Long TEST_DESTINATION_PLANET_POSITION_X = 300L;
    private static final Long TEST_DESTINATION_PLANET_POSITION_Y = 300L;

    private static final Double TEST_MISSION_PAYLOAD = 0.5;


    private static Mission testMission;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private SpaceShipService spaceShipService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();

        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);

        spaceShipService.createSpaceShip(TEST_USERNAME, TEST_SPACE_SHIP_NAME,
                TEST_SPACE_SHIP_MAX_CARGO_CAPACITY, TEST_SPACE_SHIP_LEVEL, TEST_SPACE_SHIP_SPEED);

        planetService.createPlanet(TEST_START_PLANET_NAME,
                TEST_START_PLANET_POSITION_X, TEST_START_PLANET_POSITION_Y);

        planetService.createPlanet(TEST_DESTINATION_PLANET_NAME,
                TEST_DESTINATION_PLANET_POSITION_X, TEST_DESTINATION_PLANET_POSITION_Y);

        testMission = missionService.constructNewMission(TEST_USERNAME, TEST_START_PLANET_NAME,
                TEST_DESTINATION_PLANET_NAME, TEST_SPACE_SHIP_NAME, TEST_MISSION_PAYLOAD);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getMission() throws Exception {
        testMission.setUserGameProfile(
                CircularityResolver.resolveLazyGameProfile(
                        testMission.getUserGameProfile()));

        String testMissionJSON = new JacksonJsonProvider().toJson(testMission);
        mockMvc
                .perform(get(GET_MISSION_URI)
                        .param("id", testMission.getId().toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(testMissionJSON));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void startMission() throws Exception {
        mockMvc
                .perform(post(START_MISSION_URI)
                        .param("id", testMission.getId().toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Mission mission = missionService.findMission(TEST_USERNAME, testMission.getId());
                    assertEquals(MissionStatus.STARTED, mission.getMissionStatus());

                    mission.setUserGameProfile(
                            CircularityResolver.resolveLazyGameProfile(
                                    mission.getUserGameProfile()));

                    String missionJSON = new JacksonJsonProvider().toJson(mission);
                    assertEquals(missionJSON, result.getResponse().getContentAsString());
                });
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void cancelMission() throws Exception {
        missionService.startMission(TEST_USERNAME, testMission);
        mockMvc
                .perform(post(CANCEL_MISSION_URI)
                        .param("id", testMission.getId().toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Mission mission = missionService.findMission(TEST_USERNAME, testMission.getId());
                    assertEquals(MissionStatus.CANCELED, mission.getMissionStatus());

                    mission.setUserGameProfile(
                            CircularityResolver.resolveLazyGameProfile(
                                    mission.getUserGameProfile()));

                    String missionJSON = new JacksonJsonProvider().toJson(mission);
                    assertEquals(missionJSON, result.getResponse().getContentAsString());
                });
    }
}