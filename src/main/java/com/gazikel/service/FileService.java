package com.gazikel.service;

import com.gazikel.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface FileService {

    public void addFile(File file);

    public String uploadFile(MultipartFile file, HttpSession session) throws IOException;

    List<File> getAllFile();

    /**
     * 更改表名
     * Change table name
     * @param name
     * @param tableName
     * @return
     */
    int updateTableNameByTableName(String name, String tableName);

    List<List<Object>> showFileInfo(MultipartFile file, HttpSession session);

    /**
     * 根据id查找文件
     * @param id
     * @return
     */
    File getFileById(Integer id);
}
