package org.alex323glo.its_simulator.controller;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.service.PlanetService;
import org.alex323glo.its_simulator.service.UserService;
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
public class SpaceMapControllerTest {

    private static final String GET_ALL_PLANETS_URI = "/private/space-map/planets";

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static final String TEST_START_PLANET_NAME = "P-001";
    private static final Long TEST_START_PLANET_POSITION_X = 50L;
    private static final Long TEST_START_PLANET_POSITION_Y = 50L;
    private static final Integer TEST_START_PLANET_RADIUS = 10;
    private static final String TEST_START_PLANET_COLOR = "#112233";
    private static final Integer TEST_START_PLANET_CIRCLES_NUMBER = 1;

    private static final String TEST_DESTINATION_PLANET_NAME = "P-002";
    private static final Long TEST_DESTINATION_PLANET_POSITION_X = 300L;
    private static final Long TEST_DESTINATION_PLANET_POSITION_Y = 300L;
    private static final Integer TEST_DESTINATION_PLANET_RADIUS = 20;
    private static final String TEST_DESTINATION_PLANET_COLOR = "#445566";
    private static final Integer TEST_DESTINATION_PLANET_CIRCLES_NUMBER = 2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PlanetService planetService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();

        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);

        planetService.createPlanet(TEST_START_PLANET_NAME,
                TEST_START_PLANET_POSITION_X, TEST_START_PLANET_POSITION_Y,
                TEST_START_PLANET_RADIUS, TEST_START_PLANET_COLOR, TEST_START_PLANET_CIRCLES_NUMBER);

        planetService.createPlanet(TEST_DESTINATION_PLANET_NAME,
                TEST_DESTINATION_PLANET_POSITION_X, TEST_DESTINATION_PLANET_POSITION_Y,
                TEST_DESTINATION_PLANET_RADIUS, TEST_DESTINATION_PLANET_COLOR, TEST_DESTINATION_PLANET_CIRCLES_NUMBER);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteAllUserData();
        planetService.deleteAllPlanets();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getAllPlanets() throws Exception {
        List<Planet> allPlanets = planetService.findAllPlanets();
        String allPlanetsJSON = new JacksonJsonProvider().toJson(allPlanets);

        mockMvc
                .perform(get(GET_ALL_PLANETS_URI).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(allPlanetsJSON));
    }
}