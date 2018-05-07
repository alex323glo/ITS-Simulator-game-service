package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceTest {

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    private static User testUser;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();

        UserExtension userExtension = UserExtension.builder()
                .email(TEST_EMAIL)
                .registrationTime(LocalDateTime.now())
                .build();
        UserGameProfile userGameProfile = UserGameProfile.builder()
                .ships(new ArrayList<>())
                .missions(new ArrayList<>())
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
    public void loadUserByUsername() {
        UserDetails expectedUserDetails = testUser.generateUserDetails();
        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(TEST_USERNAME);

        assertEquals(expectedUserDetails, actualUserDetails);
    }
}
