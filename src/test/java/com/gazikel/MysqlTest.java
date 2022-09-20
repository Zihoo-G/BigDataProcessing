package com.gazikel;

import com.gazikel.mapper.MysqlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class MysqlTest {

    @Autowired
    private MysqlMapper mysqlMapper;

    @Test
    public void test01() {
        List<Map> maps = mysqlMapper.listTable();

        for (Map map : maps) {
            System.out.println(map.get("TABLE_NAME"));
        }

    }
}
