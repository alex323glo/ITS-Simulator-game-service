package org.alex323glo.its_simulator.controller;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import org.alex323glo.its_simulator.model.UserExtension;
import org.alex323glo.its_simulator.model.UserGameProfile;
import org.alex323glo.its_simulator.service.UserService;
import org.alex323glo.its_simulator.util.CircularityResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonalRoomControllerTest {

    private static final String SEND_USER_URI_DATA = "/private/personal-room/user-data";
    private static final String EDIT_USER_EXTENSION_URI = "/private/personal-room/edit";

    private static final String TEST_USERNAME = "Alex";
    private static final String TEST_PASSWORD = "12345678";
    private static final String TEST_EMAIL = "alex@mail.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService.deleteAllUserData();
        userService.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteAllUserData();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void sendUserData() throws Exception {
        UserGameProfile userGameProfile = CircularityResolver
                .resolveLazyGameProfile(userService.findUserGameProfile(TEST_USERNAME));
        userGameProfile.getUser().setPassword(null);

        String userGameProfileJSON = new JacksonJsonProvider().toJson(userGameProfile);

        mockMvc
                .perform(get(SEND_USER_URI_DATA).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(userGameProfileJSON));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    public void editUserExtension() throws Exception {
        String newTestEmail = "new_" + TEST_EMAIL;
        String newUserExtensionJSON = new JacksonJsonProvider()
                .toJson(UserExtension.builder().email(newTestEmail).build());

        mockMvc
                .perform(post(EDIT_USER_EXTENSION_URI)
                        .content(newUserExtensionJSON)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    UserExtension storedUserExtension = userService.findUserExtension(TEST_USERNAME);
                    assertEquals(newTestEmail, storedUserExtension.getEmail());

                    storedUserExtension = CircularityResolver
                            .resolveUserExtensionWithLazyGameProfile(storedUserExtension);

                    String storedUserExtensionJSON = new JacksonJsonProvider().toJson(storedUserExtension);
                    assertEquals(storedUserExtensionJSON, result.getResponse().getContentAsString());
                });
    }
}