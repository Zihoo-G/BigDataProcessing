package com.gazikel.service;

import com.gazikel.mapper.FileMapper;
import com.gazikel.pojo.File;
import com.gazikel.pojo.User;
import com.gazikel.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public void addFile(File file) {
        fileMapper.addFile(file);
    }

    @Override
    public String uploadFile(MultipartFile file, HttpSession session) throws IOException {
        String filename = file.getOriginalFilename();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        User user = (User) session.getAttribute("user");
        // 文件后缀名
        String extension = FileUtil.getFileType(file);
        // 表名
        String tableName = simpleDateFormat.format(new Date()) + "_" + UUID.randomUUID().toString().replace("-", "");

        // 上传文件路径
        String filePath = ResourceUtils.getURL("classpath:").getPath() + Constants.BASE_PATH + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        java.io.File fileDir = new java.io.File(filePath);
        if (!fileDir.exists())
            fileDir.mkdirs();

        // 上传文件到服务器
        file.transferTo(new java.io.File(fileDir, tableName + extension));

        // 如果为csv文件，则转换为xlsx文件
        if (".csv".equals(extension)) {
            CsvToXlsxUtil.csvToXLSX(filePath + "/" + tableName + extension);
        }

        List<String> fieldColumn;
        List<String> commentColumn;
        List<String> unitsColumn;
        List<String> typeColumn;
        String realPath = filePath + "/" + tableName + extension;

        // 解析三行的内容
        if (".csv".equals(extension)) {
            // 1. 字段行
            fieldColumn = POIUtil.getColumnNameByLine(filePath + "/" + tableName + ".xlsx", ".xlsx",3, session);
            // 2. 注释行
            commentColumn = POIUtil.getColumnNameByLine(filePath + "/" + tableName + ".xlsx", ".xlsx", 1, session);
            // 3. 单位行
            unitsColumn = POIUtil.getColumnNameByLine(filePath + "/" + tableName + ".xlsx", ".xlsx", 2, session);
            // 通过测试行获取字段类型
            typeColumn = POIUtil.getColumnTypeByLine(filePath + "/" + tableName + ".xlsx", ".xlsx", 4, session);
        } else {
            // 1. 字段行
            fieldColumn = POIUtil.getColumnNameByLine(realPath, extension,3, session);
            // 2. 注释行
            commentColumn = POIUtil.getColumnNameByLine(realPath, extension, 1, session);
            // 3. 单位行
            unitsColumn = POIUtil.getColumnNameByLine(realPath, extension, 2, session);
            // 通过测试行获取字段类型
            typeColumn = POIUtil.getColumnTypeByLine(realPath, extension, 4, session);
        }


        // 根据内容生成sql语句
        String sql  = "CREATE TABLE " + tableName + "(";

        for (int i = 0; i < fieldColumn.size(); i++) {
            if (i == 0) {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " NOT NULL PRIMARY KEY COMMENT '" + commentColumn.get(i) + "',";
            } else if(i == fieldColumn.size()-1) {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " COMMENT '" + commentColumn.get(i) + "')";
            } else {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " COMMENT '" + commentColumn.get(i) + "',";
            }
        }

        System.out.println(sql);
        return sql;
    }

    @Override
    public List<File> getAllFile() {
        return fileMapper.getAllFile();
    }

    @Override
    public int updateTableNameByTableName(String name, String tableName) {
        return fileMapper.updateTableNameByTableName(name, tableName);
    }

    @Override
    public List<List<Object>> showFileInfo(MultipartFile file, HttpSession session) {
        int rowNumber = POIUtil2.getRowNumber(file, session);
        List<List<Object>> list = new ArrayList<>();
        List<Object> columnValueByLine;

        for (int i = 0; i < rowNumber; i++) {
            columnValueByLine = POIUtil2.getColumnValueByLine(file, i, session);
            list.add(columnValueByLine);
        }

        return list;
    }

    @Override
    public File getFileById(Integer id) {
        return fileMapper.getFileById(id);
    }


}
