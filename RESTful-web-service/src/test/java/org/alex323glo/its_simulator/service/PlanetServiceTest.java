package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.game.Planet;
import org.alex323glo.its_simulator.repository.PlanetRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetServiceTest {

    private static final String TEST_PLANET_NAME = "P-001";
    private static final Long TEST_PLANET_POSITION_X = 50L;
    private static final Long TEST_PLANET_POSITION_Y = 50L;

    private static Planet testPlanet;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetRepository planetRepository;

    @Before
    public void setUp() throws Exception {
        planetRepository.deleteAll();

        testPlanet = planetRepository.save(
                Planet.builder()
                        .name(TEST_PLANET_NAME)
                        .positionX(TEST_PLANET_POSITION_X)
                        .positionY(TEST_PLANET_POSITION_Y)
                        .build());
    }

    @After
    public void tearDown() throws Exception {
        planetRepository.deleteAll();
    }

    @Test
    public void findPlanet() throws AppException {
        Planet actualPlanet = planetService.findPlanet(TEST_PLANET_NAME);
        assertEquals(testPlanet, actualPlanet);
    }

    @Test
    public void findAllPlanets() throws AppException {
        List<Planet> planetList = planetService.findAllPlanets();
        List<Object> testPlanetList = Stream.builder().add(testPlanet).build().collect(Collectors.toList());
        assertEquals(testPlanetList, planetList);
    }
}