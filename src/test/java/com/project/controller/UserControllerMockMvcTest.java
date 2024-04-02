package com.project.controller;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.UserDTO;
import com.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
@ExtendWith(SpringExtension.class)
public class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void test_findById() throws Exception {
        var u1 = new UserDTO(1,"userU1","email",
                new ReaderDTO(1,"name","surname","email@gmail.com","address","0686377242"));
        Mockito.doReturn(u1).when(userService).findById(Mockito.any());
        mockMvc.perform(get("/user/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("userU1"))
                .andExpect(jsonPath("$.email").value("email"));
    }

}
