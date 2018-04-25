package org.alex323glo.its_simulator.service;

import org.alex323glo.its_simulator.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        System.out.println("Hello, setUp()!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Hello, tearDown()!");
    }

    @Test
    public void registerUser() {
        System.out.println("Hello, registerUser()!");
    }

    @Test
    public void findUser() {
        System.out.println("Hello, findUser()!");
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void changeUserExtension() {
    }

    @Test
    public void findAllUsers() {
    }
}