package org.alex323glo.its_simulator.controller.temp;
/*
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.repository.UserRepository;
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

import java.util.ArrayList;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonalRoomControllerTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "test@mail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
    public void sendUserData() throws Exception {
        testUser.getUserGameProfile().setUser(null);
        testUser.getUserExtension().setUser(null);
        testUser.getUserGameProfile().setMissions(new ArrayList<>());
        testUser.getUserGameProfile().setShips(new ArrayList<>());

        String testUserJSON = new JacksonJsonProvider().toJson(testUser);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/private/personal-room/user-data")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void editUserExtension() throws Exception {

        UserExtension newUserExtension =
                UserExtension.builder().id(0L).email("new_test@mail.com").build();

        testUser.getUserExtension().setEmail(newUserExtension.getEmail());
        testUser.getUserExtension().setUser(null);

        String newUserExtensionJSON = new JacksonJsonProvider().toJson(newUserExtension);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/private/personal-room/edit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(newUserExtensionJSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }
}*/