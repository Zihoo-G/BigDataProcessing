package com.gazikel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Integer id;
    private String name;
    private String tableName;
    private Integer uid;
    private String extension;
    private String url;
}
