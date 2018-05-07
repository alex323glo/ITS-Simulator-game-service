package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.repository.UserExtensionRepository;
import org.alex323glo.its_simulator.repository.UserGameProfileRepository;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ImportResource
public class UserServiceTest {

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static User testUser;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExtensionRepository userExtensionRepository;

    @Autowired
    private UserGameProfileRepository userGameProfileRepository;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();

        UserExtension userExtension = UserExtension.builder()
                .email(TEST_EMAIL)
                .registrationTime(LocalDateTime.now())
                .build();
        UserGameProfile userGameProfile = UserGameProfile.builder()
                .missions(new ArrayList<>())
                .ships(new ArrayList<>())
                .build();
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .userExtension(userExtension)
                .userGameProfile(userGameProfile)
                .build();

        userExtension.setUser(user);
        userGameProfile.setUser(user);

        testUser = userRepository.save(user);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void registerUser() throws AppException {
        String newTestUsername = "username";
        String newTestPassword = "87654321";
        String newTestEmail = "username@mail.com";

        User registeredUser = userService.registerUser(newTestUsername, newTestPassword, newTestEmail);

        User expectedUser = userRepository.findByUsername(newTestUsername);

        expectedUser.getUserGameProfile().setUser(null);
        expectedUser.getUserGameProfile().setShips(null);
        expectedUser.getUserGameProfile().setMissions(null);
        expectedUser.getUserExtension().setUser(null);

        registeredUser.getUserGameProfile().setUser(null);
        registeredUser.getUserGameProfile().setShips(null);
        registeredUser.getUserGameProfile().setMissions(null);
        registeredUser.getUserExtension().setUser(null);

        assertEquals(expectedUser, registeredUser);
    }

    @Test
    public void findUserGameProfile() throws AppException {
        UserGameProfile expectedUserGameProfile = userGameProfileRepository.findByUser_Username(TEST_USERNAME);

        expectedUserGameProfile.getUser().setUserExtension(null);
        expectedUserGameProfile.getUser().setUserGameProfile(null);
        expectedUserGameProfile.setShips(null);
        expectedUserGameProfile.setMissions(null);

        UserGameProfile actualUserGameProfile = userService.findUserGameProfile(TEST_USERNAME);

        actualUserGameProfile.getUser().setUserExtension(null);
        actualUserGameProfile.getUser().setUserGameProfile(null);
        actualUserGameProfile.setShips(null);
        actualUserGameProfile.setMissions(null);

        assertEquals(expectedUserGameProfile, actualUserGameProfile);
    }

    @Test
    public void findUserExtension() throws AppException {
        UserExtension expectedUserExtension = userExtensionRepository.findByEmail(TEST_EMAIL);

        expectedUserExtension.getUser().setUserExtension(null);
        expectedUserExtension.getUser().setUserGameProfile(null);

        UserExtension actualUserExtension = userService.findUserExtension(TEST_USERNAME);

        actualUserExtension.getUser().setUserExtension(null);
        actualUserExtension.getUser().setUserGameProfile(null);

        assertEquals(expectedUserExtension, actualUserExtension);
    }

    @Test
    public void changeUserExtension() throws AppException {
        UserExtension newTestUserExtension = UserExtension.builder()
                .email("new_" + TEST_EMAIL)
                .build();

        UserExtension updatedUserExtension = userService.changeUserExtension(TEST_USERNAME, newTestUserExtension);

        UserExtension expectedUserExtension = testUser.getUserExtension();
        expectedUserExtension.setEmail(newTestUserExtension.getEmail());

        expectedUserExtension.getUser().getUserGameProfile().setUser(null);
        expectedUserExtension.getUser().getUserExtension().setUser(null);
        updatedUserExtension.getUser().getUserGameProfile().setUser(null);
        updatedUserExtension.getUser().getUserExtension().setUser(null);

        assertEquals(expectedUserExtension, updatedUserExtension);
    }


}