package com.gazikel.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    // 获取文件名称
    public static String getOriginalFilename(MultipartFile file) {
        return file.getOriginalFilename();
    }

    // 获取文件格式
    public static String getFileType(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name.substring(name.lastIndexOf("."));
    }

    // 保存文件
    public static void uploadFile(byte[] file, String filePath, String fileName) throws IOException {
        File targetFile = new File(filePath);

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
