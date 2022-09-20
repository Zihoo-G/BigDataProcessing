package com.gazikel.controller;

import com.gazikel.pojo.File;
import com.gazikel.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/file/table")
    public String toFileTablePage(Model model) {
        List<File> allFile = fileService.getAllFile();
        model.addAttribute("files", allFile);
        return "build_table/file_table";
    }

    @GetMapping("/file/info")
    public String showFileInfo(@RequestParam("id") Integer id, Model model) {
        return "data_origin/table_info";
    }
}