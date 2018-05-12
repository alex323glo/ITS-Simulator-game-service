package org.alex323glo.its_simulator.controller;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.game.Mission;
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

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MissionManagementControllerTest {

    private static final String GET_LIST_OF_MISSIONS_URI = "/private/mission-management/missions";

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private SpaceShipService spaceShipService;

    @Autowired
    private MissionService missionService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();


        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);

        planetService.createPlanet(TEST_START_PLANET_NAME,
                TEST_START_PLANET_POSITION_X, TEST_START_PLANET_POSITION_Y);

        planetService.createPlanet(TEST_DESTINATION_PLANET_NAME,
                TEST_DESTINATION_PLANET_POSITION_X, TEST_DESTINATION_PLANET_POSITION_Y);

        spaceShipService.createSpaceShip(TEST_USERNAME, TEST_SPACE_SHIP_NAME,
                TEST_SPACE_SHIP_MAX_CARGO_CAPACITY, TEST_SPACE_SHIP_LEVEL, TEST_SPACE_SHIP_SPEED);

        missionService.constructNewMission(TEST_USERNAME, TEST_START_PLANET_NAME,
                TEST_DESTINATION_PLANET_NAME, TEST_SPACE_SHIP_NAME, TEST_MISSION_PAYLOAD);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getMissionsList() throws Exception {
        List<Mission> allMissions = missionService.findAllMissions(TEST_USERNAME);
        allMissions.forEach(mission -> {
                    mission.setUserGameProfile(null);
                    mission.getSpaceShip().setUserGameProfile(null);
                }
        );

        String allMissionsJSON = new JacksonJsonProvider().toJson(allMissions);

        mockMvc
                .perform(get(GET_LIST_OF_MISSIONS_URI).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(allMissionsJSON));
    }
}