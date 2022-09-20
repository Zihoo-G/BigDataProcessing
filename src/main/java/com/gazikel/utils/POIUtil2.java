package com.gazikel.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class POIUtil2 {
    private static Workbook workbook = null;
    private static Sheet sheet = null;

    private static final String VARCHAR = "varchar(100)";
    private static final String BOOLEAN = "tinyint(1)";
    private static final String DATETIME = "datetime";
    private static final String DOUBLE = "double";
    private static final String INTEGER = "int";


    /**
     * 读取第一行内容
     * Read the first line
     * @param file
     * @param session
     * @return
     */
    public static List<String> getColumnNameXLS(MultipartFile file, HttpSession session) {
        return getColumnNameByLine(file, 1, session);
    }

    /**
     * 获取特定某一行的列值
     * Gets the column value of a specific row
     * @param file
     * @param lineNum
     * @return
     */
    public static List<String> getColumnNameByLine(MultipartFile file, int lineNum, HttpSession session) {

        List<String> list = new ArrayList<>();
        FileInputStream inputStream = null;

        try {
            if (".csv".equals(FileUtil.getFileType(file))) {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
                System.out.println(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
            } else {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
            }

            // inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
            if (".xls".equals(FileUtil.getFileType(file))) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(FileUtil.getFileType(file)) || ".csv".equals(FileUtil.getFileType(file))) {
                workbook = new XSSFWorkbook(inputStream);
            }

            sheet = workbook.getSheetAt(0);

            Row row = sheet.getRow(lineNum - 1);
            if (row != null) {
                int columnNum = row.getPhysicalNumberOfCells();
                Cell cell = null;
                for (int i = 0; i < columnNum; i++) {
                    cell = row.getCell(i);
                    list.add(cell.getStringCellValue());
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static List<Object> getColumnValueByLine(MultipartFile file, int lineNum, HttpSession session) {
        List<Object> list = new ArrayList<>();
        FileInputStream inputStream = null;

        try {
            if (".csv".equals(FileUtil.getFileType(file))) {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
                System.out.println(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
            } else {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
            }
            if (".xls".equals(FileUtil.getFileType(file))) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(FileUtil.getFileType(file))) {
                workbook = new XSSFWorkbook(inputStream);
            }

            sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(lineNum - 1);
            if (row!=null) {
                int columnNum = row.getPhysicalNumberOfCells();
                for (int i = 0; i < columnNum; i++) {
                    Cell cell = row.getCell(i);
                    CellType cellType = cell.getCellType();
                    switch (cellType) {
                        case STRING:
                            list.add(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            if (cell.getBooleanCellValue() == true) {
                                list.add(1);
                            } else {
                                list.add(0);
                            }
                            break;
                        case BLANK:
                            list.add(null);
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                list.add(cell.getDateCellValue());
                            } else {
                                cell.setCellType(CellType.STRING);
                                String s = cell.toString();
                                if (s.contains(".")) {
                                    list.add(cell.getNumericCellValue());
                                } else {
                                    // 手机号
                                    if (s.length() >= 11) {
                                        list.add(s);
                                    } else {
                                        list.add(Integer.parseInt(s));
                                    }

                                }
                            }
                            break;
                        default:
                            list.add(cell.getStringCellValue());
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static List<String> getColumnTypeByLine(MultipartFile file, int lineNum, HttpSession session) {

        List<String> list = new ArrayList<>();
        FileInputStream inputStream = null;

        try {
            if (".csv".equals(FileUtil.getFileType(file))) {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
                System.out.println(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
            } else {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
            }
            if (".xls".equals(FileUtil.getFileType(file))) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(FileUtil.getFileType(file))) {
                workbook = new XSSFWorkbook(inputStream);
            }

            sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(lineNum - 1);

            int columnNum = row.getPhysicalNumberOfCells();

            for (int i = 0; i < columnNum; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    switch (cellType) {
                        // 字符串类型
                        case STRING:
                            list.add(VARCHAR);
                            break;
                        case BOOLEAN:
                            list.add(BOOLEAN);
                            break;
                        case NUMERIC:
                            // 日期和时间
                            if (DateUtil.isCellDateFormatted(cell)) {
                                list.add(DATETIME);
                            } else {
                                cell.setCellType(CellType.STRING);
                                String s = cell.toString();
                                if (s.contains(".")) {
                                    // 小数
                                    list.add(DOUBLE);
                                } else {
                                    if (s.length() >=11) {
                                        // 手机号
                                        list.add(VARCHAR);
                                    } else {
                                        list.add(INTEGER);
                                    }
                                }

                            }
                            break;
                        default:
                            list.add(VARCHAR);
                            break;
                    }

                } else {
                    list.add(VARCHAR);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static int getRowNumber(MultipartFile file, HttpSession session) {
        int number = 0;
        FileInputStream inputStream = null;

        try {
            if (".csv".equals(FileUtil.getFileType(file))) {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
                System.out.println(session.getServletContext().getRealPath("file/") + file.getOriginalFilename().split(".csv")[0] + ".xlsx");
            } else {
                inputStream = new FileInputStream(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
            }
            if (".xls".equals(FileUtil.getFileType(file))) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(FileUtil.getFileType(file))) {
                workbook = new XSSFWorkbook(inputStream);
            }

            sheet = workbook.getSheetAt(0);
            number = sheet.getPhysicalNumberOfRows();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return number;
    }
}
