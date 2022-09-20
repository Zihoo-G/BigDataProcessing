package com.gazikel.controller;

import com.gazikel.pojo.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BuildTableControllerTest {

    private Logger logger = LoggerFactory.getLogger(BuildTableControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    private static MockHttpSession session;

    @BeforeAll
    public static void setup() {
        session = new MockHttpSession();
    }

    @Test
    public void toBuildTablePage() throws Exception {

        // 1. file 为null
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/table")
                .contentType("text/html") // 设置请求头信息
                .accept(MediaType.APPLICATION_JSON); // 设置请求Accept头信息

        // 发送请求，得到请求结果
        ResultActions perform = mockMvc.perform(request);

        // 校验请求结果
        perform.andExpect(status().isOk());

        // 获取执行完成后的返回结果
        MvcResult mvcResult = perform.andReturn();

        // 得到执行后的响应
        MockHttpServletResponse response = mvcResult.getResponse();

        // 打印结果
        logger.info("用户状态:{}", response.getStatus());
        logger.info("用户信息:{}", response.getContentAsString());

        // 2. file不为null
    }

    @Test
    public void toBuildPage() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/table"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
    }

    @Test
    public void fileUpload() throws Exception {

        User user = new User(1, "郭智昊", "123456", "学生");

        session.setAttribute("user", user);


        MockMultipartFile file = new MockMultipartFile("importFile", "test1.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new FileInputStream("E:\\Component\\test1.xlsx"));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/table/file-upload")
                .file(file).session(session)).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("清除session，并返回页面")
    public void tableClear() throws Exception {
        session.setAttribute("file_table", "1111");

        System.out.println("----->" + session.getAttribute("file_table"));
        mockMvc.perform(MockMvcRequestBuilders.get("/table/clear")
                .session(session)).andExpect(status().is3xxRedirection()).andReturn();

        System.out.println("----->" + session.getAttribute("file_table"));
    }

    @Test
    @DisplayName("上传文件")
    void getFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/table/get/file")
                .file(new MockMultipartFile("importFile", "2018_02.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new FileInputStream("E:\\Component\\2018_02.xlsx"))))
                .andExpect(status().is3xxRedirection())
                .andReturn();

    }

    @Test
    @DisplayName("建表")
    void buildTable() throws Exception {

        User user = new User(1, "郭智昊", "123456", "学生");

        session.setAttribute("user", user);


        MockMultipartFile file = new MockMultipartFile("importFile", "test1.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new FileInputStream("E:\\Component\\test1.xlsx"));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/table/file-upload")
                .file(file).session(session)).andReturn();

        System.out.println(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
        System.out.println(result.getResponse().getContentAsString());

        MockMultipartFile importFile = new MockMultipartFile("test1.xlsx", "test1.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new FileInputStream("E:\\Component\\test1.xlsx"));

//        System.out.println(importFile.getOriginalFilename());
        session.setAttribute("file_table", (MultipartFile) importFile);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/table/input")
                .param("name", "test_controller")
                .param("field", "3")
                .param("comment", "1")
                .param("units", "0")
                .param("test", "4").session(session)).andExpect(status().isOk()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
