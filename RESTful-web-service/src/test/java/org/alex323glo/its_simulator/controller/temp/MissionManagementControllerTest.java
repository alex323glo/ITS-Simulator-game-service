package org.alex323glo.its_simulator.controller.temp;
/*
import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;
import org.alex323glo.its_simulator.repository.MissionRepository;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.alex323glo.its_simulator.service.MissionService;
import org.alex323glo.its_simulator.service.PlanetService;
import org.alex323glo.its_simulator.service.SpaceShipService;
import org.alex323glo.its_simulator.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MissionManagementControllerTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "test@mail.com";

    private static final String TEST_SHIP_1_NAME = "Enterprise";
    private static final Double TEST_SHIP_1_MAX_CARGO_CAPACITY = 100.00;

    private static final String TEST_SHIP_2_NAME = "Standard";
    private static final Double TEST_SHIP_2_MAX_CARGO_CAPACITY = 150.50;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceShipService spaceShipService;

    @Autowired
    private PlanetService planetService;

    private User testUser;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
        testUser = userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getMissionsList() throws Exception {

        UserGameProfile gameProfile = userService.findUserGameProfile(TEST_USERNAME);

        SpaceShip enterpriseShip =
                spaceShipService.createShip(gameProfile, TEST_SHIP_1_NAME, TEST_SHIP_1_MAX_CARGO_CAPACITY);

        SpaceShip standardShip =
                spaceShipService.createShip(gameProfile, TEST_SHIP_2_NAME, TEST_SHIP_2_MAX_CARGO_CAPACITY);

        Planet planet1 = planetService.createNewPlanet("P1");
        Planet planet2 = planetService.createNewPlanet("P2");

        Mission testMission1 = missionService.createMission(gameProfile, enterpriseShip, planet1, planet2);
        Mission testMission2 = missionService.createMission(gameProfile, standardShip, planet2, planet1);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/private/mission-management/missions")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }
}*/