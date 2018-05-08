package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.exception.AppException;
import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    private static final String REGISTRATION_URI = "/register";
    private static final String GET_AUTH_USERNAME_URI = "/get-authenticated-username";

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();

        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    }

    @After
    public void tearDown() throws AppException {
        userService.deleteAllUserData();
    }

    @Test
    public void registerNewUser() throws Exception {
        String anotherTestUsername = "another_" + TEST_USERNAME;
        String anotherTestPassword = "another_" + TEST_PASSWORD;
        String anotherTestEmail = "another_" + TEST_EMAIL;

        mockMvc
                .perform(post(REGISTRATION_URI)
                        .param("username", anotherTestUsername)
                        .param("password", anotherTestPassword)
                        .param("email", anotherTestEmail)
                        .with(csrf()))
                .andExpect(status().isPermanentRedirect())
                .andExpect(header().string("Location", "/login"))
                .andDo(result -> {
                    UserExtension userExtension = userService.findUserExtension(anotherTestUsername);
                    assertNotNull(userExtension);
                    assertEquals(anotherTestEmail, userExtension.getEmail());
                    User user = userExtension.getUser();
                    assertTrue(passwordEncoder.matches(anotherTestPassword, user.getPassword()));
                });
    }

    @Test
    @WithMockUser(TEST_USERNAME)
    public void getAuthenticatedUsername() throws Exception {
        mockMvc
                .perform(get(GET_AUTH_USERNAME_URI).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_USERNAME));
    }
}