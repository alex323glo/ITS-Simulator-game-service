package org.alex323glo.its_simulator.controller;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.game.Mission;
import org.alex323glo.its_simulator.model.game.MissionMetrics;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.model.game.SpaceShip;
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
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MissionConstructorControllerTest {

    private static final String GET_ALL_PLANETS_URI = "/private/mission-constructor/planet-list";
    private static final String GET_ALL_FREE_SPACE_SHIPS_URI = "/private/mission-constructor/free-ship-list";
    private static final String ANALYZE_MISSION_URI = "/private/mission-constructor/analyze";
    private static final String CONSTRUCT_MISSION_URI = "/private/mission-constructor/construct";

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_SPACE_SHIP_NAME = "Dragon-1";
    private static final Integer TEST_SPACE_SHIP_LEVEL = 1;
    private static final Double TEST_SPACE_SHIP_SPEED = 15.50;
    private static final Double TEST_SPACE_SHIP_MAX_CARGO_CAPACITY = 1.0;

    private static final String TEST_FREE_SPACE_SHIP_NAME = "Dragon-2";

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
    private SpaceShipService spaceShipService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private MissionService missionService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();

        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);

        spaceShipService.createSpaceShip(TEST_USERNAME, TEST_SPACE_SHIP_NAME,
                TEST_SPACE_SHIP_MAX_CARGO_CAPACITY, TEST_SPACE_SHIP_LEVEL, TEST_SPACE_SHIP_SPEED);
        spaceShipService.createSpaceShip(TEST_USERNAME, TEST_FREE_SPACE_SHIP_NAME,
                TEST_SPACE_SHIP_MAX_CARGO_CAPACITY, TEST_SPACE_SHIP_LEVEL, TEST_SPACE_SHIP_SPEED);

        planetService.createPlanet(TEST_START_PLANET_NAME,
                TEST_START_PLANET_POSITION_X, TEST_START_PLANET_POSITION_Y);

        planetService.createPlanet(TEST_DESTINATION_PLANET_NAME,
                TEST_DESTINATION_PLANET_POSITION_X, TEST_DESTINATION_PLANET_POSITION_Y);

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
    public void getAllPlanets() throws Exception {
        List<Planet> planetList = planetService.findAllPlanets();
        String planetListJSON = new JacksonJsonProvider().toJson(planetList);

        mockMvc
                .perform(get(GET_ALL_PLANETS_URI).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(planetListJSON));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getAllFreeShips() throws Exception {
        mockMvc
                .perform(get(GET_ALL_FREE_SPACE_SHIPS_URI).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(result -> {
                    List<SpaceShip> freeSpaceShipList = spaceShipService.findAllFreeShips(TEST_USERNAME);
                    freeSpaceShipList.forEach(ship -> ship.setUserGameProfile(
                            CircularityResolver.resolveLazyGameProfile(
                                    ship.getUserGameProfile())));

                    String freeSpaceShipListJSON = new JacksonJsonProvider().toJson(freeSpaceShipList);
                    assertEquals(freeSpaceShipListJSON, result.getResponse().getContentAsString());
                });
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void analyzeMission() throws Exception {
        MissionMetrics missionMetrics = missionService.generateMissionMetrics(TEST_USERNAME,
                TEST_START_PLANET_NAME, TEST_DESTINATION_PLANET_NAME,
                TEST_FREE_SPACE_SHIP_NAME, TEST_MISSION_PAYLOAD);
        String missionMetricsJSON = new JacksonJsonProvider().toJson(missionMetrics);

        mockMvc
                .perform(get(ANALYZE_MISSION_URI)
                        .param("start", TEST_START_PLANET_NAME)
                        .param("destination", TEST_DESTINATION_PLANET_NAME)
                        .param("ship", TEST_FREE_SPACE_SHIP_NAME)
                        .param("payload", TEST_MISSION_PAYLOAD.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(missionMetricsJSON));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void constructNewMission() throws Exception {
        mockMvc
                .perform(post(CONSTRUCT_MISSION_URI)
                        .param("start", TEST_START_PLANET_NAME)
                        .param("destination", TEST_DESTINATION_PLANET_NAME)
                        .param("ship", TEST_FREE_SPACE_SHIP_NAME)
                        .param("payload", TEST_MISSION_PAYLOAD.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    Map<String, Object> parsedMap = new JacksonJsonParser().parseMap(content);
                    Object objectId = parsedMap.get("id");
                    Long id = (objectId instanceof Long) ? (Long) objectId : Long.parseLong(objectId.toString());
                    Mission savedMission = missionService.findMission(TEST_USERNAME, id);
                    assertNotNull(savedMission);
                });
    }
}