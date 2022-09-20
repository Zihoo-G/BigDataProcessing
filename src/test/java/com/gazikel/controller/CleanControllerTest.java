package com.gazikel.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class CleanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @Test
    void toTypeHandlePage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/clean/type"))
                .andReturn();

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    void toRepetitionHandlePage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/clean/repetition"))
                .andReturn();

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    void toScreenHandlePage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/clean/screen"))
                .andReturn();

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    void toJoinHandlePage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/clean/join"))
                .andReturn();

        System.out.println(result.getResponse().getStatus());
    }

    @Test
    void toAddressHandlePage() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/clean/address"))
                .andReturn();

        System.out.println(result.getResponse().getStatus());
    }

//    @Test
//    @DisplayName("")
//    void getColumnList() throws Exception {
//        String tableName = "test_controller";
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/clean/type/column/list")
//                .param("tableName", tableName)).andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }


}
