package org.alex323glo.its_simulator.controller;

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
import org.springframework.boot.json.JacksonJsonParser;
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

// TODO : MAKE IT WORK!

@Ignore // TODO : REMOVE !
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
        testUser.setUserExtension(userService.findUserExtension(TEST_USERNAME));
        testUser.setUserGameProfile(userService.findUserGameProfile(TEST_USERNAME));
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void sendUserData() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/private/personal-room/user-data")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        new JacksonJsonProvider().toJson(testUser)
                ));
    }

    @Test
    @WithMockUser
    public void editUserExtension() throws Exception {

        UserExtension newUserExtension =
                UserExtension.builder().email("new_test@mail.com").build();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/private/personal-room/edit")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new JacksonJsonProvider().toJson(newUserExtension))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        new JacksonJsonProvider().toJson(testUser.getUserExtension())
                ));

    }
}