package com.gazikel.controller;

import com.gazikel.pojo.User;
import com.gazikel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping({"/login", "/"})
    public String toLoginPage() {
        return "login/login";
    }

    @GetMapping("/index")
    public String toIndexPage() {
        return "index";
    }

    @PostMapping("/login.do")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {

        User user = userService.getUserByName(username);
        if (password.equals(user.getPassword())) {
            session.setAttribute("user", user);
            return "redirect:/index";
        }

        return "redirect:/login";

    }
}
