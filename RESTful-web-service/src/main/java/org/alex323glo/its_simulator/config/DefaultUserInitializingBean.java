package org.alex323glo.its_simulator.config;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.exception.UserDuplicationException;
import org.alex323glo.its_simulator.service.SpaceShipService;
import org.alex323glo.its_simulator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializingBean implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserInitializingBean.class);

    private final UserService userService;
    private final SpaceShipService spaceShipService;
    private final Environment environment;

    @Autowired
    public DefaultUserInitializingBean(UserService userService, SpaceShipService spaceShipService, Environment environment) {
        this.userService = userService;
        this.spaceShipService = spaceShipService;
        this.environment = environment;
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
        String defaultAdminUsername = environment.getProperty("users.default_user.username");
        String defaultAdminPassword = environment.getProperty("users.default_user.password");
        String defaultAdminEmail = environment.getProperty("users.default_user.email");

        if (defaultAdminUsername == null || defaultAdminPassword == null || defaultAdminEmail == null) {
            LOGGER.error("Can't initialize (prepare) System after start: can't add root Admin to System " +
                    "(its credentials aren't set in application.properties file)");
            return;
        }

        try {
            userService.registerUser(defaultAdminUsername, defaultAdminPassword, defaultAdminEmail);

            spaceShipService.createSpaceShip(defaultAdminUsername, "Dragon-1",
                    50.0, 1, 30.0);

            spaceShipService.createSpaceShip(defaultAdminUsername, "Dragon-2",
                    70.0, 1, 15.0);

            LOGGER.info("Successfully initialized (prepared) System after start (registered Default User).");
        } catch (UserDuplicationException ude) {
            LOGGER.warn("Can't register Default User. User with such data (username and email) was registered before.");
        } catch (AppException e) {
            LOGGER.error("Can't initialize (prepare) System after start: " + e.getMessage(), e);
        }
    }
}
