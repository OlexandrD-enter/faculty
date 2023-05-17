package com.app.faculty.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testPageAccessForUser() throws Exception {
        mockMvc.perform(get("http://localhost:8080/admin/users"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testPageAccessForAdmin() throws Exception {
        mockMvc.perform(get("http://localhost:8080/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPageAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("http://localhost:8080/admin/users"))
                .andExpect(status().is(302));
    }

}