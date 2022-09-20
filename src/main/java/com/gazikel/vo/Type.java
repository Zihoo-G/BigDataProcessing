package com.gazikel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Type {

    private String columnName;
    private String columnType;
    private String columnTypeLong;
    private boolean isPrimaryKey;

}
