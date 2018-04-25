package org.alex323glo.its_simulator.controller;

import org.alex323glo.its_simulator.model.User;
import org.alex323glo.its_simulator.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "test@mail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void getAuthenticatedUsername() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/get-authenticated-username")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(TEST_USERNAME));
    }

    @Test
    public void getAuthenticatedUsernameWithoutAuthentication() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/get-authenticated-username")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void registerNewUser() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD)
                        .param("email", TEST_EMAIL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isPermanentRedirect())
                .andExpect(MockMvcResultMatchers.header().string("location", "/login"));

        User user = userRepository.findUserByUserExtension_Email(TEST_EMAIL);

        assertNotNull(user);
        assertEquals(TEST_USERNAME, user.getUsername());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, user.getPassword()));
    }
}