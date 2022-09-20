package com.gazikel.mapper;

import com.gazikel.pojo.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileMapper {

    void addFile(File file);

    List<File> getAllFile();

    int updateTableNameByTableName(@Param("name") String name, @Param("tableName") String tableName);

    /**
     * 根据id查找文件
     * Find files by ID
     * @param id
     * @return
     */
    File getFileById(@Param("id") Integer id);

}
