package com.gazikel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MysqlServiceTest {

    @Autowired
    private MysqlService mysqlService;

    private final String tableName = "test1";
    private final String fieldName = "sex";

    @Test
    public void listTable() {
        List<Map> maps = mysqlService.listTable();
        for (Map map : maps) {
            System.out.println(map);
        }
        Assert.notNull(maps, "没有查询到数据");
    }

    @Test
    public void listTableName() {
        List<String> tableName = mysqlService.listTableName();

        for (String s : tableName) {
            System.out.println(s);
        }

        Assert.notNull(tableName, "没有查询到数据");
    }

    @Test
    public void listColumnName() {
        List<String> file = mysqlService.listColumnName("file");
        for (String s : file) {
            System.out.println(s);
        }

        Assert.notNull(file, "没有查询到数据");
    }

    @Test
    public void createTable() {
        String sql = "create table " + tableName + "(id int primary key, `name` varchar(10), `sex` tinyint(1))";
        mysqlService.createTable(sql);
        // 查找出所有的表名
        List<String> tableName = mysqlService.listTableName();
        // 断言 表名 中 是否存在 新建的表
        assert tableName.contains("test1");
    }

    /**
     * 插入一条语句
     */
    @Test
    public void insert() {
        List<String> list = new ArrayList<>();

        list.add("insert into " + tableName + " values(1, 'zihoo', 1)");
        list.add("insert into " + tableName + " values(2, 'gazikel', 0)");
        list.add("insert into " + tableName + " values(3, null, 0)");

        mysqlService.insert(list);
    }

    /**
     * 删除一个字段
     */
    @Test
    public void dropField() {
        mysqlService.dropFiled(tableName, fieldName);

        List<String> columnName = mysqlService.listColumnName(tableName);

        assert !columnName.contains(fieldName);
    }

    /**
     * 获取总共的行数
     */
    @Test
    public void getRowSumNumber() {
        Integer number = mysqlService.getRowSumNumber(tableName);
        System.out.println("总共有"+number+"行");

        assert number == 3;
    }

    /**
     * 更新一行中为空的值
     */
    @Test
    public void updateNullRow() {
        /**
         * value值的长度
         * value是否带了 ''
         */
        mysqlService.updateNullRow(tableName, "`name`", "'value'","3");
    }

    @Test
    public void deleteRow() {

    }

    /**
     * 导出数据
     */
    @Test
    public void dataOutput() {
        mysqlService.dataOutput(tableName, mysqlService.listColumnName(tableName), mysqlService.getAllData(tableName));
    }
}
