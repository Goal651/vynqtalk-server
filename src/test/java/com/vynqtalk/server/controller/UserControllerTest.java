package com.vynqtalk.server.controller;

import com.vynqtalk.server.controller.user.UserController;
import com.vynqtalk.server.mapper.UserMapper;
import com.vynqtalk.server.service.user.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserMapper.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void deleteUser_returnsOk() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }
} 