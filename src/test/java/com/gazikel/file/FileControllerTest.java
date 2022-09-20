package com.gazikel.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    // 使用日志
    private Logger logger = LoggerFactory.getLogger(FileControllerTest.class);

    @Autowired
    private MockMvc mockMvc;


}
