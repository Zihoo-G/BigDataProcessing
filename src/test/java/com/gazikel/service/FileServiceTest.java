package com.gazikel.service;

import com.gazikel.pojo.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import java.util.List;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    /**
     * 将上传文件信息添加到数据库
     */
    @Test
    public void addFile() {
        File file = new File(null, "add_file_test", "test1", 1, ".csv", "/E:/Component/target/classes//static/upload/2021-12-17/20211217141249_4a47aa0356e24b21bb0a9f68a264b446.xlsx");

        fileService.addFile(file);
    }


    /**
     * 展示所有上传的文件
     */
    @Test
    public void getAllFile() {
        List<File> allFile = fileService.getAllFile();

        for (File file : allFile) {
            System.out.println(file);
        }
    }

    /**
     * 根据id获取文件
     */
    @Test
    public void getFileById() {
        File file = fileService.getFileById(2);
        Assert.notNull(file, "file为空");

        file = fileService.getFileById(35);
        Assert.isNull(file, "file为空");
    }

    /**
     * 展示文件信息
     */
    @Test
    public void showFileInfo() {

    }

    /**
     * 根据原来的表名修改表名
     */
    @Test
    public void updateTableNameByTableName() {

        // 1. name在表中存在
        // 2. name在表中不存在
        fileService.updateTableNameByTableName("test1", "test_modify");

    }




}
