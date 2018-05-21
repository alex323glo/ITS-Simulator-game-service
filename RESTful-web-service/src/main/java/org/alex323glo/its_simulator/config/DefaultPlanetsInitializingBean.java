package org.alex323glo.its_simulator.config;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.PlanetDuplicationException;
import org.alex323glo.its_simulator.service.PlanetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultPlanetsInitializingBean implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlanetsInitializingBean.class);

    private final PlanetService planetService;

    @Autowired
    public DefaultPlanetsInitializingBean(PlanetService planetService) {
        this.planetService = planetService;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            planetService.createPlanet("P-001", 250L, 400L,
                    10, "#d43900", 2);
            planetService.createPlanet("P-002", 600L, 600L,
                    20, "#62d432", 1);
            planetService.createPlanet("Earth", 800L, 300L,
                    15, "#32cbd4", 0);

            LOGGER.info("Successfully initialized (prepared) System after start (registered Default Planets).");
        } catch (PlanetDuplicationException pde) {
            LOGGER.warn("Can't register Default Planets. Planet with such name was registered before.");
        } catch (AppException e) {
            LOGGER.error("Can't initialize (prepare) System after start: " + e.getMessage(), e);
        }
    }
}
