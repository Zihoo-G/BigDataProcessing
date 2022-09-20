package com.gazikel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Missing {

    private String columnName;
    private Integer rowNumber;
    private Integer missingRowNumber;
}
